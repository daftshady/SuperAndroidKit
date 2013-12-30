package com.daftshady.superandroidkit.http;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.daftshady.superandroidkit.exception.SuperAndroidKitException;
import com.loopj.android.http.AsyncHttpResponseHandler;

/**
 * Async response handler for HTTP request.
 * @author parkilsu
 *
 */
public abstract class SimpleHttpResponse extends AsyncHttpResponseHandler {
	@Override
	public abstract void onSuccess(int statusCode, Header[] headers, byte[] responseBody);
	
	public JSONObject toJson(byte[] responseBody) {
		try {
			return new JSONObject(
					new String(responseBody, 0, responseBody.length)
					);
		} catch (JSONException e) {
			throw new SuperAndroidKitException(
					"Failed to convert responseBody to JSon", e);
		}
	}
}
