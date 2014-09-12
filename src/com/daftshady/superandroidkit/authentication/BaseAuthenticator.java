package com.daftshady.superandroidkit.authentication;

import static android.accounts.AccountManager.KEY_ACCOUNT_AUTHENTICATOR_RESPONSE;
import static android.accounts.AccountManager.KEY_ACCOUNT_NAME;
import static android.accounts.AccountManager.KEY_ACCOUNT_TYPE;
import static android.accounts.AccountManager.KEY_AUTHTOKEN;
import static android.accounts.AccountManager.KEY_INTENT;
import android.accounts.AbstractAccountAuthenticator;
import android.accounts.Account;
import android.accounts.AccountAuthenticatorResponse;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Should inherit this to make authenticator class dealing with android account
 * commands reachable by users.
 * 
 * @author parkilsu
 *
 */
public abstract class BaseAuthenticator extends AbstractAccountAuthenticator {

	protected Context mContext;

	protected AccountManager mManager;

	protected AbstractAccount mAccount;

	protected String tokenType;

	protected BaseAuthenticator(Context context) {
		super(context);
		mManager = AccountManager.get(context);
		mContext = context;
	}

	/**
	 * This method should be implemented. Real class can inject activity into
	 * `BaseAuthenticator` by this method.
	 * 
	 * @param response
	 * @return intent
	 */
	protected abstract Intent createActivityIntent(
			AccountAuthenticatorResponse response);

	/**
	 * This method should be implemented. Interaction with auth-server should be
	 * wrapped by this method.
	 * 
	 * @return authKey
	 */
	protected abstract String signIn(String userName, String userPass);

	@Override
	public Bundle addAccount(AccountAuthenticatorResponse response,
			String accountType, String authTokenType,
			String[] requiredFeatures, Bundle options)
			throws NetworkErrorException {
		final Intent intent = createActivityIntent(response);
		intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public Bundle confirmCredentials(AccountAuthenticatorResponse arg0,
			Account arg1, Bundle arg2) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle editProperties(AccountAuthenticatorResponse arg0, String arg1) {
		return null;
	}

	@Override
	public Bundle getAuthToken(AccountAuthenticatorResponse response,
			Account account, String authTokenType, Bundle options)
			throws NetworkErrorException {
		String authToken = mManager.peekAuthToken(account, authTokenType);
		if (TextUtils.isEmpty(authToken)) {
			String userName = account.name;
			String userPass = mManager.getPassword(account);
			if (userPass != null) {
				authToken = signIn(userName, userPass);
			}
		}

		if (TextUtils.isEmpty(authToken)) {
			return loginRetry(response, account);
		}

		mManager.setAuthToken(account, authTokenType, authToken);
		final Bundle result = new Bundle();
		result.putString(KEY_ACCOUNT_NAME, mAccount.getName());
		result.putString(KEY_ACCOUNT_TYPE, mAccount.getType());
		result.putString(KEY_AUTHTOKEN, mAccount.getAuthToken());
		return result;
	}

	/**
	 * If user have failed to sign in, re-try is needed. This method creates
	 * intent of login activity for user to re-try login.
	 * 
	 * @param response
	 * @param account
	 * @return login information
	 */
	private Bundle loginRetry(AccountAuthenticatorResponse response,
			Account account) {
		final Intent intent = createActivityIntent(response);
		intent.putExtra(KEY_ACCOUNT_AUTHENTICATOR_RESPONSE, response);
		intent.putExtra(AccountConstants.PARAM_USERNAME, account.name);
		final Bundle bundle = new Bundle();
		bundle.putParcelable(KEY_INTENT, intent);
		return bundle;
	}

	@Override
	public String getAuthTokenLabel(String arg0) {
		return null;
	}

	@Override
	public Bundle hasFeatures(AccountAuthenticatorResponse arg0, Account arg1,
			String[] arg2) throws NetworkErrorException {
		return null;
	}

	@Override
	public Bundle updateCredentials(AccountAuthenticatorResponse arg0,
			Account arg1, String arg2, Bundle arg3)
			throws NetworkErrorException {
		return null;
	}

}
