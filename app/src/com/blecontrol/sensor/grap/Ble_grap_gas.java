package com.blecontrol.sensor.grap;

import android.bluetooth.BluetoothGattCharacteristic;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
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

public class Ble_grap_gas extends BaseActivity {

	private double GasData;
	private double x = 0;
	private boolean isStartOrNot = false;
	
	// 曲线相关的参数
	private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();
	// 用于 保存点集数据 ，包括每条曲线的X，Y坐标
	private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();
	// 保存折线图名称，坐标轴的名称，坐标轴的起点重点，坐标轴的颜色，坐标轴上数字的颜色
	private XYSeries mCurrentSeries;// 第一个点集合
	private XYMultipleSeriesRenderer mCurrentRenderer;
	// 保存目前折线图名称，坐标轴的名称，坐标轴的起点重点，坐标轴的颜色，坐标轴上数字的颜色
	private String mDateFormat;
	private GraphicalView mChartView;// 用于改变的视图
	
	private ToggleButton mStart;// 开启监控
	private TextView sensor_sh_tv_show;	
	private Button btnSignal;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		isLANDSCAPE = true;
		super.onCreate(savedInstanceState);
	}
	
	@Override
	protected void InitView() {
		setContentView(R.layout.ble_grap_gas);
		
		mStart = (ToggleButton) findViewById(R.id.btn_gas_start);
		sensor_sh_tv_show = (TextView) findViewById(R.id.sensor_tv_gas_show);
		
		btnSignal = (Button)findViewById(R.id.btnsid);
	}

	@Override
	protected void InitListener() {
		
		btnSignal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (characteristic != null) {
					isStartOrNot = true;
					mBluetoothLeService.readCharacteristic(characteristic);
				}else
				{
					MyToast("需要的没有找到");
				}
			}
		});
		
		mStart.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (characteristic != null) {
					if (isChecked) {
						isStartOrNot = true;
						btnSignal.setEnabled(false);
						mBluetoothLeService.setCharacteristicNotification(characteristic, true);
					}else {
						btnSignal.setEnabled(true);
						isStartOrNot = false;
						mBluetoothLeService.setCharacteristicNotification(characteristic, false);
					}
				}else
				{
					MyToast("需要的没有找到");
				}
			}
		});
	}

	@Override
	protected void InitData() {
		setChartSettings();
		initXYSeries();
		mCurrentRenderer = mRenderer;
		mCurrentSeries.add(x, GasData);
		++x;
		if (mChartView != null) {
			mChartView.repaint();// 重画图表
		}
	}
	/*
	 * 设置视图的基本样式 创建视图 renderer
	 */
	protected void setChartSettings() {
		mRenderer.setChartTitle("可燃气体图表");
		mRenderer.setXTitle("时间(单位：s)");
		mRenderer.setYTitle("可燃气体值（单位：PPM）");

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
		mRenderer.setLegendTextSize(25);// 设置曲线显示下面那个的字的大小

		mRenderer.setMargins(new int[] { 40, 70, 20, 30 });// 设置图表的外边框(上/左/下/右)

		mRenderer.setPointSize(5);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		mRenderer.setZoomButtonsVisible(true);// 是否显示放大缩小按钮
		mRenderer.setShowGrid(true);

		mRenderer.setZoomButtonsVisible(true);// 是否显示放大缩小按钮
		mRenderer.setPointSize(3);// 设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
		mRenderer.setShowGrid(true);
		mRenderer.setXAxisMin(0);
		mRenderer.setXAxisMax(50);
		mRenderer.setYAxisMax(100);
		mRenderer.setYAxisMin(0);
	}

	/**
	 * 
	 */
	private void initXYSeries() {

		XYSeries series = new XYSeries("可燃气体");// 定义XYSeries

		mDataset.addSeries(series);// 在XYMultipleSeriesDataset中添加XYSeries

		mCurrentSeries = series;// 设置当前需要操作的XYSeries

		XYSeriesRenderer renderer = new XYSeriesRenderer();// 定义XYSeriesRenderer

		mRenderer.addSeriesRenderer(renderer);// 将单个XYSeriesRenderer增加到XYMultipleSeriesRenderer

		renderer.setPointStyle(PointStyle.SQUARE);// 点的类型是三角
		renderer.setFillPoints(true);// 设置点是否实心
		renderer.setColor(Color.YELLOW);// 设置线条颜色
		renderer.setDisplayChartValues(true);// 显示点的数值
		renderer.setChartValuesTextSize(20);
	}

	@Override
	public void onResume() {
		super.onResume();
		super.onResume();

		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.grap_gas);
			mChartView = ChartFactory.getLineChartView(this, mDataset,mRenderer);
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
						// sensor_sh_tv_show1.setText("没有点击任何点！");
					} else {
						// String showtext = "序列: "
						// + (seriesSelection.getSeriesIndex() == 1 ? "湿度"
						// : "温度") + ",序号"
						// + seriesSelection.getPointIndex() + " ,点击的坐标是："
						// + " X=" + seriesSelection.getXValue() + ", Y="
						// + seriesSelection.getValue();
						// sensor_sh_tv_show1.setText(showtext);

					}
				}
			});
			mChartView.setOnLongClickListener(new View.OnLongClickListener() {
				@Override
				public boolean onLongClick(View v) {
					SeriesSelection seriesSelection = mChartView
							.getCurrentSeriesAndPoint();
					if (seriesSelection == null) {
						// sensor_sh_tv_show1.setText("没有点击任何点！");
						return false;
					} else {
						// String showtext = "序列: "
						// + (seriesSelection.getSeriesIndex() == 1 ? "湿度"
						// : "温度") + ",序号"
						// + seriesSelection.getPointIndex() + " ,点击的坐标是："
						// + " X=" + seriesSelection.getXValue() + ", Y="
						// + seriesSelection.getValue();
						// sensor_sh_tv_show1.setText(showtext);
						return true;
					}
				}
			});
			// 这段代码处理放大缩小
			mChartView.addZoomListener(new ZoomListener() {
				public void zoomApplied(ZoomEvent e) {
					// String type = "out";
					if (e.isZoomIn()) {
						// type = "in";
					}
					// sensor_sh_tv_show1.setText("Zoom " + type + " rate "
					// + e.getZoomRate());
				}

				public void zoomReset() {
					System.out.println("Reset");
				}
			}, true, true);
			// 设置拖动图表时后台打印出图表坐标的最大最小值.
			mChartView.addPanListener(new PanListener() {
				public void panApplied() {
					// sensor_sh_tv_show1.setText("New X range=["
					// + mRenderer.getXAxisMin() + ", "
					// + mRenderer.getXAxisMax() + "], Y range=["
					// + mRenderer.getYAxisMax() + ", "
					// + mRenderer.getYAxisMax() + "]");
				}
			});
			layout.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		} else {
			mChartView.repaint();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		
		if (isStartOrNot) {
			mBluetoothLeService.setCharacteristicNotification(characteristic, false);
		}
	}
	// 恢复状态
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		mDataset = (XYMultipleSeriesDataset) savedInstanceState
				.getSerializable("dataset");
		mRenderer = (XYMultipleSeriesRenderer) savedInstanceState
				.getSerializable("renderer");
		mCurrentSeries = (XYSeries) savedInstanceState
				.getSerializable("current_series");
		mCurrentRenderer = (XYMultipleSeriesRenderer) savedInstanceState
				.getSerializable("current_renderer");
		mDateFormat = savedInstanceState.getString("date_format");
		super.onRestoreInstanceState(savedInstanceState);
	}

	// 保存状态
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putSerializable("dataset", mDataset);
		outState.putSerializable("renderer", mRenderer);
		outState.putSerializable("current_series", mCurrentSeries);
		outState.putSerializable("current_renderer", mCurrentRenderer);
		outState.putString("date_format", mDateFormat);
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void Event_Connect() {
		
	}

	@Override
	protected void Event_Disconnect() {
		
	}

	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(
				UUID.fromString(GattAttributes.GAS_services),
				UUID.fromString(GattAttributes.GAS_data));
	}

	@Override
	protected void Event_HaveData(String data) {
		try {
			if (isStartOrNot) {
				GasData = Integer.valueOf(data, 16);
				mCurrentSeries.add(x, GasData);
				if (x > mRenderer.getXAxisMax()) {
					mRenderer.setXAxisMin(mRenderer.getXAxisMin() + x - mRenderer.getXAxisMax());
					mRenderer.setXAxisMax(x);
				}
				if (mChartView != null) {
					mChartView.repaint();// 重画图表
				}
				sensor_sh_tv_show.setText("当前可燃气体值：" + GasData + "PPM");
				++x;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
