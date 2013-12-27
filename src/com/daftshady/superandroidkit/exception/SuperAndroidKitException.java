package com.daftshady.superandroidkit.exception;

/**
 * Provides an error condition.
 * @author parkilsu
 *
 */
public class SuperAndroidKitException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructs a new SuperAndroidKitException.
	 */
	public SuperAndroidKitException() {
		super();
	}
	
	/**
	 * Constructs a new SuperAndroidKitException.
	 * @param message
	 * 		Detail message of the exception.
	 */
	public SuperAndroidKitException(String message) {
		super(message);
	}
	
	/**
	 * Constructs a new SuperAndroidKitException.
	 * @param throwable
	 * 		Cause of the exception.
	 */
	public SuperAndroidKitException(Throwable throwable) {
		super(throwable);
	}

	/**
	 * Constructs a new SuperAndroidKitException
	 * @param message
	 * 		Detail message of the exception.
	 * @param throwable
	 * 		Cause of the exception.
	 */
	public SuperAndroidKitException(String message, Throwable throwable) {
		super(message, throwable);
	}
}