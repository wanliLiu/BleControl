package com.blecontrol.sensor.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

public class BLE_highled extends BaseActivity implements OnClickListener{

	private RadioGroup m1rGroup;
	
	private RadioButton radio_highled_dark,radio_highled_mid,radio_highled_bright;
	
	private SeekBar seekBar;
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_highled);
		
		m1rGroup = (RadioGroup)findViewById(R.id.radioGroup_high_led);
		
		radio_highled_dark = (RadioButton)findViewById(R.id.radio_highled_dark);
		radio_highled_mid = (RadioButton)findViewById(R.id.radio_highled_mid);
		radio_highled_bright = (RadioButton)findViewById(R.id.radio_highled_bright);
		
		radio_highled_dark.setOnClickListener(this);
		radio_highled_mid.setOnClickListener(this);
		radio_highled_bright.setOnClickListener(this);
		
		seekBar = (SeekBar)findViewById(R.id.seekBar);
	}

	@Override
	protected void InitListener() {
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
		        if (characteristic != null)
		        {
		        	characteristic.setValue(255 - seekBar.getProgress(), BluetoothGattCharacteristic.FORMAT_UINT8,0);
		            mBluetoothLeService.writeCharacteristic(characteristic);
		        }else
		        {
		        	MyToast("需要的没有找到");
		        }
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		int value = 0;
		switch (v.getId()) {
		case R.id.radio_highled_dark:
			value = 0xFF;
			break;
		case R.id.radio_highled_mid:
			value = 0x64;
			break;
		case R.id.radio_highled_bright:
			value = 0x0A;
			break;
		default:
			break;
		}
		
		seekBar.setProgress(255 - value);
        if (characteristic != null)
        {
        	characteristic.setValue(value, BluetoothGattCharacteristic.FORMAT_UINT8,0);
            mBluetoothLeService.writeCharacteristic(characteristic);
        }else
        {
        	MyToast("需要的没有找到");
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
				UUID.fromString(GattAttributes.HighLigt_Services),
				UUID.fromString(GattAttributes.HighLigt_Data));
	}
	
	protected void readValue() {
		showDialog();
		mBluetoothLeService.readCharacteristic(characteristic);
	};
	
	@Override
	protected void Event_HaveData(String data) {

		try {
			if (data.equals("FF")) {
				m1rGroup.check(m1rGroup.getChildAt(0).getId());
			}else if (data.equals("64")) {
				m1rGroup.check(m1rGroup.getChildAt(1).getId());
			}else if (data.equals("0A")) {
				m1rGroup.check(m1rGroup.getChildAt(2).getId());
			}
			seekBar.setProgress(255 - Integer.parseInt(data, 16));
		} catch (Exception e) {
		}
		DissDialog();
	}

}
