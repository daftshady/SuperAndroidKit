package com.daftshady.superandroidkit.database;

import java.util.List;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * DbManager
 * Provides easy interfaces to database actions and
 * wraps extra exceptions.
 * @author parkilsu
 *
 */
public class DbManager {
	
	public static final int FLAG_OPEN_WRITABLE_DATABASE = 1; 
	public static final int FLAG_OPEN_READABLE_DATABASE = 2;

	private final String TAG = "DbManager";
	
	private SQLiteDatabase mDatabase;
	private SQLiteOpenHelper mDatabaseHelper;
	
	private String defaultNullColumnHack = null;

	public DbManager(SQLiteOpenHelper helper){
		mDatabaseHelper = helper;
		if (mDatabaseHelper == null)
			throw new IllegalArgumentException("Helper cannot be null");
	}
	
	public SQLiteDatabase open(int flag) {
		switch(flag){
			case FLAG_OPEN_READABLE_DATABASE:
				mDatabase = mDatabaseHelper.getReadableDatabase();
				break;
			case FLAG_OPEN_WRITABLE_DATABASE:
				mDatabase = mDatabaseHelper.getWritableDatabase();
				break;
			default:
				throw new IllegalArgumentException("Flag argument error");
		}
		return mDatabase;
	}
	
	public void close() {
		if(mDatabase == null)
			throw new IllegalArgumentException("Database not opened");
		mDatabase.close();
	}
	
	public Cursor select(
			String table,String[] columns, String limit) {
		return select(table, columns, null, null, null, null, null, limit);
	}
	
	public Cursor select(
			String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy, String limit) {

		if(mDatabase == null)
			throw new IllegalArgumentException("Database not opened");
		
		try {
			return mDatabase.query(
						table, columns, selection, 
						selectionArgs, groupBy, having, orderBy, limit
						);
		} catch (SQLException e) {
			Log.e(TAG, "Selection failed : " + e.getStackTrace(), e);
			return null;
		}
	}
	public boolean insert(String tableName, ContentValues data) {
		boolean success = false;
		if(mDatabase == null)
			throw new IllegalArgumentException("Database not opened");

		try {
			mDatabase.beginTransaction();
			mDatabase.insertOrThrow(tableName, defaultNullColumnHack, data);
			mDatabase.setTransactionSuccessful();
			success = true;
		} catch (SQLException e) {
			Log.e(TAG, "Insertion failed : " + e.getStackTrace(), e);
		} finally{
			mDatabase.endTransaction();
		}
		return success;
	}
	
	public boolean insert(String tableName, List<ContentValues> data) {
		boolean success = false;
		if(mDatabase == null)
			throw new IllegalArgumentException("Database not opened");

		try {
			mDatabase.beginTransaction();
			for(ContentValues value:data){
				mDatabase.insertOrThrow(tableName, defaultNullColumnHack, value);
			}
			mDatabase.setTransactionSuccessful();
			success = true;
		} catch (SQLException e) {
			Log.e(TAG, "Insertion failed : " + e.getStackTrace(), e);
		} finally{
			mDatabase.endTransaction();
		}
		return success;
	}
	
	public boolean update(String tableName, ContentValues data) {
		if(mDatabase == null)
			throw new IllegalArgumentException("Database not opened");
		
		return update(tableName, data, null, null, false);
	}
	
	public boolean update(
			String tableName, ContentValues data, 
			String whereClause, String[] whereArgs, boolean forceUpdate) {
		boolean success = false;
		try {
			mDatabase.beginTransaction();
			int numOfUpdates = 
					mDatabase.update(tableName, data, whereClause, whereArgs);
			
			if (forceUpdate && numOfUpdates == 0)
				throw new SQLException("Nothing updated");
			else{
				success = true;
				mDatabase.setTransactionSuccessful();
			}
		} catch (SQLException e) {
			Log.e(TAG, "Update failed : " + e.getStackTrace());
		} finally{
			mDatabase.endTransaction();
		}
		return success;
	}
	public int delete(String tableName, String whereClause, String[] whereArgs){
		int deleteRowCount = -1;
		if(mDatabase == null)
			throw new IllegalArgumentException("Database not opened");
		
		try {
			mDatabase.beginTransaction();
			deleteRowCount = mDatabase.delete(tableName, whereClause, whereArgs);
			mDatabase.setTransactionSuccessful();
		} catch (SQLException e) {
			Log.e(TAG, "Selection failed : " + e.getStackTrace(), e);
		} finally{
			mDatabase.endTransaction();
		}
		return deleteRowCount;
	}
}
