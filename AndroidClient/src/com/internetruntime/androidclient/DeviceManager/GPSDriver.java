package com.internetruntime.androidclient.DeviceManager;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

public class GPSDriver {
	private Context context;
	
	public GPSDriver(Context context) {
		this.context = context;
	}
	
	public String getAddress()
	{
		String ans = "";
		Log.d("loc", "before get");
		LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
		Log.d("loc", "get mng: " + locationManager.toString());
		boolean gpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean netStatus = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		Log.d("loc", "GPS status: " + String.valueOf(gpsStatus));
		Log.d("loc", "Network status: " + String.valueOf(netStatus));
		if (gpsStatus || netStatus)
		{
			
			Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//			Log.d("loc", String.valueOf(location));
			Double longitude = location.getLongitude();
			Double latitude = location.getLatitude();
			Log.d("loc", String.valueOf(longitude));
			Log.d("loc", String.valueOf(latitude));
			Geocoder geocoder = new Geocoder(context, Locale.CHINA);
			try {
				List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
				Log.d("loc", "Address count: " + addresses.size());
				Log.d("loc", "Address: " + addresses.get(0).getAddressLine(4));
				for (int i = 0; i < 3; i++)
					ans = ans + addresses.get(0).getAddressLine(i) + ",";
				ans = ans + addresses.get(0).getAddressLine(3);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.d("loc", e.toString());
				ans = ans + "Can not connect to google location server.";
			}
			
		}
		else
		{
			Log.d("loc", "service disabled");
		}
		return ans;
	}
}
