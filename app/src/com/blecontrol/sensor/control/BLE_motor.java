package com.blecontrol.sensor.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RadioGroup;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

public class BLE_motor extends BaseActivity {

	private ImageView imViewM1,imViewM2;
	private RadioGroup m1rGroup,m2rGroup;
	private Animation rotateFor,rotateBac,rotateFor_2,rotateBac_2;
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_motor);
		
		InitHandle();
	}

	private void InitHandle() {
		
		imViewM1 = (ImageView)findViewById(R.id.imageViewM1);
		m1rGroup = (RadioGroup)findViewById(R.id.radioGroupM1);
		m1rGroup.setOnCheckedChangeListener(new RadioCheck());
		
		imViewM2 = (ImageView)findViewById(R.id.imageViewM2);
		m2rGroup = (RadioGroup)findViewById(R.id.radioGroupM2);	
		m2rGroup.setOnCheckedChangeListener(new RadioCheck());
		
	    rotateFor = AnimationUtils.loadAnimation(this, R.anim.forward);
	    rotateFor.setInterpolator(new LinearInterpolator());  

	    rotateBac = AnimationUtils.loadAnimation(this, R.anim.backward);
	    rotateBac.setInterpolator(new LinearInterpolator());
	    
	    rotateFor_2 = AnimationUtils.loadAnimation(this, R.anim.forward_2);
	    rotateFor_2.setInterpolator(new LinearInterpolator());  

	    rotateBac_2 = AnimationUtils.loadAnimation(this, R.anim.backward_2);
	    rotateBac_2.setInterpolator(new LinearInterpolator()); 	    
	}
	@Override
	protected void InitListener() {
		
	}
	/**
	 * RadioGrop相应事件
	 *
	 */
	private class RadioCheck implements RadioGroup.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
			int data = 0;
			switch (checkedId) {
			case R.id.btnM1Stop:
				imViewM1.clearAnimation();
				data = 0x10;
				break;
			case R.id.btnM1For:
				imViewM1.startAnimation(rotateFor);
				data = 0x11;
				break;
			case R.id.btnM1Bac:
				imViewM1.startAnimation(rotateBac);
				data = 0x12;
				break;
			case R.id.btnM2Stop:
				imViewM2.clearAnimation();
				data = 0x20;
				break;
			case R.id.btnM2For:
				imViewM2.startAnimation(rotateFor_2);
				data = 0x21;
				break;
			case R.id.btnM2Bac:
				imViewM2.startAnimation(rotateBac_2);
				data = 0x22;
				break;
			default:
				break;
			}
			
	        if (characteristic != null)
	        {
	        	characteristic.setValue(data, BluetoothGattCharacteristic.FORMAT_UINT8,0);
	            mBluetoothLeService.writeCharacteristic(characteristic);
	        }else
	        {
	        	MyToast("需要的没有找到");
	        }
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
		return mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.Motor_Services),UUID.fromString(GattAttributes.Motor_Data));
	}

	@Override
	protected void Event_HaveData(String data) {
		
	}


}
