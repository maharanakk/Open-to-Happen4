package com.inspire.sensohub;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;


public class MainActivity extends Activity  {

	ImageButton ibTheaft,ibLBN,ibNoise;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ibTheaft  = (ImageButton)findViewById(R.id.ibTF);
        ibLBN = (ImageButton)findViewById(R.id.imageButton2);
        ibNoise = (ImageButton)findViewById(R.id.imageButton3);
        
        ibNoise.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i2= new Intent(MainActivity.this,NoiseAlert.class);
				startActivity(i2);
				
			}
		});
        ibLBN.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(MainActivity.this,MainScreen.class);
				//intent.setAction("com.inspire.sensohub.theaftfinder.THEAFTFINDER");
				startActivity(intent1);
			}
		});
        ibTheaft.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,TheaftFinder.class);
				//intent.setAction("com.inspire.sensohub.theaftfinder.THEAFTFINDER");
				startActivity(intent);
				
			}
		});
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
