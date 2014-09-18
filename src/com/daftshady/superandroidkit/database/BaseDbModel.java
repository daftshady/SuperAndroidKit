package com.daftshady.superandroidkit.database;

public abstract class BaseDbModel {

	public static final String DB_DATE_FORMAT = "yyyy-MM-DD HH:mm:ss";
	public abstract DbColumn[] getColumns();
}