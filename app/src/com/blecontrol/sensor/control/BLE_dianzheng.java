package com.blecontrol.sensor.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LevelListDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

public class BLE_dianzheng extends BaseActivity {

	private ImageView btnlef,btnright;
	private Button btnSure;
	private ImageView mdiandisplay;
	private int currentIndex = 0;
	private LevelListDrawable dianzhengDrawable;
	
	@Override
	protected void InitView() {
		
		setContentView(R.layout.ble_sensor_control_dianzheng);
		
		dianzhengDrawable =(LevelListDrawable)this.getResources().getDrawable(R.drawable.dianzheng_display);
		
	    btnlef = (ImageView)findViewById(R.id.bnt_lef);
	    btnright = (ImageView)findViewById(R.id.btn_right);
	    mdiandisplay = (ImageView)findViewById(R.id.imadianz);
	    mdiandisplay.setBackgroundDrawable(getMDrawable(currentIndex));
	    btnSure = (Button)findViewById(R.id.btn_dianzheng_sure);
	    btnlef.setOnClickListener(this);
	    btnright.setOnClickListener(this);
	    btnSure.setOnClickListener(this);
	}
	/**
	 * 获取显示的图片
	 */
	private Drawable getMDrawable(int i){
		
		Drawable image = null;
		if (dianzhengDrawable != null) {
			
			dianzhengDrawable.setLevel(i);
			image = dianzhengDrawable.getCurrent();
		}
		
		return image;
	}
	
	@Override
	protected void InitListener() {
		
	}

	@Override
	protected void InitData() {
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bnt_lef:
			currentIndex--;
			if (currentIndex == -1) {
				currentIndex = 9;
			}
			mdiandisplay.setBackgroundDrawable(getMDrawable(currentIndex));
			break;
		case R.id.btn_right:
			currentIndex++;
			if (currentIndex == 10) {
				currentIndex = 0;
			}	
			mdiandisplay.setBackgroundDrawable(getMDrawable(currentIndex));
			break;
		case R.id.btn_dianzheng_sure:
	        if (characteristic != null)
	        {
//	        	characteristic.setWriteType(BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE);
	        	characteristic.setValue(currentIndex,BluetoothGattCharacteristic.FORMAT_UINT8,0);
	            mBluetoothLeService.writeCharacteristic(characteristic);
	        }else
	        {
	        	MyToast("需要的没有找到");
	        }
			break;			
		default:
			break;
		}
	}	
	
//	@Override
//	protected void ServiceConnected(BluetoothLeService service) {
//		
//	}

	@Override
	protected void Event_Connect() {
		
	}

	@Override
	protected void Event_Disconnect() {
		
	}

	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.dianzheng_Services),UUID.fromString(GattAttributes.dianzheng_LEDData));
	}

	protected void readValue() {
		showDialog();
		mBluetoothLeService.readCharacteristic(characteristic);
	};
	
	@Override
	protected void Event_HaveData(String data) {
		try {
			currentIndex = Integer.parseInt(data);
			mdiandisplay.setBackgroundDrawable(getMDrawable(currentIndex));
		} catch (Exception e) {
		}
		DissDialog();
	}
}
