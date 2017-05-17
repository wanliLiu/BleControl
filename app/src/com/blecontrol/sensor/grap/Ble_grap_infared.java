package com.blecontrol.sensor.grap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ToggleButton;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;
import com.blecontrol.util.AnimationHelp;

import java.util.UUID;

public class Ble_grap_infared extends BaseActivity {

	private ToggleButton mStar;
	private ImageView image_infraed;
	private AnimationHelp ainHelp;
	private boolean isStartOrNot = false;
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_grap_infraed);
		
	    mStar = (ToggleButton)findViewById(R.id.btn_infraed_start);
	    image_infraed = (ImageView)findViewById(R.id.image_infraed);
	    ainHelp = new AnimationHelp(image_infraed, this);
	    
	}

	@Override
	protected void InitListener() {
	    mStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (characteristic != null) {
					if (isChecked) {
						isStartOrNot = true;
						image_infraed.setBackgroundResource(R.drawable.infraed_nothappen);
						mBluetoothLeService.setCharacteristicNotification(characteristic, true);
					} else {
						image_infraed.setBackgroundResource(R.drawable.infraed_happen);
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
	protected void InitData() {

	}

	@Override
	protected void Event_Connect() {

	}

	@Override
	protected void Event_Disconnect() {

	}
	@Override
	protected void onPause() {
		super.onPause();
		if (isStartOrNot) {
			mBluetoothLeService.setCharacteristicNotification(characteristic, false);
		}
	}
	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(
				UUID.fromString(GattAttributes.Infrared_services),
				UUID.fromString(GattAttributes.Infrared_data));
	}

	@Override
	protected void Event_HaveData(String data) {
		if (isStartOrNot) {
			if (data.equals("01")) {
				ainHelp.playAnimation(ctx);
				image_infraed.setBackgroundResource(R.drawable.infraed_happen);
			}else {
				image_infraed.setBackgroundResource(R.drawable.infraed_nothappen);
			}
		}
	}

}
