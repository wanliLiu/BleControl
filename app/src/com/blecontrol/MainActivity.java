package com.blecontrol;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.blecontrol.base.Constants;
import com.blecontrol.sensor.ReNameDevice;
import com.blecontrol.util.MLog;
import com.blecontrol.util.Mytool;

import java.util.ArrayList;

public class MainActivity extends SherlockListActivity {

//	private static final String TAG = MainActivity.class.getSimpleName();
	
    private LeDeviceListAdapter mLeDeviceListAdapter;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning;
    private Handler mHandler;

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_RENAME_OK = 2;
    
    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 10000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle(R.string.title_devices);
        mHandler = new Handler();

        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        
        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        setListAdapter(mLeDeviceListAdapter);
        
    }
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	getSupportMenuInflater().inflate(R.menu.main, menu);
        if (!mScanning) {
            menu.findItem(R.id.menu_stop).setVisible(false);
            menu.findItem(R.id.menu_scan).setVisible(true);
            menu.findItem(R.id.menu_refresh).setVisible(false);
            menu.findItem(R.id.menu_refresh).setActionView(null);
        } else {
            menu.findItem(R.id.menu_stop).setVisible(true);
            menu.findItem(R.id.menu_scan).setVisible(false);
            menu.findItem(R.id.menu_refresh).setVisible(true);
            menu.findItem(R.id.menu_refresh).setActionView(
                    R.layout.actionbar_indeterminate_progress);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_scan:
                mLeDeviceListAdapter.clear();
                scanLeDevice(true);
                break;
            case R.id.menu_stop:
                scanLeDevice(false);
                break;
            case R.id.RestartBlueTooth:
            {
    	        if (mBluetoothAdapter.isEnabled() && 
     	        		mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
     	        {
       	          if (mBluetoothAdapter.isDiscovering())
      	        	 mBluetoothAdapter.cancelDiscovery();
       	          
     	        	mBluetoothAdapter.disable();
     	        	mLeDeviceListAdapter.clear();
     	        }
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
       	              Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
       	              startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                    }
                }, 100);
            }
            	break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        if (mBluetoothAdapter.isEnabled() && mBluetoothAdapter.getState() == BluetoothAdapter.STATE_ON) {
            scanLeDevice(true);
		}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    	
    	if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUEST_ENABLE_BT:
		        scanLeDevice(true);
				break;
			case REQUEST_RENAME_OK:
				mLeDeviceListAdapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}else if (requestCode == REQUEST_ENABLE_BT) {
            finish();
            return;
		}
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPause() {
        super.onPause();
        scanLeDevice(false);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        final BluetoothDevice device = mLeDeviceListAdapter.getDevice(position);
        if (device == null) return;
        ViewHolder holder = (ViewHolder)v.getTag();
        Class<?> mClass = Mytool.getClass(holder.index);
        if (mClass != null) {
            final Intent intent = new Intent(this, mClass);
            intent.putExtra(Constants.EXTRAS_DEVICE_NAME, device.getName());
            intent.putExtra(Constants.EXTRAS_DEVICE_ADDRESS, device.getAddress());
            intent.putExtra(Constants.EXTRAS_DEVICE_ID, holder.index);
            if (mScanning) {
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
                mScanning = false;
            }
            startActivity(intent);
		}else {
			Toast.makeText(MainActivity.this, "暂时还不支持此类型的节点，后续会加上，敬请期待！", Toast.LENGTH_SHORT).show();
		}
    }

    private void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                    invalidateOptionsMenu();
                }
            }, SCAN_PERIOD);

            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
            mLeDeviceListAdapter.clear();
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }
        invalidateOptionsMenu();
    }

    // Adapter for holding devices found through scanning.
    private class LeDeviceListAdapter extends BaseAdapter {
        private ArrayList<BluetoothDevice> mLeDevices;
        private LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<BluetoothDevice>();
            mInflator = MainActivity.this.getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
        	try {
                //&& device.getName().startsWith("BleNetworkNode")
            	if (!TextUtils.isEmpty(device.getName()) ) {
                    if(!mLeDevices.contains(device)) {
                        mLeDevices.add(device);
                        notifyDataSetChanged();
                    }
    			}
			} catch (Exception e) {
			}
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {
            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.listitem_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = (TextView) view.findViewById(R.id.device_address);
                viewHolder.deviceName = (TextView) view.findViewById(R.id.device_name);
                viewHolder.icon = (ImageView)view.findViewById(R.id.icon);
                viewHolder.btnEdit = (ImageView)view.findViewById(R.id.btnEdit);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            MLog.e(device.getName());
            viewHolder.index = Integer.parseInt(device.getName().substring(device.getName().indexOf("_") + 1).trim());
            viewHolder.icon.setImageResource(Mytool.getDevicePicture(viewHolder.index));
            viewHolder.deviceName.setText(Mytool.GetDevceiceName(MainActivity.this,device.getAddress(),viewHolder.index));
            viewHolder.deviceAddress.setText(device.getAddress());

            viewHolder.btnEdit.setTag(viewHolder);
            viewHolder.btnEdit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ViewHolder device = (ViewHolder)v.getTag();
			        if (device == null) return;
		            final Intent intent = new Intent(MainActivity.this, ReNameDevice.class);
		            intent.putExtra(Constants.EXTRAS_DEVICE_NAME, device.deviceName.getText().toString());
		            intent.putExtra(Constants.EXTRAS_DEVICE_ADDRESS, device.deviceAddress.getText().toString());
		            intent.putExtra(Constants.EXTRAS_DEVICE_ID, device.index);
		            if (mScanning) {
		                mBluetoothAdapter.stopLeScan(mLeScanCallback);
		                mScanning = false;
		            }
		            startActivityForResult(intent, REQUEST_RENAME_OK);
				}
			});
            return view;
        }
    }

    // Device scan callback.
    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLeDeviceListAdapter.addDevice(device);
                }
            });
        }
    };

    public  class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
        ImageView icon,btnEdit;
        int index = -1;
    }

}
