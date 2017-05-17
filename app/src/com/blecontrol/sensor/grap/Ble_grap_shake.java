package com.blecontrol.sensor.grap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;
import com.blecontrol.util.AnimationHelp;

import java.util.UUID;

public class Ble_grap_shake extends BaseActivity {

	private BluetoothGattCharacteristic beepCharacteristic;
	
	private boolean isStartOrNot = false;
	private AnimationHelp ainHelp;
	private ImageView shakeWaring;
	private ToggleButton mStar,mBeep;
	
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_grap_shake);
		
	    shakeWaring = (ImageView)findViewById(R.id.image_shake_waring);
	    ainHelp = new AnimationHelp(shakeWaring, this);
	    
	    mStar = (ToggleButton)findViewById(R.id.btn_shake_start);
	    mBeep = (ToggleButton)findViewById(R.id.btn_shake_beep);
	}

	@Override
	protected void InitListener() {
		
	    mStar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if (isChecked) {
					if (!mBeep.isChecked() && !isStartOrNot) {
						isStartOrNot = true;
						mBeep.setEnabled(false);		
						mBluetoothLeService.setCharacteristicNotification(characteristic, true);
					}else{
						mStar.setChecked(false);
						Toast.makeText(getApplicationContext(), "请先关闭蜂鸣器后再开始测试！", Toast.LENGTH_SHORT).show();
					}
				}else {
					if (isStartOrNot) {
						mBeep.setEnabled(true);
						isStartOrNot = false;
						mBluetoothLeService.setCharacteristicNotification(characteristic, false);
					}
				}
			}
		});
	    
	    mBeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (beepCharacteristic != null) {
					if (isChecked) {
						beepCharacteristic.setValue(0x01, BluetoothGattCharacteristic.FORMAT_UINT8,0);
					}else {
						beepCharacteristic.setValue(0x00, BluetoothGattCharacteristic.FORMAT_UINT8,0);
					}
					mBluetoothLeService.writeCharacteristic(beepCharacteristic);
				}else {
					MyToast("需要的没有找到");
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
		
		beepCharacteristic = mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.Shake_Services),UUID.fromString(GattAttributes.Shake_beep));
		
		return mBluetoothLeService.getWeNeed(
				UUID.fromString(GattAttributes.Shake_Services),
				UUID.fromString(GattAttributes.Shake_Data));
	}

	@Override
	protected void Event_HaveData(String data) {
		mBeep.setChecked(true);
		ainHelp.playAnimation(ctx);
	}

}
