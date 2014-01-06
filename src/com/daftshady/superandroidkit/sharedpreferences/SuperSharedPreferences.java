package com.daftshady.superandroidkit.sharedpreferences;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Wraps `android.content.SharedPreferences` for fastest use of
 * `SharedPreferences`.
 * @author parkilsu
 *
 */
public class SuperSharedPreferences {
	
	private SharedPreferences mPreferences;
	
	public SuperSharedPreferences(Activity activity, String fileName) {
		mPreferences = activity.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}
	
	/**
	 * Get matched value for key.
	 * @param key
	 * @return
	 * 		`String` value
	 */
	public String get(String key) {
		return mPreferences.getString(key, null);
	}
	
	/**
	 * Set (key, value) pair to `SharedPreference`
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean set(String key, String value) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.putString(key, value);
		return editor.commit();
	}
	
	/**
	 * Remove matched value for `key`
	 * @param key
	 * @return
	 * 		Returns true if removing is success, else false.
	 */
	public boolean remove(String key) {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.remove(key);
		return editor.commit();
	}
	
	/**
	 * Remove all data from `SharedPreference`
	 * @return
	 * 		Returns true if removing is success, else false.
	 */
	public boolean flush() {
		SharedPreferences.Editor editor = mPreferences.edit();
		editor.clear();
		return editor.commit();
	}
}
