package com.daftshady.superandroidkit.database;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.database.Cursor;

import com.daftshady.superandroidkit.utils.DateUtils;
import com.daftshady.superandroidkit.utils.StringUtils;

public class BaseCursorManager {

	private Cursor mCursor;

	public BaseCursorManager(Cursor cursor) {
		mCursor = cursor;
		if (mCursor == null)
			throw new IllegalArgumentException("Should provide valid cursor");
	}

	protected <T> T createModel(Class<T> klass) {
		T model = null;

		try {
			model = klass.newInstance();
		} catch (Exception e) {
			throw new IllegalArgumentException(
					"DbModel doesn't have default constructor");
		}

		try {
			DbColumn[] columns = (DbColumn[]) klass.getMethod("getColumns",
					null).invoke(model, null);
			for (DbColumn column : columns) {
				String columnName = StringUtils.toLowerCamelCase(column.getName());
				Field field = klass.getDeclaredField(columnName);
				field.setAccessible(true);
				field.set(model, field.getType().cast(getValue(column)));
			}
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Cannot access to method!");
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(
					"Method param type does not match!");
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(
					"Model must be extended from BaseDbModel.");
		} catch (NoSuchFieldException e) {
			throw new IllegalArgumentException("Model has undeclared field.");
		}

		return model;
	}

	/*
	 * Can be shortened with reflection.
	 */
	private Object getValue(DbColumn column) {
		String name = column.getName();
		Object value = null;
		int columnIndex = mCursor.getColumnIndex(name);
		switch (column.getType()) {
		case INTEGER:
			value = mCursor.getInt(columnIndex);
			break;
		case BLOB:
			value = mCursor.getBlob(columnIndex);
			break;
		case STRING:
			value = mCursor.getString(columnIndex);
			break;
		case DOUBLE:
			value = mCursor.getDouble(columnIndex);
			break;
		case FLOAT:
			value = mCursor.getFloat(columnIndex);
			break;
		case LONG:
			value = mCursor.getLong(columnIndex);
			break;
		}
		return value;
	}

	public <T> List<T> retreiveData(Class<T> klass) {
		List<T> dataList = new ArrayList<T>();

		mCursor.moveToFirst();
		if (mCursor.getCount() > 0) {
			do {
				dataList.add(createModel(klass));
			} while (mCursor.moveToNext());
			mCursor.moveToFirst();
		}
		return dataList;
	}

	@SuppressLint("SimpleDateFormat")
	private Date getDateByName(String columnName) throws ParseException {
		String dateString = mCursor.getString(mCursor
				.getColumnIndex(columnName));
		SimpleDateFormat transFormat = new SimpleDateFormat(
				DateUtils.DATE_FORMAT);
		return transFormat.parse(dateString);
	}
}
