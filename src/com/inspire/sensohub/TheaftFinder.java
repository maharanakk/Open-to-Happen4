package com.inspire.sensohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.Toast;

public class TheaftFinder extends Activity implements AccelerometerListener,
		AnimationListener {

	Animation animBlink;
	ImageView iv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.theaft_finder);

		iv = (ImageView) findViewById(R.id.imageView1);

		animBlink = AnimationUtils.loadAnimation(getApplicationContext(),
				R.anim.blink);
		animBlink.setAnimationListener(this);
		iv.startAnimation(animBlink);
	}

	@Override
	public void onAccelerationChanged(float x, float y, float z) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onShake(float force) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "Motion detected", Toast.LENGTH_LONG)
				.show();
		Intent i = new Intent(this, MyService.class);
		startService(i);
	}

	@Override
	public void onResume() {
		super.onResume();
		Toast.makeText(getBaseContext(), "onResume Accelerometer Started",
				Toast.LENGTH_LONG).show();

		// Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isSupported(this)) {

			// Start Accelerometer Listening
			AccelerometerManager.startListening(this);
		}
	}

	@Override
	public void onStop() {
		super.onStop();

		// Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {

			// Start Accelerometer Listening
			AccelerometerManager.stopListening();

			Toast.makeText(getBaseContext(), "onStop Accelerometer Stoped",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.i("Sensor", "Service  distroy");

		Intent i = new Intent(this, MyService.class);
		stopService(i);
		// Check device supported Accelerometer senssor or not
		if (AccelerometerManager.isListening()) {

			// Start Accelerometer Listening
			AccelerometerManager.stopListening();

			Toast.makeText(getBaseContext(), "onDestroy Accelerometer Stoped",
					Toast.LENGTH_LONG).show();
		}

	}

	@Override
	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationEnd(Animation animation) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

}
