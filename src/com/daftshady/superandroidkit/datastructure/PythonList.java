package com.daftshady.superandroidkit.datastructure;

/**
 * Provides python-style list form in string
 * @author parkilsu
 *
 */
public class PythonList {
	private final static String start = "[";
	
	private final static String end = "]";
	
	private final static String emptyList = start + end;
	
	private final static String itemSeparator = ",";
	
	private StringBuilder mList = new StringBuilder(emptyList);
	
	public void append(String item) {
		open();
		if (!isEmpty())
			mList.append(itemSeparator);
		mList.append(item);
		close();
	}
	
	public void append(PythonDict item) {
		append(item.toString());
	}
	
	@Override
	public String toString() {
		return mList.toString();
	}
	
	private void open() {
		mList.deleteCharAt(mList.lastIndexOf(end));
	}
	
	private void close() {
		mList.append(end);
	}
	
	private boolean isEmpty() {
		return start.equals(mList.toString());
	}
	
}
