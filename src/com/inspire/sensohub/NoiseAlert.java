package com.inspire.sensohub;

import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class NoiseAlert extends Activity {

	/* constants */
    private static final int POLL_INTERVAL = 300;
    
    
    TextToSpeech t1;
    /** running state **/
    private boolean mRunning = false;
     
    /** config state **/
    private int mThreshold;
     
    private PowerManager.WakeLock mWakeLock;

    private Handler mHandler = new Handler();

    /* References to view elements */
    private TextView mStatusView;
    private SoundLevelView mDisplay;

    /* data source */
    private SoundMeter mSensor;
     
     

 /****************** Define runnable thread again and again detect noise *********/
  
    private Runnable mSleepTask = new Runnable() {
            public void run() {
                //Log.i("Noise", "runnable mSleepTask");
                     
                start();
            }
    };
     
    // Create runnable thread to Monitor Voice
    private Runnable mPollTask = new Runnable() {
            public void run() {
                 
                    double amp = mSensor.getAmplitude();
                    //Log.i("Noise", "runnable mPollTask");
                    updateDisplay("Monitoring Voice...", amp);

                    if ((amp > mThreshold)) {
                          /*callForHelp();
                          Thread timer = new Thread(){
                        	  public void run()
                        	  {
                        		  try {
									sleep(5000);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
                        	  }
                          };
                          timer.start();*/
                    	
                    	t1.speak("Please reduce the noise", TextToSpeech.QUEUE_FLUSH, null);
                    	amp =0.0;
                		Toast.makeText(getApplicationContext(),
                				"Noise Thersold Crossed, do here your stuff.",
                				Toast.LENGTH_LONG).show();
                		
                          //Log.i("Noise", "==== onCreate ===");
                		 stop();
                    }
                    // Runnable(mPollTask) will again execute after POLL_INTERVAL
                    mHandler.postDelayed(mPollTask, POLL_INTERVAL);
                   
            }
    };
     
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Defined SoundLevelView in main.xml file
		setContentView(R.layout.noise);
		mStatusView = (TextView) findViewById(R.id.status);

		// Used to record voice
		mSensor = new SoundMeter();
		mDisplay = (SoundLevelView) findViewById(R.id.volume);

		PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = pm.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK,
				"NoiseAlert");
		t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
	         @Override
	         public void onInit(int status) {
	            if(status != TextToSpeech.ERROR) {
	               t1.setLanguage(Locale.US);
	            }
	         }
	      });
	}

	@Override
    public void onDestroy() {
        // Don't forget to shutdown tts!
        if (t1 != null) {
            t1.stop();
            t1.shutdown();
        }
        super.onDestroy();
    }
	@Override
	public void onResume() {
		super.onResume();
		// Log.i("Noise", "==== onResume ===");

		initializeApplicationConstants();
		mDisplay.setLevel(0, mThreshold);

		if (!mRunning) {
			mRunning = true;
			start();
		}
	}

	@Override
	public void onStop() {
		super.onStop();
		// Log.i("Noise", "==== onStop ===");

		// Stop noise monitoring
		stop();

	}

	private void start() {
		// Log.i("Noise", "==== start ===");

		mSensor.start();
		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
		}

		// Noise monitoring start
		// Runnable(mPollTask) will execute after POLL_INTERVAL
		mHandler.postDelayed(mPollTask, POLL_INTERVAL);
	}

	private void stop() {
		Log.i("Noise", "==== Stop Noise Monitoring===");
		if (mWakeLock.isHeld()) {
			mWakeLock.release();
		}
		mHandler.removeCallbacks(mSleepTask);
		mHandler.removeCallbacks(mPollTask);
		mSensor.stop();
		mDisplay.setLevel(0, 0);
		updateDisplay("stopped...", 0.0);
		mRunning = false;

	}

	private void initializeApplicationConstants() {
		// Set Noise Threshold
		mThreshold = 8;

	}

	private void updateDisplay(String status, double signalEMA) {
		mStatusView.setText(status);
		//
		mDisplay.setLevel((int) signalEMA, mThreshold);
	}

	private void callForHelp() {

		// stop();

		// Show alert when noise thersold crossed
		/*t1.speak("Please reduce the noise", TextToSpeech.QUEUE_FLUSH, null);
		Toast.makeText(getApplicationContext(),
				"Noise Thersold Crossed, do here your stuff.",
				Toast.LENGTH_LONG).show();*/
	}

}
