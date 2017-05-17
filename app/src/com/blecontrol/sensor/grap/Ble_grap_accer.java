package com.blecontrol.sensor.grap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

public class Ble_grap_accer extends BaseActivity {

	private RotateAnimation rotatex,rotatey,rotatez;
	private ImageView imageX,imageY,imageZ;
	private TextView  textX,textY,textZ;
	private int last_x,last_y,last_z;
	
    private ToggleButton mStart;
    
    private boolean isStartOrNot = false;
    
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_grap_accer);
		
	    textX = (TextView)findViewById(R.id.textX);
	    textY = (TextView)findViewById(R.id.textY);
	    textZ = (TextView)findViewById(R.id.textZ);
	    imageX = (ImageView)findViewById(R.id.imageX);
	    imageY = (ImageView)findViewById(R.id.imageY);
	    imageZ = (ImageView)findViewById(R.id.imageZ);
	    mStart = (ToggleButton)findViewById(R.id.btn_accer_start);
	    
		rotatex = new RotateAnimation(0, 90,Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
		rotatex.setDuration(500);
		rotatex.setFillAfter(true);
	}

	@Override
	protected void InitListener() {
	    mStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (characteristic != null) {
					if (isChecked) {
						isStartOrNot = true;
						mBluetoothLeService.setCharacteristicNotification(characteristic, true);
					} else {
						imageX.clearAnimation();
						imageY.clearAnimation();
						imageZ.clearAnimation();
						isStartOrNot = false;
						mBluetoothLeService.setCharacteristicNotification(characteristic, false);
					}
				}else {
					MyToast("要的没有找到");
				}
			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		
		if (isStartOrNot) {
			mBluetoothLeService.setCharacteristicNotification(characteristic, false);
		}
	}
	@Override
	protected void InitData() {
		
	}

	@Override
	protected void Event_Connect() {
		
	}

	@Override
	protected void Event_Disconnect() {
		
	}

	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(
				UUID.fromString(GattAttributes.Accer_Services),
				UUID.fromString(GattAttributes.Accer_Data));
	}
	
	@Override
	protected void Event_HaveData(String who) {
		try {
			if (isStartOrNot) {
				int x = Integer.valueOf( who.substring(2, 4), 16);
				int y = Integer.valueOf( who.substring(4, 6), 16);
				int z = Integer.valueOf( who.substring(6, 8), 16);
				rotatex = new RotateAnimation(last_x * 10, (x - last_x) * 10 + last_x * 10, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
				rotatex.setFillAfter(true);
				rotatex.setDuration(500);
				imageX.startAnimation(rotatex);
				rotatey = new RotateAnimation(last_y * 10, (y - last_y) * 10 + last_y * 10, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
				rotatey.setDuration(500);
				rotatey.setFillAfter(true);
				imageY.startAnimation(rotatey);
				rotatez = new RotateAnimation(last_z * 10, (z - last_z) * 10 + last_z * 10, Animation.RELATIVE_TO_SELF, 0.5f,Animation.RELATIVE_TO_SELF, 0.5f);
				rotatez.setDuration(500);
				rotatez.setFillAfter(true);
				imageZ.startAnimation(rotatez);

				last_x = x;
				last_y = y;
				last_z = z;

				textX.setText("X方向：" + x + "m/s");
				textY.setText("Y方向：" + y + "m/s");
				textZ.setText("Z方向：" + z + "m/s");									
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
