package com.daftshady.superandroidkit.authentication;
import com.daftshady.superandroidkit.exception.SuperAndroidKitException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Abstract base class for android account model.
 * This class wraps `android.accounts.Account` to easily manage authentication info.
 * Should inherit this, when implementing account model 
 * connected with android internal account system.
 * @author parkilsu
 *
 */
public abstract class AbstractAccount {
	
	private AccountManager mAccountManager;
	
	protected Account mAccount;
	
	protected AbstractAccount(Context context, String accountType) {
		mAccountManager = AccountManager.get(context);
		mAccount = AccountUtils.getAccount(context, accountType);
		if (mAccount == null){
			throw new SuperAndroidKitException("No available account");
		}
	}
	
	protected abstract String signIn(String userName, String userPass);
	
	protected abstract String accountType();
	
	protected abstract String tokenType();
	
	/**
	 * Get username.
	 * Basically, username is `email`
	 * @return userName
	 */
	public String getName() {
		return mAccount.name;
	}
	
	/**
	 * Get user password
	 * @return password
	 */
	public String getPassword() {
		return mAccountManager.getPassword(mAccount);
	}
	
	/**
	 * Get account type
	 * @return accountType
	 */
	public String getType() {
		return accountType();
	}
	
	/**
	 * Get auth token type
	 * tokenType should be retrieved from real class.
	 * @return authTokenType
	 */
	public String getAuthTokenType() {
		return tokenType();
	}
	
	/**
	 * Get current account manager
	 * @return acccountManager
	 */
	public AccountManager getManager() {
		return mAccountManager;
	}
	
	/**
	 * Get existing account
	 * @return account if there exists, else null
	 */
	public Account getAccount() {
		return mAccount;
	}
	
	/**
	 * Get auth token saved in account manager
	 * returns null if there is no cached auth token.
	 * @return authToken
	 */
	public String getAuthToken() {
		return getAuthToken(tokenType());
	}
	
	/**
	 * Get auth token by type
	 * returns null if there is no cached auth token.
	 * @param authTokenType
	 * @return authToken
	 */
	public String getAuthToken(String authTokenType) {
		String token = mAccountManager.peekAuthToken(mAccount, authTokenType);
		return token;
	}
	
	/**
	 * Set auth token into account manager
	 * @param authToken
	 */
	public void setAuthToken(String authToken) {
		mAccountManager.setAuthToken(mAccount, tokenType(), authToken);
	}
	
	/**
	 * Remove currently cached auth token
	 */
	public void removeAuthToken() {
		setAuthToken(null);
	}
	
	/**
	 * Check auth token validation
	 * @param context
	 * @return true if auth token is valid, else false
	 */
	public Boolean isAuthTokenValid(final Context context) {
		// TODO : Validation depends on type of auth token should be done here.
		String authToken = getAuthToken();
		String recentAuthToken = signIn(getName(), getPassword());
		return authToken == recentAuthToken;
	}
	
	/**
	 * Call validation method sequentially
	 * @param context
	 */
	public void validate(final Context context) {
		validateAuthToken(context);
	}
	
	/**
	 * Update auth token if current auth token is invalid.
	 * @param context
	 */
	public void validateAuthToken(final Context context) {
		if (isAuthTokenValid(context))
			return;
		updateToken();
	}
	
	/**
	 * Update auth token from new token by server
	 */
	public void updateToken() {
		String newAuthToken = signIn(getName(), getPassword());
		setAuthToken(newAuthToken);
	}
}
