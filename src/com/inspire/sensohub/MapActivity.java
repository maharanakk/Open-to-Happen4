package com.inspire.sensohub;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends Activity {
	private GoogleMap mMap;
	Circle mCircle;
	Marker mMarker;
	LocationManager locationManager;
	Location currentLocation;
	Boolean locationChanged;
	float radius = 1000;
	double destLat;
	double destLong;
	private Button savePointButton;
	private static final String PROX_ALERT_INTENT = "com.inspire.sensohub.ProximityAlert";
	private static final int notifyId = 100;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.map);
		mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
				.getMap();
		mMap.setMyLocationEnabled(true);

		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, new MyLocationListener());
		
		mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

			@Override
			public void onMapClick(LatLng point) {
				destLat = point.latitude;
				destLong = point.longitude;
				LatLng latlng = new LatLng(destLat, destLong);
				if (mCircle == null || mMarker == null) {
					drawMarkerWithCircle(latlng);
				} else {
					updateMarkerWithCircle(latlng);
				}

			}
		});

		mMap.setOnMarkerClickListener(new OnMarkerClickListener() {

			@SuppressWarnings("deprecation")
			@Override
			public boolean onMarkerClick(Marker marker) {
				AlertDialog alert = new AlertDialog.Builder(MapActivity.this)
						.create();
				alert.setTitle("Add Alarm");
				alert.setMessage("Create an alarm at this point?");
				alert.setButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						saveProximityAlertPoint();
					}
				});
				alert.setButton2("Cancel",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}
						});
				alert.show();
				return true;
			}
		});
	}

	private void saveProximityAlertPoint() {
		Location location = locationManager
				.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		NotificationManager mNotificationManager = (NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
		Intent mNotificationIntent = new Intent(this, MapActivity.class);
		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mNotificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		mNotificationIntent.setAction(Intent.ACTION_MAIN);
		mNotificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		PendingIntent mpendingIntent = PendingIntent.getActivity(this, 0,
				mNotificationIntent, 0);
		NotificationCompat.Builder mNotification = new NotificationCompat.Builder(
				this)
				.setSmallIcon(R.drawable.notification)
				.setContentTitle("Alarm is active")
				.setAutoCancel(true)
				.setWhen(new Date().getTime())
				.setContentIntent(mpendingIntent);
		if (location == null) {
			Toast.makeText(this, "No last known location found",
					Toast.LENGTH_LONG).show();
		}else{
		Toast.makeText(this, "Alarm Set", Toast.LENGTH_LONG).show();
		addProximityAlert(destLat, destLong);
		mNotificationManager.notify(notifyId, mNotification.build());
		}
	}

	private void addProximityAlert(double latitude, double longitude) {
		Intent intent = new Intent(PROX_ALERT_INTENT);
		PendingIntent proximityIntent = PendingIntent.getBroadcast(this, 0,
				intent, 0);
		locationManager.addProximityAlert(latitude, longitude, radius, -1,
				proximityIntent);
		IntentFilter filter = new IntentFilter(PROX_ALERT_INTENT);
		registerReceiver(new ProximityReceiver(), filter);
	}

	public void drawMarkerWithCircle(LatLng position) {
		double radiusInMeters = 1000.0;
		int strokeColor = 0XFF0000FF;
		int shadeColor = 0XFF87CEFA;

		CircleOptions circleoptions = new CircleOptions().center(position)
				.radius(radiusInMeters).fillColor(shadeColor)
				.strokeColor(strokeColor).strokeWidth(2);
		mCircle = mMap.addCircle(circleoptions);

		MarkerOptions markeroptions = new MarkerOptions().position(position);
		mMarker = mMap.addMarker(markeroptions);
	}

	public void updateMarkerWithCircle(LatLng position) {

		mCircle.setCenter(position);
		mMarker.setPosition(position);
	}

	public void geoLocate(View v) throws IOException {
		hideSoftKeyboard(v);
		EditText et = (EditText) findViewById(R.id.et_location);
		String location = et.getText().toString();

		Geocoder gc = new Geocoder(this);
		List<Address> list = gc.getFromLocationName(location, 2);

		Address add = list.get(0);
		String locality = add.getLocality();
		Toast.makeText(this, locality, Toast.LENGTH_LONG).show();

		double lat = add.getLatitude();
		double lng = add.getLongitude();

		gotoLocation(lat, lng, 16.0f);
	}

	public void gotoLocation(double lat, double lng, float zoom) {

		LatLng ll = new LatLng(lat, lng);
		CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
		mMap.moveCamera(update);

	}

	public void hideSoftKeyboard(View v) {
		InputMethodManager im = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
		im.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}


}
