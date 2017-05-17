package com.blecontrol.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Vibrator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

import com.blecontrol.R;

public class AnimationHelp {
	private Animation am;
	private SoundPool sp;
	private int soundId;
	private View mView;
	
	public AnimationHelp(View view, final Context context) {
		
		//实例SoundPool播放器
		mView = view;
		sp = new SoundPool(4, AudioManager.STREAM_MUSIC, 100);
		sp.setOnLoadCompleteListener(new OnLoadCompleteListener() {
					@Override
					public void onLoadComplete(SoundPool arg0, int arg1, int arg2) {
						// TODO Auto-generated method stub
						//Toast.makeText((Activity)context, "music load completed", Toast.LENGTH_SHORT).show();
//						sp.play(soundId, 1f, 1f, 0, 0, 1);
					}
				});
		//加载音乐文件获取其数据ID
		soundId = sp.load(context, R.raw.shake_waring, 1);
		
		am = new RotateAnimation(0, 10, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);

		// 动画开始到结束的执行时间(1000 = 1 秒)
		am.setDuration(50);

		// 动画重复次数(-1 表示一直重复)
		am.setRepeatMode(Animation.REVERSE);
		am.setRepeatCount(10);
		// am.setRepeatCount(Animation.INFINITE);
		am.setInterpolator(new LinearInterpolator());
		
		am.setAnimationListener(new Animation.AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				sp.play(soundId, 1f, 1f, 0, 0, 1);
			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				am.cancel();
				am.reset();
			}
		});

	}

	private void Vibrator(Context context)
	{
		Vibrator vibrator = (Vibrator)context.getSystemService(context.VIBRATOR_SERVICE); 
		//等待1秒，震动2秒，等待1秒，震动3秒  
		long[] pattern = {100, 500};    
		//-1表示不重复, 如果不是-1, 比如改成1, 表示从前面这个long数组的下标为1的元素开始重复.  
	    vibrator.vibrate(pattern, -1); 
	}
	
	public void playAnimation(Context context) {
		Vibrator(context);
		mView.startAnimation(am);
	}
}