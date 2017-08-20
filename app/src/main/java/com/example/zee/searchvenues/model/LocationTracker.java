package com.example.zee.searchvenues.model;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by zee on 8/10/2017.
 */

public class LocationTracker extends Service implements LocationListener
{

            private final Context con;
            //flag for gps status
            boolean isGpsOn=false;

            boolean isLocationEnabled=false;
            //flag for network enabled
            boolean isNetWorkEnabled=false;
            //The minimum distance to request updates
            private static final long MIN_DISTANCE_TO_REQUEST_LOCATION=1;

            //The minimum time between updates
            private static final long MIN_TIME_FOR_UPDATES=1000*1;

            private Location location; //location

            private double latitude; // latitude
            private double longitude; //longitude
            // Declaring a location Manager
            private LocationManager locationManager;

            private boolean canGetLocation=false;

            public LocationTracker(Context context)
            {
                this.con=context;
                checkIfLocationAvailable();

            }
            public Location checkIfLocationAvailable()
            {
                try
                {
                    locationManager=(LocationManager)
                            con.getSystemService(LOCATION_SERVICE);

                    //getting gps status
                    isGpsOn=locationManager
                            .isProviderEnabled(LocationManager.GPS_PROVIDER);

                    //getting network status
                    isNetWorkEnabled=locationManager
                            .isProviderEnabled(LocationManager.NETWORK_PROVIDER);


                    if(!isGpsOn && !isNetWorkEnabled)
                    {
                       this.canGetLocation=false;
                        Toast.makeText(con,"No Location is Available", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        this.canGetLocation=true;

                        //get location from netowrk provider
                        if(isNetWorkEnabled)
                        {
                            locationManager.requestLocationUpdates
                                    (LocationManager.NETWORK_PROVIDER,MIN_TIME_FOR_UPDATES,
                                            MIN_DISTANCE_TO_REQUEST_LOCATION,this);
                            if(locationManager!=null)
                            {
                                location=locationManager.
                                        getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                                if(location!=null)
                                {
                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();


                                }

                            }

                        }
                        //if gps on get lat/long using GPS services
                        if(isGpsOn)
                        {
                            locationManager.requestLocationUpdates
                                    (LocationManager.GPS_PROVIDER,MIN_TIME_FOR_UPDATES,
                                            MIN_DISTANCE_TO_REQUEST_LOCATION,this);
                            Log.d("GPS ENABLED", "GPS ENABLED");
                            if(locationManager!=null)
                            {
                                location=locationManager.getLastKnownLocation
                                        (LocationManager.GPS_PROVIDER);
                                if(location!=null)
                                {
                                    latitude=location.getLatitude();
                                    longitude=location.getLongitude();


                                }


                            }


                        }

                    }

                }catch (SecurityException e)
                {


                }

                return location;


            }
    public  void stopUsingLocation()
    {
        if(locationManager!=null)
        {
            locationManager.removeUpdates(LocationTracker.this);

        }


    }

    //Function to get latitude
    public double getLatitude()
    {
        if(location!=null)
        {
            latitude=location.getLatitude();


        }
        return  latitude;
    }


    //Function to get longitude
    public double getLongitude()
    {
        if(location!=null)
        {
            longitude=location.getLongitude();

        }
            return longitude;
    }
    public boolean isLocationEnabled()
    {
        return this.isLocationEnabled;

    }

    public void askToOnLocation()
    {
        AlertDialog.Builder dialog=new AlertDialog.Builder(con);
        dialog.setTitle("Settings");
        dialog.setMessage("Location is not Enabled, Do you want to got to settings to enable it?");
        dialog.setPositiveButton("Settings", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent=new Intent(Settings.ACTION_LOCALE_SETTINGS);
                con.startActivity(intent);

            }


        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.cancel();

            }


        });
        dialog.show();
    }

    //update location on location change
    public void onLocationChanged(Location location)
    {
        latitude=location.getLatitude();
        longitude=location.getLongitude();

    }
    public void onProviderDisabled(String provider)
    {

    }
    public void onProviderEnabled(String provider)
    {


    }
    public void onStatusChanged(String provider, int status, Bundle extras)
    {

    }

    public IBinder onBind(Intent intent)
    {
        return null;

    }
    public boolean canGetLocation()
    {
        return this.canGetLocation;

    }
    public void onDestory()
    {
        super.onDestroy();
        stopUsingLocation();

    }
}


