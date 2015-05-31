package com.inspire.sensohub;

import java.io.IOException;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class MyService extends Service{

	MediaPlayer player;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		player = MediaPlayer.create(getApplicationContext(), R.raw.music);
		player.setAudioStreamType(AudioManager.STREAM_MUSIC);
		try {
			player.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// screen will stay on during this section
		player.start();
		
		
		// TODO Auto-generated method stub
		return START_STICKY;
	}
	private void onStop() {
		// TODO Auto-generated method stub
		player.stop();
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		player.stop();
	}

}
