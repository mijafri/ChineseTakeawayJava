package com.jafritech.chinesetakeawayjava;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.ContextThemeWrapper;


/**
 * Gps location tracker class to get users location and other information
 * related to location
 */
public class GpsLocationTracker extends Service implements LocationListener {

    /**
     * context of calling class
     */
    private final Context mContext;

    /**
     * flag for gps
     */
    private boolean canGetLocation = false;

    /**
     * location
     */
    private Location mLocation;

    /**
     * latitude
     */
    private double mLatitude;

    /**
     * longitude
     */
    private double mLongitude;

    /**
     * min distance change to get location update
     */
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATE = 10;

    /**
     * min time for location update 60000 = 1min
     */
    private static final long MIN_TIME_FOR_UPDATE = 60000;

    /**
     * @param mContext
     *            constructor of the class
     */
    public GpsLocationTracker(Context mContext) {

        this.mContext = mContext;
        getLocation();
    }

    /**
     */
    @SuppressLint("MissingPermission")
    public void getLocation() {

        try {


             //location manager

            LocationManager mLocationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);

            // getting status of the gps 
            // flag for gps status
             
            boolean isGpsEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting status of network provider 
            //  flag for network status
             
            boolean isNetworkEnabled = mLocationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (!isGpsEnabled && !isNetworkEnabled) {
                
                Log.i("LocationTag", "Location data not available");
            
            } else {

                this.canGetLocation = true;

            // getting location from network provider 
                if (isNetworkEnabled) {
                    mLocationManager.requestLocationUpdates(
                            LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_FOR_UPDATE,
                            MIN_DISTANCE_CHANGE_FOR_UPDATE, this);
                    if (mLocationManager != null) {

                        mLocation = mLocationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

                        if (mLocation != null) {

                            mLatitude = mLocation.getLatitude();

                            mLongitude = mLocation.getLongitude();
                        }
                    }
                    // if gps is enabled then get location using gps
                    if (isGpsEnabled) {

                        if (mLocation == null) {

                            assert mLocationManager != null;
                            mLocationManager.requestLocationUpdates(
                                    LocationManager.GPS_PROVIDER,
                                    MIN_TIME_FOR_UPDATE,
                                    MIN_DISTANCE_CHANGE_FOR_UPDATE, this);

                            if (mLocationManager != null) {

                                mLocation = mLocationManager
                                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);

                                if (mLocation != null) {

                                    mLatitude = mLocation.getLatitude();

                                    mLongitude = mLocation.getLongitude();
                                }

                            }
                        }

                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    
     // call this function to stop using gps in your application
     
//    public void stopUsingGps() {
//
//        if (mLocationManager != null) {
//
//            try {
//                mLocationManager.removeUpdates(GpsLocationTracker.this);
//            } catch (Throwable e) {
//                e.printStackTrace();
//            }
//        }
//    }

    
     // @return latitude <p/> function to get latitude
     
    public double getLatitude() {

        if (mLocation != null) {

            mLatitude = mLocation.getLatitude();
        }
        return mLatitude;
    }

    // @return longitude function to get longitude
     
    public double getLongitude() {

        if (mLocation != null) {

            mLongitude = mLocation.getLongitude();
        }

        return mLongitude;
    }
    
     // @return to check gps or wifi is enabled or not
     
    public boolean canGetLocation() {

        return this.canGetLocation;
    }

    
     // function to prompt user to open settings to enable gps
    
    public void showSettingsAlert() {

        AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(
                new ContextThemeWrapper(mContext, R.style.Theme_AppCompat));

        mAlertDialog.setTitle("Gps Disabled"); // cant set string resources here

        mAlertDialog.setMessage("gps is not enabled . do you want to enable ?"); // cant set string resources here

        mAlertDialog.setPositiveButton("settings", (dialog, which) -> {
            Intent mIntent = new Intent(
                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            mContext.startActivity(mIntent);
        });

        mAlertDialog.setNegativeButton("cancel", (dialog, which) -> dialog.cancel());

        final AlertDialog createDialog = mAlertDialog.create();
        createDialog.show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    public void onLocationChanged(Location location) {

    }

    public void onProviderDisabled(String provider) {

    }

    public void onProviderEnabled(String provider) {

    }

    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

}