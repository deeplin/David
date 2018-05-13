package com.david.incubator.ui.menu.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.apkfuns.logutils.LogUtils;
import com.david.common.util.AutoUtil;
import com.david.common.util.Constant;

import org.xclcharts.chart.CustomLineData;
import org.xclcharts.chart.LineChart;
import org.xclcharts.chart.LineData;
import org.xclcharts.common.DensityUtil;
import org.xclcharts.renderer.XEnum;
import org.xclcharts.renderer.axis.DataAxis;
import org.xclcharts.view.ChartView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * author: Ling Lin
 * created on: 2018/1/1 10:46
 * email: 10525677@qq.com
 * description:
 */
public class SensorChartView extends ChartView {

    protected final LineChart chart = new LineChart();
    protected final List<CustomLineData> customLineData = new LinkedList<>();
    private final List<String> xAxisLabels = new ArrayList<>();
    private final List<LineData> chartData = new ArrayList<>();

    public SensorChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置绘图区默认缩进px值,留置空间显示Axis,Axis title....
        int[] ltrb = getBarLnDefaultSpadding();
        chart.setPadding(ltrb[0], ltrb[1], ltrb[2], ltrb[3]);
//        chart.getPlotGrid().showHorizontalLines();
        chart.showDyLine();
        chart.setAxesClosed(false);

        //数据源
        chart.setCategories(xAxisLabels);

        chart.setDataSource(chartData);

        chart.setCustomLines(customLineData);

        //定义数据轴标签显示格式
        chart.getDataAxis().setLabelFormatter(value -> {
            Double tmp = Double.parseDouble(value);
            DecimalFormat df = new DecimalFormat("#0");
            return (df.format(tmp));
        });

        //标签旋转45度
//        chart.getCategoryAxis().setTickLabelRotateAngle(45f);
        chart.getCategoryAxis().getTickLabelPaint().setTextSize(20);

        //设定格式
        chart.setItemLabelFormatter(value -> {
            DecimalFormat df = new DecimalFormat("#0.0");
            return df.format(value);
        });

        //禁用平移模式
        chart.disablePanMode();
        //提高性能
        chart.disableHighPrecision();

        //柱形和标签居中方式
        chart.setBarCenterStyle(XEnum.BarCenterStyle.TICKMARKS);

        chart.getDataAxis().setAxisLineStyle(XEnum.AxisLineStyle.FILLCAP);
        chart.getCategoryAxis().setAxisLineStyle(XEnum.AxisLineStyle.FILLCAP);

        chart.setLineAxisIntersectVisible(false);
    }

    public void resetXAxis() {
        xAxisLabels.clear();
    }

    public void addXAxisLabel(String label) {
        xAxisLabels.add(label);
    }

    public void setYAxisLabels(double max, double min, double step, double detailStep) {
        DataAxis dataAxis = chart.getDataAxis();
        dataAxis.setAxisMax(max);
        dataAxis.setAxisMin(min);
        dataAxis.setAxisSteps(step);
        //指隔多少个轴刻度(即细刻度)后为主刻度
        dataAxis.setDetailModeSteps(detailStep);
        dataAxis.hideFirstTick();
    }

    public void addDataSet(List<Double> dataSeries, int color) {
        LineData lineData = new LineData("", dataSeries, color);
        lineData.setDotStyle(XEnum.DotStyle.HIDE);
        lineData.getLinePaint().setStrokeWidth(2);
        chartData.add(lineData);
    }

    public void clearAllDataSet() {
        chartData.clear();
        customLineData.clear();
    }

    /**
     * 期望线/分界线
     */
    public void setObjectiveLines(double objective) {
        customLineData.clear();
        if (objective >= 0) {
            CustomLineData customLineData = new CustomLineData(
                    "", objective, Constant.AXIS_COLOR, 2);
            customLineData.setLineStyle(XEnum.LineStyle.DASH);
            this.customLineData.add(customLineData);
        }
    }

    protected int getLeftOffset() {
        return 60;
    }

    //偏移出来的空间用于显示tick,axistitle....
    protected int[] getBarLnDefaultSpadding() {
        int[] ltrb = new int[4];
        ltrb[0] = DensityUtil.dip2px(getContext(), getLeftOffset()); //left
        ltrb[1] = DensityUtil.dip2px(getContext(), (int) (50 * AutoUtil.heightRadio + 0.5f)); //top
        ltrb[2] = DensityUtil.dip2px(getContext(), 44); //right
        ltrb[3] = DensityUtil.dip2px(getContext(), (int) (50 * AutoUtil.heightRadio + 0.5f)); //bottom
        return ltrb;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //图所占范围大小
        chart.setChartRange(w, h);
    }

    @Override
    public void render(Canvas canvas) {
        try {
            chart.render(canvas);
        } catch (Exception e) {
            LogUtils.e(e);
        }
    }
}