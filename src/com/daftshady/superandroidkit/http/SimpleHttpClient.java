package com.daftshady.superandroidkit.http;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.ByteArrayEntity;
import org.json.JSONObject;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Simple static HTTP client using `loopj async-http-client`. HTTP request
 * stream is handled asynchronously.
 * 
 * @author parkilsu
 *
 */
public class SimpleHttpClient {

	private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();

	/**
	 * Send HTTP `GET` request to URL.
	 * 
	 * @param url
	 *            `URL` request is sent to.
	 * @param params
	 *            `Parameters` which goes with `GET` request.
	 * @param responseHandler
	 *            Async handler for HTTP response. If null, it will be replaced
	 *            with empty handler.
	 */
	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		AsyncHttpResponseHandler handler = responseHandler != null ? responseHandler
				: new AsyncHttpResponseHandler();
		asyncHttpClient.get(url, params, handler);
	}

	/**
	 * Send HTTP `POST` request to URL.
	 * 
	 * @param url
	 *            `URL` request is sent to.
	 * @param params
	 *            `Parameters` which goes with `POST` request.
	 * @param responseHandler
	 *            Async handler for HTTP response. If null, it will be replaced
	 *            with empty handler.
	 */
	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		AsyncHttpResponseHandler handler = responseHandler != null ? responseHandler
				: new AsyncHttpResponseHandler();
		asyncHttpClient.post(url, params, handler);
	}

	public static void post(Context context, String url, JSONObject params,
			SimpleHttpResponse responseHandler) throws UnsupportedEncodingException {
		AsyncHttpResponseHandler handler = responseHandler != null ? responseHandler
				: new AsyncHttpResponseHandler();
		ByteArrayEntity entity = new ByteArrayEntity(params.toString()
				.getBytes("UTF-8"));
		asyncHttpClient.post(context, url, entity, "application/json", handler);
	}
}