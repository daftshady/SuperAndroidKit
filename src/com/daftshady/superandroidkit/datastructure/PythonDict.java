package com.daftshady.superandroidkit.datastructure;

/**
 * Provides python-style dictionary form in string
 * @author parkilsu
 *
 */
public class PythonDict {
	/*
	 * Only goes with the basic interface and shape of python dict.
	 * There is no strict error handling on this Dict.
	 * Because it's only used to communicate with python-based server easily.
	 */
	
	private final static String start = "{";
	
	private final static String end = "}";
	
	private final static String emptyDict = start + end;
	
	private final static String itemSeparator = ",";
	
	private final static String keySeparator = ":";
	
	private StringBuilder mDict = new StringBuilder(emptyDict);
	
	public PythonDict() {
		this(emptyDict);
	}
	
	public PythonDict(String initDict) {
		update(initDict);
	}
	
	public void set(String k, String v) {
		if (k == null)
			new IllegalArgumentException("Cannot set on null key in MVDict");
		v = v != null ? v : "";
		
		open();
		if (!isEmpty())
			mDict.append(itemSeparator);
		mDict.append(pack(k));
		mDict.append(keySeparator);
		String dictValue = isDict(v) ? v : pack(v);
		mDict.append(dictValue);
		close();
	}
	
	@Override
	public String toString() {
		return mDict.toString();
	}
	
	private void get(String k) {
		// TODO: implement it
	}
	
	private void update(String dict) {
		// TODO: implement it
	}
	
	private void open() {
		mDict.deleteCharAt(mDict.lastIndexOf(end));
	}
	
	private void close() {
		mDict.append(end);
	}
	
	private String pack(String input) {
		return "'" + input + "'";
	}
	
	private boolean isDict(String v) {
		return v.contains(start) && v.contains(end);
	}
	
	private boolean isEmpty() {
		return start.equals(mDict.toString());
	}
}
