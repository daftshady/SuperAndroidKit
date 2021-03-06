/**
 * Copyright 2013 MVERSE <dev@mverse.co.kr>
 * 
 * Unauthorized copying of this file, via any medium is strictly prohibited
 * All of the codes and comments are strictly proprietary and confidential
 * 
 * Written by Great Minds of @MVERSE inc
 * at Jun 29, 2013
 */

package com.daftshady.superandroidkit.authentication;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.accounts.AccountsException;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

public class AccountUtils {

	private static final String TAG = "SuperAndroidKitAccountUtils";

	private static class AccountException extends IOException {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * @param context
	 * @return true if there exists any token type of account having specific
	 *         accountType, else false.
	 */
	public static Boolean isThereAccount(final Context context,
			String accountType) {
		return getAccount(context, accountType) != null ? true : false;
	}

	/**
	 * @param context
	 * @param accountType
	 * @param tokenType
	 * @return true if there exists account having specific token type and
	 *         account type.
	 */
	public static Boolean isThereAccount(final Context context,
			String accountType, String tokenType) {
		return getAccount(context, accountType, tokenType) != null ? true
				: false;
	}

	/**
	 * Get account from accountManager. If there exists multiple account, this
	 * method takes recently logined account. Returns null if there is any
	 * existing account.
	 * 
	 * @param context
	 * @param accountType
	 * @return Account
	 */
	public static Account getAccount(final Context context, String accountType) {
		final Account[] accounts = getAccounts(context, accountType);
		if (accounts.length == 0)
			return null;

		Integer idx = 0;
		Integer max = 0;
		Integer cnt = 0;
		AccountManager manager = AccountManager.get(context);
		for (Account account : accounts) {
			String seqId = manager.getUserData(account,
					AccountConstants.ACCOUNT_SEQ_ID);
			if (seqId == null) {
				manager.removeAccount(account, null, null);
				return null;
			}

			if (Integer.parseInt(seqId) > max) {
				max = Integer.parseInt(seqId);
				idx = cnt;
			}
			cnt++;
		}

		return accounts[idx];
	}

	/**
	 * Get account from accountManager. Returns null if there is no account
	 * having that accountType and tokenType.
	 * 
	 * @param context
	 * @param accountType
	 * @param tokenType
	 * @return Account
	 */
	public static Account getAccount(final Context context, String accountType,
			String tokenType) {
		AccountManager manager = AccountManager.get(context);
		for (Account account : getAccounts(context, accountType)) {
			String token = manager.peekAuthToken(account, tokenType);
			if (!TextUtils.isEmpty(token))
				return account;
		}
		return null;
	}

	/**
	 * Get all registered accounts from system
	 * 
	 * @param context
	 * @return array of accounts
	 */
	public static Account[] getAccounts(final Context context,
			String accountType) {
		return AccountManager.get(context).getAccountsByType(accountType);
	}

	/**
	 * Get exiting account or generate account with showing AuthUI. This method
	 * must not be used on the main thread, because creating new account can
	 * block thread
	 * 
	 * @param context
	 * @return account if there exists, else create account.
	 */
	public static Account getOrCreateAccount(final Context context,
			Activity activity, String accountType, String tokenType) {
		if (getAccounts(context, accountType).length == 0) {
			AccountManagerFuture<Bundle> future = AccountManager.get(context)
					.addAccount(accountType, tokenType, null, null, activity,
							null, null);
			try {
				future.getResult();
			} catch (OperationCanceledException e) {
				e.printStackTrace();
				return null;
			} catch (AuthenticatorException e) {
				e.printStackTrace();
				return null;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		}
		return getAccount(context, accountType);
	}

	/**
	 * Get number of account in android accountManager.
	 * 
	 * @param context
	 * @param accountType
	 * @return Number of accounts
	 */
	public static Integer getNumberOfAccounts(final Context context,
			String accountType) {
		return getAccounts(context, accountType).length;
	}

	/**
	 * This method should called when adding account to android. Because every
	 * account has its sequence id.
	 * 
	 * @param context
	 * @param accountType
	 * @return
	 */
	public static String getNextAccountSeqId(final Context context,
			String accountType) {
		Integer max = 0;

		for (Account account : getAccounts(context, accountType)) {
			String seqId = AccountManager.get(context).getUserData(account,
					AccountConstants.ACCOUNT_SEQ_ID);
			if (seqId == null)
				continue;

			if (Integer.parseInt(seqId) > max)
				max = Integer.parseInt(seqId);
		}
		return (++max).toString();
	}

	/**
	 * Remove first account from accountManager.
	 * 
	 * @param context
	 */
	public static void removeCurrentAccount(final Context context,
			String accountType) {
		if (isThereAccount(context, accountType))
			AccountManager.get(context).removeAccount(
					getAccount(context, accountType), null, null);
	}

	/**
	 * Remove all account having specific accountType.
	 * 
	 * @param context
	 * @param accountType
	 */
	public static void removeAllAccount(final Context context,
			String accountType) {
		AccountManager manager = AccountManager.get(context);
		if (isThereAccount(context, accountType)) {
			for (Account account : getAccounts(context, accountType)) {
				manager.removeAccount(account, null, null);
			}
		}
	}

}
