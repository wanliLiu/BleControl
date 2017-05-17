package com.blecontrol.base;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.blecontrol.R;
import com.blecontrol.services.BluetoothLeService;
import com.blecontrol.util.MLog;
import com.blecontrol.util.Mytool;


public abstract class BaseActivity extends SherlockActivity implements OnClickListener{

	protected Context ctx;
	protected BluetoothLeService mBluetoothLeService;
	protected BluetoothGattCharacteristic characteristic;
	
	protected boolean isLANDSCAPE = false;
	
	protected ProgressDialog dialog;
	
	protected boolean isNeedBleservice = true;
	
	private static final int Operationing = 21;
	private static final int OperationFail = 23;
	private static final int OperationDone = 12;
	private int isOperationOkay = Operationing;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		if (!isLANDSCAPE) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}else {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		
		
		ctx = this;
		
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		if (isNeedBleservice) {
			getSupportActionBar().setTitle(Mytool.GetDevceiceName(ctx,getIntent().getStringExtra(Constants.EXTRAS_DEVICE_ADDRESS),getIntent().getIntExtra(Constants.EXTRAS_DEVICE_ID, 0)));
			startService();
		}
		
		InitView();
		InitListener();
		InitData();
	}
	
	@Override
	public void onClick(View v) {
		
	}

	/**
	 * 开启服务
	 */
	private void startService()
	{
        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);
	}
	
    // Code to manage Service lifecycle.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBluetoothLeService = ((BluetoothLeService.LocalBinder)service).getService();
            if (!mBluetoothLeService.initialize()) {
                MLog.e("Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBluetoothLeService.connect(getIntent().getStringExtra(Constants.EXTRAS_DEVICE_ADDRESS));
//            ServiceConnected(mBluetoothLeService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBluetoothLeService = null;
        }
    };
    
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.ACTION_GATT_CONNECTED);
        intentFilter.addAction(Constants.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(Constants.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(Constants.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    
    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (Constants.ACTION_GATT_CONNECTED.equals(action)) {
            	showDialog();
            	Event_Connect();
            } else if (Constants.ACTION_GATT_DISCONNECTED.equals(action)) {
            	Event_Disconnect();
            } else if (Constants.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
            	try {
            		characteristic = Event_ServicesFind();
            		DissDialog();
            		if (characteristic != null) {
            			isOperationOkay = OperationDone;
            			invalidateOptionsMenu();
						readValue();
					}
				} catch (Exception e) {
					MyToast("请重新启动蓝牙！");
					isOperationOkay = OperationFail;
					BaseActivity.this.finish();
					e.printStackTrace();
					DissDialog();
				}
            } else if (Constants.ACTION_DATA_AVAILABLE.equals(action)) {
            	try {
            		MLog.e("获取到的数据", intent.getStringExtra(Constants.EXTRA_DATA));
            		Event_HaveData(intent.getStringExtra(Constants.EXTRA_DATA));
				} catch (Exception e) {
					e.printStackTrace();
				}
            	
            }
        }
    };
    
    @Override
    protected void onResume() {
        super.onResume();
        if (isNeedBleservice) {
        	 registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
		}
       
        if (mBluetoothLeService != null) {
//        	mBluetoothLeService.connect(getIntent().getStringExtra(Constants.EXTRAS_DEVICE_NAME));
//        	ServiceConnected(mBluetoothLeService);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        
        if (isNeedBleservice) {
            unregisterReceiver(mGattUpdateReceiver);
		}
        
        DissDialog();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isNeedBleservice) {
            unbindService(mServiceConnection);
            mBluetoothLeService = null;
		}
    }
    
    
	protected abstract void InitView();
	protected abstract void InitListener();
	protected abstract void InitData();
	
    /**
     * 链接成功后，连接设备
     * @param service
     */
//    protected abstract void ServiceConnected(BluetoothLeService service);

	
	protected abstract void Event_Connect();
	protected abstract void Event_Disconnect();
	/**
	 * 特定的服务
	 * @return
	 */
	protected abstract BluetoothGattCharacteristic Event_ServicesFind();
	protected abstract void Event_HaveData(String data);
	
	protected void readValue() {
		
	}
	
	protected void showDialog()
	{
		if (dialog != null) {
			dialog = null;
		}
		dialog = new ProgressDialog(ctx);
		dialog.setMessage("正在进行必要操作，请稍后...");
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setCancelable(true);
		dialog.show();
	}
	
	protected void DissDialog()
	{
		if (dialog != null) {
			dialog.dismiss();
			dialog = null;
		}
	}
	
    /**
     * 显示Toast
     * 
     * @param text 文本内容
     */
    protected void MyToast(String text) {
        Toast.makeText(ctx, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示Toast
     * 
     * @param resId string资源id
     */
    protected void MyToast(int resId) {
        Toast.makeText(ctx, resId, Toast.LENGTH_SHORT).show();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	if (!isNeedBleservice) {
			super.onCreateOptionsMenu(menu);
		}else {
	    	getSupportMenuInflater().inflate(R.menu.gatt_services, menu);
	    	if (isOperationOkay == Operationing) {
	    		
	    		menu.findItem(R.id.Connecting).setVisible(true);
	            menu.findItem(R.id.Connecting).setActionView(R.layout.actionbar_indeterminate_progress);
	            menu.findItem(R.id.ConnectOkay).setVisible(false);
	            menu.findItem(R.id.ConnectFail).setVisible(false);
	            
			}else if(isOperationOkay == OperationFail)
			{    	
				menu.findItem(R.id.Connecting).setVisible(false);
		        menu.findItem(R.id.Connecting).setActionView(null);
		        menu.findItem(R.id.ConnectOkay).setVisible(false);
		        menu.findItem(R.id.ConnectFail).setVisible(true);
				
			}else if(isOperationOkay == OperationDone)
			{
				menu.findItem(R.id.Connecting).setVisible(false);
		        menu.findItem(R.id.Connecting).setActionView(null);
		        menu.findItem(R.id.ConnectOkay).setVisible(true);
		        menu.findItem(R.id.ConnectFail).setVisible(false);
			}
		}

        return true;
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
