package com.daftshady.superandroidkit.location;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;

/**
 * Provides location related methods.
 * @author parkilsu
 *
 */
public class LocationFetcher {

	private static LocationManager mLocationManager;

	private static Location mCurrentLocation;

	private static SsLocationListener mLocationListener = new SsLocationListener();

	/**
	 * LocationManager should be singleton object.
	 * @param context
	 * @return LocationManager
	 */
	private static LocationManager getLocationManager(Context context) {
		if (mLocationManager == null) {
			synchronized (LocationFetcher.class) {
				if (mLocationManager == null) {
					mLocationManager = (LocationManager) context
							.getSystemService(Context.LOCATION_SERVICE);
				}
			}
		}
		return mLocationManager;
	}
	
	/**
	 * Request continuous location updates with private listener MVLocationListener.
	 * @param context
	 * @param provider The name of the provider with which to register
	 */
	private static void requestLocationUpdates(Context context, String provider) {
		requestLocationUpdates(context, provider, mLocationListener);
	}
	
	/**
	 * Request continuous location updates with specific listener.
	 * This update cannot stop until `removeUpdates` method is called. 
	 * @param context
	 * @param provider The name of the provider with which to register
	 * @param listener LocationListener
	 */
	private static void requestLocationUpdates(
			Context context, String provider, LocationListener listener) {
		long minTime = 0;
		float minDistance = 0;
		LocationManager manager = getLocationManager(context);
		manager.requestLocationUpdates(provider,
				minTime, minDistance, listener);	
	}
	
	/**
	 * This method starts background location updates 
	 * with private listener MVLocationListener.
	 * Location updates continuously change `mCurrentLocation`
	 * so that when developer calls `getCurrentLocation`, 
	 * it will return recent location of user.
	 * (Determined by `isBetterLocation` function) 
	 * @param context
	 */
	public static void startLocationUpdates(Context context) {
		updateLocation(context, mLocationListener, false);
	}
	
	/**
	 * 
	 * This method starts background location updates 
	 * with specific LocationListener.
	 * @param context
	 * @param listener
	 */
	public static void startLocationUpdates(
			Context context, LocationListener listener) {
		updateLocation(context, listener, false);
	}
	
	/**
	 * Stops background location updates.
	 * @param context
	 * @param listener 
	 */
	public static void stopLocationUpdates(
			Context context, LocationListener listener) {
		LocationManager manager = getLocationManager(context);
		manager.removeUpdates(listener);
	}
	
	/**
	 * Get current location with mLocationListener.
	 * `mCurrentLocation` is singleton object.
	 * Current location can be null if both GPS, network devices are 
	 * not ready and there no cached location data. 
	 * @param context
	 * @return Location
	 */
	public static Location getCurrentLocation(Context context) {
		return getCurrentLocation(context, mLocationListener, false);
	}

	/**
	 * Get current location with specific listener.
	 * If there is location change, `onLocationChanged`
	 * method of Listener will be called.
	 * Current location can be null if both GPS, network devices are 
	 * not ready and there no cached location data.
	 * @param context
	 * @param listener
	 * @param forceUpdate
	 * If forceUpdate is true, device once get recent location from provider.
	 * @return Location
	 */
	public static Location getCurrentLocation(
			Context context, LocationListener listener, Boolean forceUpdate) {
		if (mCurrentLocation == null || forceUpdate)
			updateLocation(context, listener, true);
		
		return mCurrentLocation;
	}

	/**
	 * Update `mCurrentLocation`.
	 * If developer use custom listener when calling this method,
	 * `onLocationChanged` method in that listener must extends
	 * MVLocationListener and call super.onLocationChanged(location)
	 * to update singleton `mCurrentLocation`.
	 * @param context
	 * @param listener
	 * @param updateOnce
	 */
	private static void updateLocation(
			Context context, LocationListener listener, Boolean updateOnce) {
		String provider = getBestProvider(context);
		
		if (provider == null) {
			// There is no available location source.
			// TODO : Throw exception
			//throw new LocationException("Cannot find location provider");
		}
		
		LocationManager manager = getLocationManager(context);
		Location location = manager.getLastKnownLocation(provider);
		if (location != null) 
			mCurrentLocation = location;
		
		stopLocationUpdates(context, listener);
		if (updateOnce) {
			// TODO : Do update once here.
		} else {
			requestLocationUpdates(context, provider, listener);
		}
	}

	/**
	 * Choose best location provider by the criteria options.
	 * @param context
	 * @return Provider(String)
	 */
	private static String getBestProvider(Context context) {
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);
		return getLocationManager(context).getBestProvider(criteria, true);
	}
	
	/**
	 * Determine between currently cached location and newly acquired location,
	 * which is accurate location.
	 * This method is taken from 
	 * 'http://developer.android.com/intl/ko/guide/topics/location/strategies.html'
	 * (Google location sample 'Maintaining a current best estimate')
	 * and somewhat modified.
	 * @param location
	 * @return true if newly acquired location is better than mCurrentLocation else false.
	 */
	private static Boolean isBetterThanCurrentLocation(Location location) {
		if (location == null)
			throw new IllegalArgumentException("Location should be provided");
		
		if (mCurrentLocation == null)
			return true;
		
		int timeCriteria = 1000 * 60 * 1;
		long timeDelta = location.getTime() - mCurrentLocation.getTime();
		boolean isNewer = timeDelta > 0;
		
		if (timeDelta > timeCriteria) {
			return true;
		} else if (timeDelta < -timeCriteria) {
			return false;
		}
		
		int accuracyDelta = 
				(int) (location.getAccuracy() - mCurrentLocation.getAccuracy());
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isFuckingLessAccurate = accuracyDelta > 200;
		boolean isFromSameProvider = TextUtils.
				equals(location.getProvider(), mCurrentLocation.getProvider());
		
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isFuckingLessAccurate && isFromSameProvider) {
			return true;
		}
		return false;
	}
	
	/**
	 * CAUTION : All Listener used in this MVLocationManager 
	 * should be 'MVLocationListener' or 
	 * must extends `MVLocationListener` and call super.onLocationChanged
	 * in overrided `onLocationChanged` method to update singleton `mCurrrentLocation`
	 * 
	 * @author park-ilsu
	 *
	 */
	public static class SsLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (isBetterThanCurrentLocation(location))
				mCurrentLocation = location;
			
			if (mCurrentLocation == null)
				throw new IllegalArgumentException("Cannot get location from existing device");
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO : implement it
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO : implement it
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO : implement it
		}

	}

}