package com.blecontrol;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.actionbarsherlock.app.SherlockActivity;

import java.util.Timer;
import java.util.TimerTask;

public class Weclome extends SherlockActivity {

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);

		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setScaleType(ScaleType.CENTER_CROP);
		imageView.setBackgroundResource(R.drawable.home_lauch);
		setContentView(imageView);
	}

	@Override
	protected void onResume() {
		super.onResume();

		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {
				timer.cancel();
				// 首页广告最多两个
				startActivity(new Intent(Weclome.this, MainActivity.class));
				finish();
			}
		}, 2000);
	}
}
