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

import java.math.BigDecimal;
import java.util.UUID;

public class Ble_grap_gaojinduwenshidu extends BaseActivity {

	private boolean isStartOrNot = false;
	
	private double Wendu;
	private double shidu;
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
		setContentView(R.layout.ble_grap_gaojinduwenshidu);
		
		mStart = (ToggleButton) findViewById(R.id.btn_gaojindu_start);
		sensor_sh_tv_show = (TextView) findViewById(R.id.sensor_tv_gaojindu_show);
		
		btnSignal = (Button)findViewById(R.id.btnSignal);
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
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
		mCurrentSeries1.add(x, Wendu);
		mCurrentSeries2.add(x, shidu);
		++x;		
		if (mChartView != null) {
			mChartView.repaint();// 重画图表
		}	 
		
	}
	/*
	 * 设置视图的基本样式 创建视图 renderer
	 */
	protected void setChartSettings() {
		mRenderer.setChartTitle("高精温湿度图表");
		mRenderer.setXTitle("时间(单位：s)");
		mRenderer.setYTitle("温度和湿度（单位：°C，%）");
		
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
		mRenderer.setYAxisMax(80);
		mRenderer.setYAxisMin(0);

		
	}	
	/**
	 * 
	 */
	private void initXYSeries() {
		String[] seriesTitles = { "温度", "湿度" };// 图例
		PointStyle[] style = new PointStyle[] { PointStyle.TRIANGLE,
				PointStyle.SQUARE};// 设置节点类型
		int[] color = new int[] { Color.BLUE, Color.GREEN };// 设置颜色

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
	protected void Event_Connect() {

	}

	@Override
	protected void Event_Disconnect() {

	}

	@Override
	protected BluetoothGattCharacteristic Event_ServicesFind() {
		return mBluetoothLeService.getWeNeed(
				UUID.fromString(GattAttributes.GaoJinDu_Services),
				UUID.fromString(GattAttributes.GaoJinDu_Data));
	}

	/**
	 * 0405F415
	 * 15F4 0504
	 */
	@Override
	protected void Event_HaveData(String data) {
		try {
			if (isStartOrNot) {
				data = data.substring(6, 8) + data.substring(4, 6) + data.substring(2, 4) + data.substring(0, 2);
				Wendu = Integer.parseInt(data.substring(0, 4), 16) / 100 - 39.6;
				Wendu = Double.valueOf(String.valueOf(new BigDecimal(Wendu).setScale(1, BigDecimal.ROUND_HALF_UP)));
				int temp = Integer.parseInt(data.substring(4, 8), 16);
				shidu = temp * 0.0405 - 4 - temp * temp * 2.8 * 0.000001;
				shidu = (double)Math.round(shidu * 10)/10;
				mCurrentSeries1.add(x, Wendu);
				mCurrentSeries2.add(x, shidu);
				if(x > mRenderer.getXAxisMax())
				{
					mRenderer.setXAxisMin(mRenderer.getXAxisMin()+x-mRenderer.getXAxisMax());
					mRenderer.setXAxisMax(x);				
				}
				if (mChartView != null) {
					mChartView.repaint();// 重画图表
				}
				sensor_sh_tv_show.setText("当前温度：" + Wendu + "°C     " + "当前湿度："+ shidu + "%");		
				++x;
//				x += 10;
			}
		} catch (Exception e) {
		}
	}
	@Override
	public void onResume(){
		super.onResume();
		
		if (mChartView == null) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.grap_gaojindu);
			mChartView = ChartFactory.getLineChartView(this, mDataset,mRenderer);
			mRenderer.setClickEnabled(true);// 设置图表是否允许点击
			mRenderer.setSelectableBuffer(100);// 设置点的缓冲半径值(在某点附件点击时,多大范围内都算点击这个点)
			mChartView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// 这段代码处理点击一个点后,获得所点击的点在哪个序列中以及点的坐标.
					SeriesSelection seriesSelection = mChartView.getCurrentSeriesAndPoint();
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
}
