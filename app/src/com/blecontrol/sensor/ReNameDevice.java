package com.blecontrol.sensor;

import android.bluetooth.BluetoothGattCharacteristic;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.Constants;
import com.blecontrol.util.Mytool;
import com.blecontrol.util.ReNameUtil;

public class ReNameDevice extends BaseActivity {

	private EditText reNameInput;
	private Button btnDelete,btnSet;
	
	private String deviceAddress = "";
	private String deviceName = "";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		isNeedBleservice = false;
	    super.onCreate(savedInstanceState);
	    
	    getSupportActionBar().setTitle("重命名：" + Mytool.GetDevceiceName(ctx,getIntent().getStringExtra(Constants.EXTRAS_DEVICE_ADDRESS),getIntent().getIntExtra(Constants.EXTRAS_DEVICE_ID, 0)));
	    
	}

	@Override
	protected void InitView() {
		setContentView(R.layout.re_name_device);
		
		reNameInput = (EditText)findViewById(R.id.reNameInput);
		btnDelete = (Button)findViewById(R.id.btnDelete);
		btnSet = (Button)findViewById(R.id.btnSet);
	}

	@Override
	protected void InitListener() {
		
		btnDelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ReNameUtil.getInstance(ctx).removeName(ctx, deviceAddress);
				MyToast("删除设备：" + deviceAddress + "重命名");
				setResult(RESULT_OK);
				ReNameDevice.this.finish();
			}
		});
		
		btnSet.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!deviceName.equals(reNameInput.getText().toString())) {
					ReNameUtil.getInstance(ctx).setName(ctx, reNameInput.getText().toString(), deviceAddress);
					MyToast("设备：" + deviceAddress + " 重命名成功");
					setResult(RESULT_OK);
				}
				ReNameDevice.this.finish();
			}
		});
	}

	@Override
	protected void InitData() {
		
		deviceAddress = getIntent().getStringExtra(Constants.EXTRAS_DEVICE_ADDRESS);
		deviceName = getIntent().getStringExtra(Constants.EXTRAS_DEVICE_NAME);
		
		reNameInput.setText(deviceName);
		reNameInput.setSelection(deviceName.length());
		
		
		if (TextUtils.isEmpty(ReNameUtil.getInstance(ctx).getName(ctx, deviceAddress))) {
			btnDelete.setVisibility(View.GONE);
		}
	}

	@Override
	protected void Event_Connect() {
		
	}

	@Override
	protected void Event_Disconnect() {
		
	}

	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return null;
	}

	@Override
	protected void Event_HaveData(String data) {
		
	}

}
