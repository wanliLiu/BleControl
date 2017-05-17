package com.blecontrol.util;

import android.content.Context;
import android.text.TextUtils;

import com.blecontrol.R;
import com.blecontrol.sensor.control.BLE_LED;
import com.blecontrol.sensor.control.BLE_Relay;
import com.blecontrol.sensor.control.BLE_dianzheng;
import com.blecontrol.sensor.control.BLE_highled;
import com.blecontrol.sensor.control.BLE_motor;
import com.blecontrol.sensor.grap.Ble_grap_accer;
import com.blecontrol.sensor.grap.Ble_grap_chaoshengbo;
import com.blecontrol.sensor.grap.Ble_grap_gaojinduwenshidu;
import com.blecontrol.sensor.grap.Ble_grap_gas;
import com.blecontrol.sensor.grap.Ble_grap_infared;
import com.blecontrol.sensor.grap.Ble_grap_press;
import com.blecontrol.sensor.grap.Ble_grap_shake;
import com.blecontrol.sensor.grap.Ble_grap_templight;

import java.util.Locale;

public class Mytool {

	/**
	 * 获取设备的名称
	 */
	public static int getDevicePicture(int deviceID) {
		int id = 0;
		switch (deviceID) {
		case 1:
			id = R.drawable.icon_dianzheng;
			break;
		case 2:
			id = R.drawable.icon_led;
			break;
		case 3:
			id = R.drawable.icon_gaojinduwenshidu;
			break;
		case 4:
			id = R.drawable.icon_templight;
			break;
		case 5:
			id = R.drawable.icon_press;
			break;
		case 6:
			id = R.drawable.icon_relay;
			break;
		case 7:
			// Devicename = "雨滴传感器节点";
			break;
		case 8:
			id = R.drawable.icon_infraed;
			// Devicename = "人体红外传感器节点";
			break;
		case 9:
			id = R.drawable.icon_shake;
			break;
		case 10:
			id = R.drawable.icon_accer;
			break;
		case 11:
			id = R.drawable.icon_gas;
			// Devicename = "可燃气体传感器节点";
			break;
		case 12:
			// Devicename = "DAC输出节点";
			break;
		case 13:
			// Devicename = "红外测温传感器节点";
			break;
		case 14:
			id = R.drawable.icon_chaoshengbo;
			// Devicename = "超声波传感器节点";
			break;
		case 15:
			id = R.drawable.icon_motor;
			break;
		case 16:
			// Devicename = "霍尔传感器节点";
			break;
		case 17:
			id = R.drawable.icon_highled;
			break;
		case 18:
			// Devicename = "火焰传感器节点";
			break;
		case 19:
			// Devicename = "玻璃破碎传感器节点";
			break;
		case 20:
			// Devicename = "红外转发传感器节点";
			break;
		case 21:
			// Devicename = "电表传感器节点";
			break;
		case 22:
			// Devicename = "门磁传感器节点";
			break;
		case 23:
			// Devicename = "烟雾传感器节点";
			break;
		default:
			// Devicename = "未知设备";
			break;
		}

		return id;
	}

	/**
	 * 获取设备的名称
	 */
	public static String GetDevceiceName(Context ctx, String MAC, int deviceID) {
		String Devicename = "";

		Devicename = ReNameUtil.getInstance(ctx).getName(ctx, MAC);
		if (!TextUtils.isEmpty(Devicename)) {
			return Devicename;
		}

		switch (deviceID) {
		case 1:
			Devicename = "8*8点阵屏节点";
			break;
		case 2:
			Devicename = "4*LED数码管屏节点";
			break;
		case 3:
			Devicename = "高精温湿度节点";
			break;
		case 4:
			Devicename = "温度、光敏、蜂鸣器节点";
			break;
		case 5:
			Devicename = "压力传感器节点";
			break;
		case 6:
			Devicename = "继电器节点";
			break;
		case 7:
			Devicename = "雨滴传感器节点";
			break;
		case 8:
			Devicename = "人体红外传感器节点";
			break;
		case 9:
			Devicename = "振动/蜂鸣器节点";
			break;
		case 10:
			Devicename = "加速度传感器节点";
			break;
		case 11:
			Devicename = "可燃气体传感器节点";
			break;
		case 12:
			Devicename = "DAC输出节点";
			break;
		case 13:
			Devicename = "红外测温传感器节点";
			break;
		case 14:
			Devicename = "超声波传感器节点";
			break;
		case 15:
			Devicename = "电机模块节点";
			break;
		case 16:
			Devicename = "霍尔传感器节点";
			break;
		case 17:
			Devicename = "高亮LED控制节点";
			break;
		case 18:
			Devicename = "火焰传感器节点";
			break;
		case 19:
			Devicename = "玻璃破碎传感器节点";
			break;
		case 20:
			Devicename = "红外转发传感器节点";
			break;
		case 21:
			Devicename = "电表传感器节点";
			break;
		case 22:
			Devicename = "门磁传感器节点";
			break;
		case 23:
			Devicename = "烟雾传感器节点";
			break;
		default:
			Devicename = "未知设备";
			break;
		}

		return Devicename;
	}

	/**
	 * 获取设备的名称
	 */
	public static Class<?> getClass(int deviceID) {
		Class<?> Devicename = null;
		switch (deviceID) {
		case 1:
			Devicename = BLE_dianzheng.class;
			break;
		case 2:
			Devicename = BLE_LED.class;
			break;
		case 3:
			Devicename = Ble_grap_gaojinduwenshidu.class;
			break;
		case 4:
			Devicename = Ble_grap_templight.class;
			break;
		case 5:
			Devicename = Ble_grap_press.class;
			break;
		case 6:
			Devicename = BLE_Relay.class;
			break;
		case 7:
			break;
		case 8:
			Devicename = Ble_grap_infared.class;
			break;
		case 9:
			Devicename = Ble_grap_shake.class;
			break;
		case 10:
			Devicename = Ble_grap_accer.class;
			break;
		case 11:
			Devicename = Ble_grap_gas.class;
			break;
		case 12:
			break;
		case 13:
			break;
		case 14:
			Devicename = Ble_grap_chaoshengbo.class;
			break;
		case 15:
			Devicename = BLE_motor.class;
			break;
		case 16:
			break;
		case 17:
			Devicename = BLE_highled.class;
			break;
		case 18:
			break;
		case 19:
			break;
		case 20:
			break;
		case 21:
			break;
		case 22:
			break;
		case 23:
			break;
		default:
			break;
		}

		return Devicename;
	}

	// 转换HEX为字符串
	public static String changeHexToString(byte[] buffer, int size) {
		StringBuffer strbuf = new StringBuffer("");
		if (buffer == null || buffer.length <= 0) {
			return null;
		}
		for (int i = 0; i < size; i++) {
			int temp = buffer[i] & 0xFF;
			String strTemp = Integer.toHexString(temp).toUpperCase();
			if (strTemp.length() < 2) {
				strbuf.append(0);
			}
			strbuf.append(strTemp);
		}
		return strbuf.toString();
	}

	public static String changeHexToString(byte[] buffer, int start, int end) {
		StringBuffer strbuf = new StringBuffer("");
		for (int i = start; i < end; i++) {
			int temp = buffer[i] & 0xFF;
			String strTemp = Integer.toHexString(temp);
			if (strTemp.length() < 2) {
				strbuf.append(0);
			}
			strbuf.append(strTemp);
		}
		return strbuf.toString();
	}

	// 转换字符串为HEX
	public static byte[] hexStringToBytes(String hexString) {
		if (hexString == null || hexString.equals("")) {
			return null;
		}
		hexString = hexString.toUpperCase(Locale.getDefault());
		int length = hexString.length() / 2;
		char[] hexChars = hexString.toCharArray();
		byte[] data = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			data[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return data;
	}

	// 获取字符串对应的16进制数
	public static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

}
