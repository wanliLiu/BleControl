package com.blecontrol.util;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;

/**
 * 设备重命令管理
 * @author qingpeng
 *
 */
public class ReNameUtil {

	private List<UnName> list = new ArrayList<UnName>();
	
	private static ReNameUtil instance = null;
	
	public static ReNameUtil getInstance(Context ctx)
	{
		if (instance == null) {
			instance = new ReNameUtil(ctx);
		}
		
		return instance;
	}
	/**
	 * 
	 * @param ctx
	 */
	public ReNameUtil(Context ctx) {
		SPUtil sp = new SPUtil(ctx, SPUtil.DEVICE_Rename,Context.MODE_APPEND);
		String devices = sp.getValue("UnameDevices", "");
		
		if (!TextUtils.isEmpty(devices)) {
			list = JSON.parseArray(devices, UnName.class);
		}
	}
	/**
	 * 
	 * @param ctx
	 */
	private void init(Context ctx) {
		SPUtil sp = new SPUtil(ctx, SPUtil.DEVICE_Rename,Context.MODE_APPEND);
		String devices = sp.getValue("UnameDevices", "");
		
		if (!TextUtils.isEmpty(devices)) {
			list = JSON.parseArray(devices, UnName.class);
		}
	}
	/**
	 * 
	 * @param ctx
	 * @param MAC
	 * @return
	 */
	public String getName(Context ctx,String MAC)
	{
		try {
			if (list == null || list.isEmpty() || list.size() == 0) {
				init(ctx);
				if (list == null || list.isEmpty() || list.size() == 0) {
					return "";
				}
			}
			
			for (UnName temp : list) {
				if (temp.getMAC().equals(MAC)) {
					return temp.getName();
				}
			}
		} catch (Exception e) {
		}

		return "";
	}
	
	/**
	 * 
	 * @param ctx
	 * @param Name
	 * @param MAC
	 */
	public void setName(Context ctx,String Name,String MAC)
	{
		if (list == null || list.isEmpty() || list.size() == 0) {
			init(ctx);
		}
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getMAC().equals(MAC)) {
					list.get(i).setName(Name);
					SPUtil sp = new SPUtil(ctx, SPUtil.DEVICE_Rename,Context.MODE_APPEND);
					sp.putValue("UnameDevices", JSON.toJSONString(list));
					return;
				}
			}
		}
		
		if (list == null) {
			list = new ArrayList<UnName>();
		}
		
		UnName temp = new UnName();
		temp.setMAC(MAC);
		temp.setName(Name);
		list.add(temp);
		
		SPUtil sp = new SPUtil(ctx, SPUtil.DEVICE_Rename,Context.MODE_APPEND);
		sp.putValue("UnameDevices", JSON.toJSONString(list));
		
	}
	/**
	 * 
	 * @param ctx
	 * @param MAC
	 */
	public void removeName(Context ctx,String MAC)
	{
		if (list == null || list.isEmpty() || list.size() == 0) {
			init(ctx);
		}
		
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getMAC().equals(MAC)) {
					list.remove(i);
					SPUtil sp = new SPUtil(ctx, SPUtil.DEVICE_Rename,Context.MODE_APPEND);
					sp.putValue("UnameDevices", JSON.toJSONString(list));
					return;
				}
			}
		}
	}
}
