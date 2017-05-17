package com.blecontrol.sensor.grap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.blecontrol.R;
import com.blecontrol.base.BaseActivity;
import com.blecontrol.base.GattAttributes;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.tools.PanListener;
import org.achartengine.tools.ZoomEvent;
import org.achartengine.tools.ZoomListener;

import java.util.UUID;

public class Ble_grap_templight extends BaseActivity {

	private int Tempure;
	private int GuangMi;
	private double x = 0;
	
	// 曲线相关的参数
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// 用于 保存点集数据 ，包括每条曲线的X，Y坐标
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	// 保存折线图名称，坐标轴的名称，坐标轴的起点重点，坐标轴的颜色，坐标轴上数字的颜色
	private XYSeries mCurrentSeries1;// 第一个点集合
	private XYSeries mCurrentSeries2;// 第二个点集合
	private XYMultipleSeriesRenderer mCurrentRenderer;
	// 保存目前折线图名称，坐标轴的名称，坐标轴的起点重点，坐标轴的颜色，坐标轴上数字的颜色
	private String mDateFormat;
	private GraphicalView mChartView;// 用于改变的视图
	
	private ToggleButton mStart,mspeaker;
	private Button mTempSet,mLightSet;// 开启监控
	private TextView sensor_sh_tv_show;	
	private EditText setTemp,setLight;
	
	/** 键盘输入键按钮 */
	private Button btn_num0;
	private Button btn_num1;
	private Button btn_num2;
	private Button btn_num3;
	private Button btn_num4;
	private Button btn_num5;
	private Button btn_num6;
	private Button btn_num7;
	private Button btn_num8;
	private Button btn_num9;
	/** 删除键盘的按钮 */
	private Button btn_delete;
	/** 清除按钮输入的值 */
	private Button btn_clear;
	/** 显示的值的文本框 */
	private EditText tv_show;
	
	private Button mSure,mCancle;
	
	private PopupWindow pW = null;
	
	private boolean isStartOrNot = false;
	
	private BluetoothGattCharacteristic beepCharacteristic,setTempCharacteristic,setLightCharacteristic;
	
	private static final int set_Temp = 32;
	private static final int set_Light = 12;
	private int isSetMode = 0;
			
	private Button btnSignal;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		isLANDSCAPE = true;
	    super.onCreate(savedInstanceState);
	}

	@Override
	protected void InitView() {
		setContentView(R.layout.ble_grap_templight);
		
		mTempSet = (Button)findViewById(R.id.btn_templight_setTem);
		mLightSet = (Button)findViewById(R.id.btn_templight_setgua);
		
		mStart = (ToggleButton) findViewById(R.id.btn_templight_start);
		sensor_sh_tv_show = (TextView) findViewById(R.id.sensor_tv_templight_show);
		mspeaker = (ToggleButton)findViewById(R.id.btn_templight_speak);
		
		btnSignal = (Button)findViewById(R.id.btnSignal);
	}

	@Override
	protected void InitListener() {
		
		btnSignal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (characteristic != null) {
					isSetMode = 0;
					isStartOrNot = true;
					mBluetoothLeService.readCharacteristic(characteristic);
				}else
				{
					MyToast("需要的没有找到");
				}

			}
		});
		
	    mTempSet.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (setTempCharacteristic != null) {
					isSetMode = set_Temp;
					mBluetoothLeService.readCharacteristic(setTempCharacteristic);
				}else {
					MyToast("需要的没有找到");
				}
				return true;
			}
		});		
	    mTempSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					int temp = Integer.parseInt(setTemp.getText().toString());
					if (temp >= 0 & temp < 80) {
				        if (setTempCharacteristic != null)
				        {
				        	setTempCharacteristic.setValue(temp, BluetoothGattCharacteristic.FORMAT_UINT8,0);
				            mBluetoothLeService.writeCharacteristic(setTempCharacteristic);
				        }else
				        {
				        	MyToast("需要的没有找到");
				        }
					}else {
						Toast.makeText(getApplicationContext(), "请输入0到80之间的数值", Toast.LENGTH_SHORT).show();
					}					
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "对不起，你没有输入，请输入！", Toast.LENGTH_SHORT).show();
				}

			}
		});
	    
	    mLightSet.setOnLongClickListener(new View.OnLongClickListener() {
			
			@Override
			public boolean onLongClick(View v) {
				if (setLightCharacteristic != null) {
					isSetMode = set_Light;
					mBluetoothLeService.readCharacteristic(setLightCharacteristic);
				}else {
					MyToast("需要的没有找到");
				}
				return true;
			}
		});	
	    mLightSet.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				try {
					int temp = Integer.parseInt(setLight.getText().toString());
					if (temp >= 0 & temp < 255) {
				        if (setLightCharacteristic != null)
				        {
				        	setLightCharacteristic.setValue(temp, BluetoothGattCharacteristic.FORMAT_UINT8,0);
				            mBluetoothLeService.writeCharacteristic(setLightCharacteristic);
				        }else
				        {
				        	MyToast("需要的没有找到");
				        }
					}else {
						Toast.makeText(getApplicationContext(), "请输入0到255之间的数值", Toast.LENGTH_SHORT).show();
					}					
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(), "对不起，你没有输入，请输入！", Toast.LENGTH_SHORT).show();
				}
			}
		});	    
	    
	    setTemp = (EditText)findViewById(R.id.edit_templight_temp);
	    setTemp.setInputType(InputType.TYPE_NULL);
	    setTemp.setSelected(false);
	    setTemp.clearFocus();
	    setTemp.setOnClickListener(new onclicklistener(Ble_grap_templight.this, R.id.edit_templight_temp,setTemp));
	    setLight = (EditText)findViewById(R.id.edit_templight_guan);
	    setLight.setInputType(InputType.TYPE_NULL);
	    setLight.setSelected(false);
	    setLight.clearFocus();
	    setLight.setOnClickListener(new onclicklistener(Ble_grap_templight.this, R.id.edit_templight_guan,setLight));
	    
		mStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (characteristic != null) {
					if (isChecked) {
						if (!mspeaker.isChecked()) {
							Enable(false);
							isStartOrNot = true;
							mBluetoothLeService.setCharacteristicNotification(characteristic, true);
						}else {
							mStart.setChecked(false);
							Toast.makeText(getApplicationContext(), "请先关闭蜂鸣器后再开始测试！", Toast.LENGTH_SHORT).show();
						}
					} else {
						if (isStartOrNot) {
							Enable(true);
							isStartOrNot = false;
							mBluetoothLeService.setCharacteristicNotification(characteristic, false);
						}	
					}
				}else {
					MyToast("要的没有找到");
				}
			}
		});	
		
		mspeaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (beepCharacteristic != null) {
					if (isChecked) {
						beepCharacteristic.setValue(0x01, BluetoothGattCharacteristic.FORMAT_UINT8,0);
					}else {
						beepCharacteristic.setValue(0x00, BluetoothGattCharacteristic.FORMAT_UINT8,0);
					}
					mBluetoothLeService.writeCharacteristic(beepCharacteristic);
				}else {
					MyToast("需要的没有找到");
				}
			}
		});	
	}
	@Override
	protected void onPause() {
		super.onPause();
		
		if (isStartOrNot) {
			mBluetoothLeService.setCharacteristicNotification(characteristic, false);
		}
	}
	@Override
	protected void InitData() {
		setChartSettings();
		initXYSeries();
		mCurrentRenderer = mRenderer;
		mCurrentSeries1.add(x, Tempure);
		mCurrentSeries2.add(x, GuangMi);
		++x;		
		if (mChartView != null) {
			mChartView.repaint();// 重画图表
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
		beepCharacteristic = mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.TempLight_Services),UUID.fromString(GattAttributes.beep));
		setTempCharacteristic = mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.TempLight_Services),UUID.fromString(GattAttributes.Set_Temp));
		setLightCharacteristic = mBluetoothLeService.getWeNeed(UUID.fromString(GattAttributes.TempLight_Services),UUID.fromString(GattAttributes.Set_Light));
		
		return mBluetoothLeService.getWeNeed(
				UUID.fromString(GattAttributes.TempLight_Services),
				UUID.fromString(GattAttributes.TempLight_Data));
	}
	@Override
	protected void Event_HaveData(String data) {
		try {
			if (isSetMode == 0) {
				if (isStartOrNot) {
					GuangMi = Integer.parseInt(data.substring(0, 2), 16);
					Tempure = Integer.parseInt(data.substring(2, 4), 16);
					
//					if (GuangMi == 0 || Tempure == 0 ) {
//						return;
//					}
					mCurrentSeries1.add(x, Tempure);
					mCurrentSeries2.add(x, GuangMi);
					if(x>mRenderer.getXAxisMax())
					{
						mRenderer.setXAxisMin(mRenderer.getXAxisMin()+x-mRenderer.getXAxisMax());
						mRenderer.setXAxisMax(x);				
					}
					if (mChartView != null) {
						mChartView.repaint();// 重画图表
					}
					sensor_sh_tv_show.setText("当前温度：" + Tempure + "°C     " + "当前光敏值为："
							+ GuangMi);		
					++x;							
				}
			}else if (isSetMode == set_Temp) {
				isSetMode = 0;
				setTemp.setText(Integer.parseInt(data, 16) + "");
			}else if (isSetMode == set_Light) {
				isSetMode = 0;
				setLight.setText(Integer.parseInt(data, 16) + "");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 使能关闭
	 */
	private void Enable(boolean enable){
		mTempSet.setEnabled(enable);
		setTemp.setEnabled(enable);
		mLightSet.setEnabled(enable);
		setLight.setEnabled(enable);
		mspeaker.setEnabled(enable);
		btnSignal.setEnabled(enable);
		if(!enable){
			mTempSet.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_disable));
			mLightSet.setBackgroundDrawable(getResources().getDrawable(R.drawable.btn_disable));
		}else {
			mTempSet.setBackgroundResource(R.drawable.btn_select);
			mLightSet.setBackgroundResource(R.drawable.btn_select);
		}
	}
	/*
	 * 设置视图的基本样式 创建视图 renderer
	 */
	protected void setChartSettings() {
		mRenderer.setChartTitle("温度和光敏图表");
		mRenderer.setXTitle("时间(单位：s)");
		mRenderer.setYTitle("温度和光敏（单位：°C）");
		
		mRenderer.setAxesColor(Color.BLACK);
		mRenderer.setApplyBackgroundColor(true);// 设置是否显示背景色
		mRenderer.setBackgroundColor(Color.GRAY);
		mRenderer.setMarginsColor(Color.GRAY);
		mRenderer.setLabelsColor(Color.BLACK);
		mRenderer.setXLabelsColor(Color.BLACK);
		mRenderer.setYLabelsColor(0, Color.BLACK);
		mRenderer.setYLabelsAlign(Align.RIGHT);
		
		mRenderer.setAxisTitleTextSize(25); // 设置XY轴标题文字的大小
		mRenderer.setChartTitleTextSize(30);// 设置这个图表标题文字大小
		mRenderer.setLabelsTextSize(25);// 设置XY轴的文字大小
		mRenderer.setLegendTextSize(25);//设置曲线显示下面那个的字的大小
		
		mRenderer.setMargins(new int[] { 40, 70, 20, 30 });// 设置图表的外边框(上/左/下/右)
		
		mRenderer.setPointSize(5);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		mRenderer.setZoomButtonsVisible(true);// 是否显示放大缩小按钮
		mRenderer.setShowGrid(true);
		
		mRenderer.setXAxisMin(0);
		mRenderer.setXAxisMax(50);
		mRenderer.setYAxisMax(30);
		mRenderer.setYAxisMin(0);

	}	
	/**
	 * 
	 */
	private void initXYSeries() {
		String[] seriesTitles = { "温度", "光敏" };// 图例
		PointStyle[] style = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND };// 设置节点类型
		int[] color = new int[] { Color.RED, Color.GREEN };// 设置颜色

		XYSeries series1 = new XYSeries(seriesTitles[0]);// 定义XYSeries
		XYSeries series2 = new XYSeries(seriesTitles[1]);// 定义XYSeries

		mDataset.addSeries(series1);// 在XYMultipleSeriesDataset中添加XYSeries
		mDataset.addSeries(series2);// 在XYMultipleSeriesDataset中添加XYSeries

		mCurrentSeries1 = series1;// 设置当前需要操作的XYSeries
		mCurrentSeries2 = series2;// 设置当前需要操作的XYSeries

		XYSeriesRenderer renderer1 = new XYSeriesRenderer();// 定义XYSeriesRenderer
		XYSeriesRenderer renderer2 = new XYSeriesRenderer();// 定义XYSeriesRenderer

		mRenderer.addSeriesRenderer(renderer1);// 将单个XYSeriesRenderer增加到XYMultipleSeriesRenderer
		mRenderer.addSeriesRenderer(renderer2);// 将单个XYSeriesRenderer增加到XYMultipleSeriesRenderer

		renderer1.setPointStyle(style[0]);// 点的类型是三角
		renderer1.setFillPoints(true);// 设置点是否实心
		renderer1.setColor(color[0]);// 设置线条颜色
		renderer1.setDisplayChartValues(true);// 显示点的数值
		renderer1.setChartValuesTextSize(20);
		
		renderer2.setPointStyle(style[1]);// 点的类型是三角
		renderer2.setFillPoints(true);// 设置点是否实心
		renderer2.setColor(color[1]);
		renderer2.setDisplayChartValues(true);
		renderer2.setChartValuesTextSize(20);
	}
	/*
	 * 根据线条的条数创建线条注解 colors 颜色名称集合 styles 电的样式集合 fill 是否为空心
	 */
	protected XYMultipleSeriesRenderer buildRenderer(int[] colors,
			PointStyle[] styles, boolean fill) {
		XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
		int length = colors.length;
		for (int i = 0; i < length; i++) {
			XYSeriesRenderer r = new XYSeriesRenderer();
			r.setColor(colors[i]);
			r.setPointStyle(styles[i]);
			r.setFillPoints(fill);
			renderer.addSeriesRenderer(r);
		}
		return renderer;
	}
	
		// 恢复状态
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		mDataset = (XYMultipleSeriesDataset) savedInstanceState
				.getSerializable("dataset");
		mRenderer = (XYMultipleSeriesRenderer) savedInstanceState
				.getSerializable("renderer");
		mCurrentSeries1 = (XYSeries) savedInstanceState
				.getSerializable("current_series1");
		mCurrentSeries2 = (XYSeries) savedInstanceState
				.getSerializable("current_series2");
		mCurrentRenderer = (XYMultipleSeriesRenderer) savedInstanceState
				.getSerializable("current_renderer");
		mDateFormat = savedInstanceState.getString("date_format");
		super.onRestoreInstanceState(savedInstanceState);
	}

	// 保存状态
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putSerializable("dataset", mDataset);
		outState.putSerializable("renderer", mRenderer);
		outState.putSerializable("current_series1", mCurrentSeries1);
		outState.putSerializable("current_series2", mCurrentSeries2);
		outState.putSerializable("current_renderer1", mCurrentRenderer);
		outState.putString("date_format", mDateFormat);
		super.onSaveInstanceState(outState);
	}	
	@Override
	public void onResume(){
		super.onResume();
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.grap_templight);
			mChartView = ChartFactory.getLineChartView(this, mDataset,
					mRenderer);
			mRenderer.setClickEnabled(true);// 设置图表是否允许点击
			mRenderer.setSelectableBuffer(100);// 设置点的缓冲半径值(在某点附件点击时,多大范围内都算点击这个点)
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 这段代码处理点击一个点后,获得所点击的点在哪个序列中以及点的坐标.
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					// double[] xy = mChartView.toRealPoint(0);
					if (seriesSelection == null) {
//						sensor_sh_tv_show1.setText("没有点击任何点！");
					} else {
//						String showtext = "序列: "
//								+ (seriesSelection.getSeriesIndex() == 1 ? "湿度"
//										: "温度") + ",序号"
//								+ seriesSelection.getPointIndex() + " ,点击的坐标是："
//								+ " X=" + seriesSelection.getXValue() + ", Y="
//								+ seriesSelection.getValue();
//						sensor_sh_tv_show1.setText(showtext);

					}
				}
			});
			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
//						sensor_sh_tv_show1.setText("没有点击任何点！");
						return false;
					} else {
//						String showtext = "序列: "
//								+ (seriesSelection.getSeriesIndex() == 1 ? "湿度"
//										: "温度") + ",序号"
//								+ seriesSelection.getPointIndex() + " ,点击的坐标是："
//								+ " X=" + seriesSelection.getXValue() + ", Y="
//								+ seriesSelection.getValue();
//						sensor_sh_tv_show1.setText(showtext);
						return true;
					}
				}
			});
			// 这段代码处理放大缩小
			mChartView.addZoomListener(new ZoomListener() {
				public void zoomApplied(ZoomEvent e) {
//					String type = "out";
					if (e.isZoomIn()) {
//						type = "in";
					}
//					sensor_sh_tv_show1.setText("Zoom " + type + " rate "
//							+ e.getZoomRate());
				}

				public void zoomReset() {
					System.out.println("Reset");
				}
			}, true, true);
			// 设置拖动图表时后台打印出图表坐标的最大最小值.
			mChartView.addPanListener(new PanListener() {
				public void panApplied() {
//					sensor_sh_tv_show1.setText("New X range=["
//							+ mRenderer.getXAxisMin() + ", "
//							+ mRenderer.getXAxisMax() + "], Y range=["
//							+ mRenderer.getYAxisMax() + ", "
//							+ mRenderer.getYAxisMax() + "]");
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
		}
	}
	private class onclicklistener implements OnClickListener{

		private Context mContext;
		private EditText input;
		private View parentView;
		public onclicklistener(Context context, int mid,EditText inputText){
			
			this.mContext = context;
			this.parentView = Ble_grap_templight.this.findViewById(mid);
			this.input = inputText;
		}
		@Override
		public void onClick(View v1) {
			LayoutInflater layout = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View view = layout.inflate(R.layout.keyboard, null);
			btn_num0 = (Button) view.findViewById(R.id.keyboard_num0);
			btn_num1 = (Button) view.findViewById(R.id.keyboard_num1);
			btn_num2 = (Button) view.findViewById(R.id.keyboard_num2);
			btn_num3 = (Button) view.findViewById(R.id.keyboard_num3);
			btn_num4 = (Button) view.findViewById(R.id.keyboard_num4);
			btn_num5 = (Button) view.findViewById(R.id.keyboard_num5);
			btn_num6 = (Button) view.findViewById(R.id.keyboard_num6);
			btn_num7 = (Button) view.findViewById(R.id.keyboard_num7);
			btn_num8 = (Button) view.findViewById(R.id.keyboard_num8);
			btn_num9 = (Button) view.findViewById(R.id.keyboard_num9);
			btn_clear = (Button) view.findViewById(R.id.keyboard_clear);
			btn_delete = (Button) view.findViewById(R.id.keyboard_delete);
			tv_show = (EditText) view.findViewById(R.id.keyboard_tv);
			tv_show.setInputType(InputType.TYPE_NULL);
			tv_show.setSelected(false);
			tv_show.clearFocus();
			mSure = (Button)view.findViewById(R.id.btn_input_sure);
			mCancle = (Button)view.findViewById(R.id.btn_input_cancle);

			btn_num0.setOnClickListener(key_onclick);
			btn_num1.setOnClickListener(key_onclick);
			btn_num2.setOnClickListener(key_onclick);
			btn_num3.setOnClickListener(key_onclick);
			btn_num4.setOnClickListener(key_onclick);
			btn_num5.setOnClickListener(key_onclick);
			btn_num6.setOnClickListener(key_onclick);
			btn_num7.setOnClickListener(key_onclick);
			btn_num8.setOnClickListener(key_onclick);
			btn_num9.setOnClickListener(key_onclick);
			btn_clear.setOnClickListener(key_onclick);
			btn_delete.setOnClickListener(key_onclick);
			mSure.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (!tv_show.getText().toString().equals("")) {
						input.setText(tv_show.getText());
					}
					pW.dismiss();
				}
			});
			mCancle.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					pW.dismiss();
					input.setText("点击输入");
				}
			});
			
//			pW= new PopupWindow(view,365,395,true);
			pW = new PopupWindow(view, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
			//	以下这行加上去后就可以使用BACK键关闭POPWINDOW
//			pW.setBackgroundDrawable(new ColorDrawable(0xb0000000));
			pW.setBackgroundDrawable(new ColorDrawable(0x00000000));	
			pW.setOutsideTouchable(false);
			pW.setAnimationStyle(R.style.FromRightAnimation);//从右进入
//			pW.setAnimationStyle(android.R.style.Animation_Toast);
//			pW.setAnimationStyle(R.style.PopupAnimation);
	        int[] location = new int[2];
	        parentView.getLocationOnScreen(location);			
			pW.showAtLocation(parentView, Gravity.NO_GRAVITY,location[0] - pW.getWidth() + 50, location[1] - pW.getHeight() + 50);
			pW.update();			
	   }
	}
	OnClickListener key_onclick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.keyboard_num0:
				tv_show.setText(tv_show.getText() + "0");
				break;
			case R.id.keyboard_num1:
				tv_show.setText(tv_show.getText() + "1");
				break;
			case R.id.keyboard_num2:
				tv_show.setText(tv_show.getText() + "2");
				break;
			case R.id.keyboard_num3:
				tv_show.setText(tv_show.getText() + "3");
				break;
			case R.id.keyboard_num4:
				tv_show.setText(tv_show.getText() + "4");
				break;
			case R.id.keyboard_num5:
				tv_show.setText(tv_show.getText() + "5");
				break;
			case R.id.keyboard_num6:
				tv_show.setText(tv_show.getText() + "6");
				break;
			case R.id.keyboard_num7:
				tv_show.setText(tv_show.getText() + "7");
				break;
			case R.id.keyboard_num8:
				tv_show.setText(tv_show.getText() + "8");
				break;
			case R.id.keyboard_num9:
				tv_show.setText(tv_show.getText() + "9");
				break;
			case R.id.keyboard_delete:
				String text = tv_show.getText().toString();
				if (text.length() != 0)
					tv_show.setText(text.substring(0, text.length() - 1));
				break;
			case R.id.keyboard_clear:
				tv_show.setText("");
				break;
			default:
				break;
			}
		}
	};	
}
