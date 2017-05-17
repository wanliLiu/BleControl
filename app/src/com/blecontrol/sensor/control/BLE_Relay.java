package com.blecontrol.sensor.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;
import android.widget.ToggleButton;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

public class BLE_Relay extends BaseActivity {

	private ToggleButton Relay1,Relay2;
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_relay);
		
	    Relay1 = (ToggleButton)findViewById(R.id.btn_relay_1);
	    Relay1.setOnClickListener(this);
	    Relay2 = (ToggleButton)findViewById(R.id.btn_relay_2);
	    Relay2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int dataString = 0;
		if (v.getId() == R.id.btn_relay_1) {//继电器1
			if (Relay1.isChecked()) {//关闭
				 dataString =2;
			}else {//打开
				dataString =1;
			}	
		}else {//继电器2
			if (Relay2.isChecked()) {//关闭
				dataString =4;
			}else {//打开
				dataString =3;
			}			
		}	
		
        if (characteristic != null)
        {
        	characteristic.setValue(dataString, BluetoothGattCharacteristic.FORMAT_UINT8,0);
            mBluetoothLeService.writeCharacteristic(characteristic);
        }else
        {
        	MyToast("需要的没有找到");
        }
	}

	@Override
	protected void InitListener() {
		
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

	protected void readValue() {
		showDialog();
		mBluetoothLeService.readCharacteristic(characteristic);
	}
	
	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.Relay_Services),UUID.fromString(GattAttributes.Relay_Data));
	}

	@Override
	protected void Event_HaveData(String data) {
	       /* 读
	        * 1关 2关    5
	        * 1关 2开    6
	        * 1开 2关    7
	        * 1开 2开    8
	        */
		try {
			switch (Integer.valueOf(data)) {
			case 5:
				Relay1.setChecked(true);
				Relay2.setChecked(true);
				break;
			case 6:
				Relay1.setChecked(true);
				Relay2.setChecked(false);
				break;
			case 7:
				Relay1.setChecked(false);
				Relay2.setChecked(true);
				break;
			case 8:
				Relay1.setChecked(false);
				Relay2.setChecked(false);
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
		DissDialog();
	}


}
