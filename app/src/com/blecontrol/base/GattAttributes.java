package com.blecontrol.base;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class GattAttributes {
	
//    private static HashMap<String, String> attributes = new HashMap();
    
    //notfication
    public static String CLIENT_CHARACTERISTIC_CONFIG = "00002902-0000-1000-8000-00805f9b34fb";
    
    //点阵屏
    public static String dianzheng_Services = "00001003-0000-1000-8000-00805f9b34fb";
    public static String dianzheng_LEDData = "00002003-0000-1000-8000-00805f9b34fb";
    //数码管
    public static String LED_Services = "00001002-0000-1000-8000-00805f9b34fb";
    public static String LED_LEDData = "00002002-0000-1000-8000-00805f9b34fb";
    //高精度温湿度
    public static String GaoJinDu_Services = "00001005-0000-1000-8000-00805f9b34fb";
    public static String GaoJinDu_Data = "00002005-0000-1000-8000-00805f9b34fb";
    //继电器
    public static String Relay_Services = "00001001-0000-1000-8000-00805f9b34fb";
    public static String Relay_Data = "00002001-0000-1000-8000-00805f9b34fb";  
    //电机
    public static String Motor_Services = "00001009-0000-1000-8000-00805f9b34fb";
    public static String Motor_Data = "00002009-0000-1000-8000-00805f9b34fb";  
    //高亮LED灯
    public static String HighLigt_Services = "00001004-0000-1000-8000-00805f9b34fb";
    public static String HighLigt_Data = "00002004-0000-1000-8000-00805f9b34fb";     
    //三维加速度传感器
    public static String Accer_Services = "00001006-0000-1000-8000-00805f9b34fb";
    public static String Accer_Data = "00002006-0000-1000-8000-00805f9b34fb";     
    //压力传感器
    public static String Press_Services = "0000100C-0000-1000-8000-00805f9b34fb";
    public static String Press_Data = "0000200C-0000-1000-8000-00805f9b34fb";   
    //温度 光敏 蜂鸣器传感器
    public static String TempLight_Services = "00001007-0000-1000-8000-00805f9b34fb";
    //温度和光敏
    public static String TempLight_Data = "00002007-0000-1000-8000-00805f9b34fb";  
    //设置温度的阀值
    public static String Set_Temp = "00004007-0000-1000-8000-00805f9b34fb";
    //设置光敏的阀值
    public static String Set_Light = "00004050-0000-1000-8000-00805f9b34fb";
    //蜂鸣器
    public static String beep = "00005007-0000-1000-8000-00805f9b34fb";
    //振动传感器
    public static String Shake_Services = "00001008-0000-1000-8000-00805f9b34fb";
    public static String Shake_Data = "00003008-0000-1000-8000-00805f9b34fb";
    public static String Shake_beep = "00002008-0000-1000-8000-00805f9b34fb";
    
    //超声波传感器
    public static final String ChaoShengBo_services="00001007-0000-1000-8000-00805f9b34fb";
    public static final String ChaoShengBo_data="00002007-0000-1000-8000-00805f9b34fb";
    
    //超声波传感器
    public static final String Infrared_services="0000100f-0000-1000-8000-00805f9b34fb";
    public static final String Infrared_data="0000200f-0000-1000-8000-00805f9b34fb";
    
    //气体传感器
    public static final String GAS_services="0000100a-0000-1000-8000-00805f9b34fb";
    public static final String GAS_data="0000200a-0000-1000-8000-00805f9b34fb";
    
//    static {
//        attributes.put(dianzheng_Services, "数码管控制服务");
//        attributes.put(dianzheng_LEDData, "数码管");
//    }
//
//    public static String lookup(String uuid, String defaultName) {
//        String name = attributes.get(uuid);
//        return name == null ? defaultName : name;
//    }
    
    
}

