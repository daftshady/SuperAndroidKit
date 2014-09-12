package com.daftshady.superandroidkit.authentication;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AccountsException;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.daftshady.superandroidkit.exception.SuperAndroidKitException;

/**
 * Abstract base class for android account model. This class wraps
 * `android.accounts.Account` to easily manage authentication info. Should
 * inherit this, when implementing account model connected with android internal
 * account system.
 * 
 * @author parkilsu
 *
 */
public abstract class AbstractAccount {

	public static interface OnGetAuthTokenListener {
		void onGetAuthToken(String token);

		void onFailure(Exception e);
	}

	protected static String TAG = "AbstractAccount";

	private Context mContext;
	private AccountManager mAccountManager;

	protected Account mAccount;

	protected abstract String accountType();

	protected abstract String tokenType();

	protected AbstractAccount(Context context, String accountType) {
		mContext = context;
		mAccountManager = AccountManager.get(context);
		mAccount = AccountUtils.getAccount(context, accountType);
		if (mAccount == null) {
			throw new SuperAndroidKitException("No available account");
		}
	}

	protected AbstractAccount(Context context, Account account) {
		mContext = context;
		mAccountManager = AccountManager.get(context);
		mAccount = account;
		if (mAccount == null) {
			throw new SuperAndroidKitException("No available account");
		}
	}

	/**
	 * Get username. Basically, username is `email`
	 * 
	 * @return userName
	 */
	public String getName() {
		return mAccount.name;
	}

	/**
	 * Get user password
	 * 
	 * @return password
	 */
	public String getPassword() {
		return mAccountManager.getPassword(mAccount);
	}

	/**
	 * Get account type
	 * 
	 * @return accountType
	 */
	public String getType() {
		return accountType();
	}

	/**
	 * Get auth token type tokenType should be retrieved from real class.
	 * 
	 * @return authTokenType
	 */
	public String getAuthTokenType() {
		return tokenType();
	}

	/**
	 * Get current account manager
	 * 
	 * @return acccountManager
	 */
	public AccountManager getManager() {
		return mAccountManager;
	}

	/**
	 * Get existing account
	 * 
	 * @return account if there exists, else null
	 */
	public Account getAccount() {
		return mAccount;
	}

	/**
	 * Get local cached token
	 * 
	 * @return authToken. This token can be expired.
	 */
	public String getCachedAuthToken() {
		return mAccountManager.peekAuthToken(mAccount, tokenType());
	}

	/**
	 * Get AuthToken. This method must not be used in main thread, because
	 * AccountManagerFuture may block thread.
	 * 
	 * @return authToken from account. If there is no authToken, manager try to
	 *         re-take authToken from server. If there is no valid authToken
	 *         even after re-taking, there may be server problem.
	 */

	public String getAuthToken() {
		return getAuthToken(tokenType());
	}

	/**
	 * Get AuthToken. This method must not be used in main thread, because
	 * AccountManagerFuture may block thread.
	 * 
	 * @param context
	 * @param authTokenType
	 * @return authToken from account. If there is no authToken, manager try to
	 *         re-take authToken from server. If there is no valid authToken
	 *         even after re-taking, there may be server problem.
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	public String getAuthToken(String authTokenType) {
		if (mAccount == null) {
			return null;
		}
		try {
			final AccountManagerFuture<Bundle> future;
			if (mContext instanceof Activity) {
				future = mAccountManager.getAuthToken(mAccount, authTokenType,
						null, (Activity) mContext, null, null);
			} else {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
					future = mAccountManager.getAuthToken(mAccount,
							authTokenType, null, false, null, null);
				} else {
					future = mAccountManager.getAuthToken(mAccount,
							authTokenType, false, null, null);
				}
			}
			Bundle result = future.getResult();
			if (result == null)
				return null;
			return result.getString(AccountManager.KEY_AUTHTOKEN, null);
		} catch (AccountsException e) {
			Log.e(TAG, "Fatal error : server has invalid auth information", e);
			return null;
		} catch (IOException e) {
			Log.e(TAG, "Auth token lockup failed", e);
			return null;
		} catch (NullPointerException e) {
			Log.e(TAG, "No available account", e);
			return null;
		}
	}

	/**
	 * Get authToken asynchronously
	 * 
	 * @param listener
	 */
	public void getAuthToken(OnGetAuthTokenListener listener) {
		getAuthToken(tokenType(), listener);
	}

	/**
	 * Get authToken asynchronously
	 * 
	 * @param listener
	 */
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public void getAuthToken(String authTokenType,
			final OnGetAuthTokenListener listener) {
		if (mAccount == null) {
			listener.onFailure(new RuntimeException("There is no account"));
			return;
		}
		AccountManagerCallback<Bundle> callback = new AccountManagerCallback<Bundle>() {

			@Override
			public void run(AccountManagerFuture<Bundle> future) {
				try {
					Bundle result = future.getResult();
					String token = null;
					if (result != null) {
						token = result.getString(AccountManager.KEY_AUTHTOKEN,
								null);
					}
					listener.onGetAuthToken(token);
				} catch (OperationCanceledException e) {
					listener.onFailure(e);
				} catch (AuthenticatorException e) {
					listener.onFailure(new RuntimeException(
							"Fatal error : server has invalid auth information",
							e));
				} catch (IOException e) {
					listener.onFailure(new RuntimeException(
							"Auth token lockup failed", e));
				}
			}
		};

		if (mContext instanceof Activity) {
			mAccountManager.getAuthToken(mAccount, authTokenType, null,
					(Activity) mContext, callback, null);
		} else {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				mAccountManager.getAuthToken(mAccount, authTokenType, null,
						false, callback, null);
			} else {
				mAccountManager.getAuthToken(mAccount, authTokenType, false,
						callback, null);
			}
		}
	}

	/**
	 * Invalidate local token cache. Why android does not support invalidating
	 * one account?
	 */
	public void invalidateAuthToken() {
		mAccountManager.invalidateAuthToken(accountType(), tokenType());
	}
}
