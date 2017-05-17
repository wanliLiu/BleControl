package com.blecontrol.util;

import android.util.Log;

public class MLog {

	/**
	 * 是否开启全局打印日志
	 */
	public static final boolean EnDebug = true;

	private static final String DEFAULT_TAG = "debug";

	public static void i(String TAG, String msg) {
		if (EnDebug)
			Log.i(TAG, msg);
	}

	public static void e(String TAG, String msg) {
		if (EnDebug)
			Log.e(TAG, msg);
	}

	public static void d(String TAG, String msg) {
		if (EnDebug)
			Log.d(TAG, msg);
	}

	public static void v(String TAG, String msg) {
		if (EnDebug)
			Log.v(TAG, msg);
	}

	public static void w(String TAG, String msg) {
		if (EnDebug)
			Log.w(TAG, msg);
	}

	public static void i(String msg) {
		if (EnDebug)
			Log.i(DEFAULT_TAG, msg);
	}

	public static void e(String msg) {
		if (EnDebug)
			Log.e(DEFAULT_TAG, msg);
	}

	public static void d(String msg) {
		if (EnDebug)
			Log.d(DEFAULT_TAG, msg);
	}

	public static void v(String msg) {
		if (EnDebug)
			Log.v(DEFAULT_TAG, msg);
	}

	public static void w(String msg) {
		if (EnDebug)
			Log.w(DEFAULT_TAG, msg);
	}

}
