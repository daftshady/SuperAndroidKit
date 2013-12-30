package com.daftshady.superandroidkit.http;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/**
 * Simple static HTTP client using `loopj async-http-client`.
 * HTTP request stream is handled asynchronously.
 * @author parkilsu
 *
 */
public class SimpleHttpClient {
	
	private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
	
	/**
	 * Send HTTP `GET` request to URL.
	 * @param url
	 * 		`URL` request is sent to.
	 * @param params
	 * 		`Parameters` which goes with `GET` request.
	 * @param responseHandler
	 * 		Async handler for HTTP response.	
	 * 		If null, it will be replaced with empty handler.
	 */
	public static void get(
			String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpResponseHandler handler = 
				responseHandler != null ? 
						responseHandler : new AsyncHttpResponseHandler();
		asyncHttpClient.get(url, params, handler);
	}
	
	/**
	 * Send HTTP `POST` request to URL.
	 * @param url
	 * 		`URL` request is sent to.
	 * @param params
	 * 		`Parameters` which goes with `POST` request.
	 * @param responseHandler
	 * 		Async handler for HTTP response.	
	 * 		If null, it will be replaced with empty handler.
	 */
	public static void post(
			String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
		AsyncHttpResponseHandler handler = 
				responseHandler != null ? 
						responseHandler : new AsyncHttpResponseHandler();
		asyncHttpClient.post(url, params, handler);
	}
}