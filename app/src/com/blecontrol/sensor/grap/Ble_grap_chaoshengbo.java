package com.blecontrol.sensor.grap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Typeface;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

public class Ble_grap_chaoshengbo extends BaseActivity {

	private ToggleButton mStart;
	private TextView distanceDisplay;
	private boolean isStartOrNot = false;
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_grap_chao);
		
	    mStart  = (ToggleButton)findViewById(R.id.btn_chao_start);
	    distanceDisplay = (TextView)findViewById(R.id.text_chao_distance);
	    String path = "fonts/digifaw.ttf";//
	    Typeface tf = Typeface.createFromAsset(getAssets(), path);
	    distanceDisplay.setTypeface(tf);
	    distanceDisplay.setTextSize(100);
	    distanceDisplay.setText("0");
	    
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
				UUID.fromString(GattAttributes.ChaoShengBo_services),
				UUID.fromString(GattAttributes.ChaoShengBo_data));
	}

	@Override
	protected void Event_HaveData(String data) {
		
		if (isStartOrNot) {
			data = data.substring(2,4) + data.substring(0,2);
			distanceDisplay.setText(String.valueOf(Integer.valueOf(data,16)));
		}
	}


}
