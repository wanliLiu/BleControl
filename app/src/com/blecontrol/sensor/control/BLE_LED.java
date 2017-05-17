package com.blecontrol.sensor.control;

import android.bluetooth.BluetoothGattCharacteristic;
import android.view.View;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.Button;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import java.util.UUID;

import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.OnWheelClickedListener;
import kankan.wheel.widget.OnWheelScrollListener;
import kankan.wheel.widget.WheelView;
import kankan.wheel.widget.adapters.NumericWheelAdapter;

public class BLE_LED extends BaseActivity {

	private Button mbtn,mbtnsure;
	private int led1,led2,led3,led4;
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_sensor_control_led);
		
        initWheel(R.id.Line_1);
        initWheel(R.id.Line_2);
        initWheel(R.id.Line_3);
        initWheel(R.id.Line_4);
        mbtn = (Button)findViewById(R.id.btn_ram);
        mbtn.setOnClickListener(this);
        mbtnsure = (Button)findViewById(R.id.btn_sure_led);
        mbtnsure.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_ram:
			mbtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_disable));
			mbtnsure.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_disable));
			mbtnsure.setEnabled(false);
			mbtn.setEnabled(false);
            mixWheel(R.id.Line_1);
            mixWheel(R.id.Line_2);
            mixWheel(R.id.Line_3);
            mixWheel(R.id.Line_4);			
			break;
		case R.id.btn_sure_led:
		{
        	characteristic.setValue(getnumString(),BluetoothGattCharacteristic.FORMAT_UINT16,0);
            mBluetoothLeService.writeCharacteristic(characteristic);
		}
			break;
		default:
			break;
		}
	}
	/**
	 * 把led1 led2 led3 led4 合成字符串
	 */
	private int getnumString() {
		
		String temp = "0000";
		temp = String.valueOf(led1) + String.valueOf(led2) + String.valueOf(led3) + String.valueOf(led4);
//		temp = Integer.toHexString(Integer.parseInt(temp));
//		if (temp.length() == 1) {
//			temp = "000" + temp;
//		}
//		else if (temp.length() == 2) {
//			temp = "00" + temp;
//		}else if (temp.length() == 3) {
//			temp = "0" + temp;
//		}		
		
		return Integer.parseInt(temp);
	}
    /**
     * Initializes wheel
     * @param id the wheel widget Id
     */
    private void initWheel(int id) {
    	int mid = 0;
        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(new NumericWheelAdapter(this, 0, 9));
        mid = (int)(Math.random() * 10);
        switch (id) {
		case R.id.Line_1:
			led1 = mid;
			break;
		case R.id.Line_2:
			led2 = mid;
			break;
		case R.id.Line_3:
			led3 = mid;
			break;
		case R.id.Line_4:
			led4 = mid;
			break;			
		default:
			break;
		}
        wheel.setCurrentItem(mid);
        wheel.addChangingListener(changedListener);
        wheel.addScrollingListener(scrolledListener);
        wheel.addClickingListener(click);
        wheel.setCyclic(true);
        wheel.setInterpolator(new AnticipateOvershootInterpolator());
    }
    /**
     * Returns wheel by Id
     * @param id the wheel Id
     * @return the wheel with passed Id
     */
    private WheelView getWheel(int id) {
    	return (WheelView) findViewById(id);
    } 
    /**
     * Mixes wheel
     * @param id the wheel id
     */
    private void mixWheel(int id) {
        WheelView wheel = getWheel(id);
        wheel.scroll(-50 + (int)(Math.random() * 50), 2000);
    }    
    // Wheel scrolled flag
    private boolean wheelScrolled = false;
    
    private OnWheelClickedListener click = new OnWheelClickedListener() {
        public void onItemClicked(WheelView wheel, int itemIndex) {
            wheel.setCurrentItem(itemIndex, true);
        }
    };    
    // Wheel scrolled listener
    private OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        public void onScrollingStarted(WheelView wheel) {
            wheelScrolled = true;
        }
        public void onScrollingFinished(WheelView wheel) {
			mbtnsure.setEnabled(true);
			mbtnsure.setBackgroundResource(R.drawable.btn_select);
			mbtn.setEnabled(true);
			mbtn.setBackgroundResource(R.drawable.btn_select);			
            wheelScrolled = false;
            int id = wheel.getId();
            int num = getWheel(id).getCurrentItem();
            switch (id) {
    		case R.id.Line_1:
    			led1 = num;
    			break;
    		case R.id.Line_2:
    			led2 = num;
    			break;
    		case R.id.Line_3:
    			led3 = num;
    			break;
    		case R.id.Line_4:
    			led4 = num;
    			break;	
			default:
				break;
			}
            
        }
    };
    
    // Wheel changed listener
    private OnWheelChangedListener changedListener = new OnWheelChangedListener() {
        public void onChanged(WheelView wheel, int oldValue, int newValue) {
            if (!wheelScrolled) {
            	
            }
        }
    };
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

	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.LED_Services),UUID.fromString(GattAttributes.LED_LEDData));
	}

	@Override
	protected void Event_HaveData(String data) {
		
	}

}
