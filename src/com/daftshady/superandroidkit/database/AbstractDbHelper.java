package com.daftshady.superandroidkit.database;

import java.util.List;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * AbstractDbHelper
 * Application should extends this helper to use database.
 * @author parkilsu
 *
 */
public abstract class AbstractDbHelper extends SQLiteOpenHelper {
	
	private final String TAG = "AbstractDbHelper";
	
	private final String mDatabaseName;
	
	public AbstractDbHelper(Context context, String dbName) {
		this(context, dbName, 1);
	}
	
	public AbstractDbHelper(Context context, String dbName, int versionNumber){
		super(context, dbName, null, versionNumber);
		mDatabaseName = dbName;
	}

	/*
	 * Should return list of table creation query.
	 */
	protected abstract List<String> getTableCreationQuerys();
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.d(TAG, "Creating tables");
		boolean success = false;
		try {
			for (String creationQuery : getTableCreationQuerys()) {
					db.execSQL(creationQuery);
			}
			success = true;
		} catch (SQLException e) {
				Log.e(TAG, "Table creation failed : " + e.getMessage(), e);
		}
		if (success)
			Log.d(TAG, "Table creation completed successfully");
	}

	/**
	 * Database versioning is not supported in AbstractDbHelper
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
	
	public String getDatabaseName(){
		return mDatabaseName;
	}

}
