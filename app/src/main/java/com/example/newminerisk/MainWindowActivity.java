package com.example.newminerisk;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainWindowActivity extends AppCompatActivity {

    private TextView title;
    private TextView tvNum10;
    private TextView tvNum11;
    private TextView tvNum20;
    private TextView tvNum21;
    private TextView tvNum30;
    private PieChart pieChart;
    private TextView tvPiechartTop;
    private PieChart pieChartBig;
    private BarChart lineChartTop;
    private BarChart lineChartBotoom;
    private List<String> mineList = Arrays.asList("东山矿", "杏花矿", "煤矿三", "煤矿四", "煤矿五", "煤矿六", "煤矿七", "煤矿八", "煤矿九", "煤矿十");
    private String[] colorList = new String[]{"#3bbd0a", "#0068f7", "#21d7fb",
            "#f19ec2", "#80c269", "#facd89", "#c490bf", "#f39800", "#b28850", "#e5004f",};
    private ImageView ivMonth;
    private ImageView ivSelect;
    String mineName = "总局";
    String timeName = getTime(Calendar.getInstance().getTime()).toString();
    private LinearLayoutCompat ll10;
    private LinearLayoutCompat ll11;
    private LinearLayoutCompat ll20;
    private LinearLayoutCompat ll21;
    private LinearLayoutCompat ll30;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_window);
        initView();
    }

    private void initView() {
        title = findViewById(R.id.title);
        tvNum10 = findViewById(R.id.tv_num_10);
        tvNum11 = findViewById(R.id.tv_num_11);
        tvNum20 = findViewById(R.id.tv_num_20);
        tvNum21 = findViewById(R.id.tv_num_21);
        tvNum30 = findViewById(R.id.tv_num_30);
        pieChart = findViewById(R.id.pieChart);
        tvPiechartTop = findViewById(R.id.tv_piechart_top);
        initChartView();
        pieChartBig = findViewById(R.id.pieChart_big);
        initBigChartView();
        lineChartTop = findViewById(R.id.lineChartTop);
        lineChartBotoom = findViewById(R.id.lineChartBotoom);
        initBarChart(lineChartTop);
        initBarChart(lineChartBotoom);
        setUpBarData();
        ivMonth = findViewById(R.id.iv_month);
        ivSelect = findViewById(R.id.iv_select);
        ivMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMonth();
            }
        });

        ivSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectMine();
            }
        });
        ll10 = findViewById(R.id.ll_10);
        ll11 = findViewById(R.id.ll_11);
        ll20 = findViewById(R.id.ll_20);
        ll21 = findViewById(R.id.ll_21);
        ll30 = findViewById(R.id.ll_30);
        ll10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainWindowActivity.this, ListActivity.class);
                intent.putExtra("title", "隐患总个数");
                startActivity(intent);
            }
        });
    }

    private void selectMine() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mineName = mineList.get(options1);
                title.setText(mineName + "[" + timeName + "]");
            }
        })
                .setSubmitText("确定")//确定按钮文字
                .setCancelText("取消")//取消按钮文字
                .setTitleText("选择煤矿")//标题
                .setSubCalSize(18)//确定和取消文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#11bfdd"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#11bfdd"))//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFEEEEEE)//滚轮背景颜色 Night mode
                .setContentTextSize(18)//滚轮文字大小
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setCyclic(false, false, false)//循环与否
                .setSelectOptions(1, 1, 1)  //设置默认选中项
                .setOutSideCancelable(false)//点击外部dismiss default true
                .isDialog(false)//是否显示为对话框样式
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。
                .build();

        pvOptions.setPicker(mineList);//添加数据源
        pvOptions.show();
    }

    private void selectMonth() {
        Calendar selectedDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        //startDate.set(2013,1,1);
        Calendar endDate = Calendar.getInstance();
        //endDate.set(2020,1,1);

        //正确设置方式 原因：注意事项有说明
        startDate.add(Calendar.YEAR, -2);

        TimePickerView pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                timeName = getTime(date).toString();
                title.setText(mineName + "[" + timeName + "]");
            }
        })
                .setType(new boolean[]{true, true, false, false, false, false})// 默认全部显示
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确定")//确认按钮文字
                .setTitleSize(20)//标题文字大小
                .setTitleText("选择月份")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(false)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.parseColor("#11bfdd"))//确定按钮文字颜色
                .setCancelColor(Color.parseColor("#11bfdd"))//取消按钮文字颜色
                .setTitleBgColor(0xFFFFFFFF)//标题背景颜色 Night mode
                .setBgColor(0xFFEEEEEE)//滚轮背景颜色 Night mode
                .setDate(selectedDate)// 如果不设置的话，默认是系统时间*/
                .setRangDate(startDate, endDate)//起始终止年月日设定
                .setLabel("年", "月", "日", "时", "分", "秒")//默认设置为年月日时分秒
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isDialog(false)//是否显示为对话框样式
                .build();
        pvTime.show();
    }

    private CharSequence getTime(Date date) {
        String time = "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(date);
    }

    private void setUpBarData() {
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        for (int i = 0; i < 10; i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
            //i: 位置  (float) new Random().nextInt(100):值
            BarEntry barEntry = new BarEntry(i, (i + 1) * 10);
            entries.add(barEntry);
            // 每一个BarDataSet代表一类柱状图
            BarDataSet barDataSet = new BarDataSet(entries, mineList.get(i));

            initBarDataSet(barDataSet, Color.parseColor(colorList[i]));
            dataSets.add(barDataSet);

        }
        // 添加多个BarDataSet时
        BarData data = new BarData(dataSets);
        //BarChart控件宽度 / 柱状图数量  * mBarWidth
        data.setBarWidth(0.5f);
        lineChartTop.setData(data);
        lineChartBotoom.setData(data);
    }

    private void initBigChartView() {
        //设置标题
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            titles.add(Integer.toString(i));
        }
        ArrayList<PieEntry> entrys = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            entrys.add(new PieEntry(i + 1, (i + 1) * 2));
        }
        //饼图数据集
        PieDataSet dataset = new PieDataSet(entrys, "星级评定");
        //饼图Item被选中时变化的距离
        dataset.setSelectionShift(0f);
        //颜色填充
        dataset.setColors(new int[]{Color.parseColor("#4808da"),
                Color.parseColor("#ff7737"),
                Color.parseColor("#30bd08"),
                Color.parseColor("#da089c")});
        //数据填充
        PieData piedata = new PieData(dataset);
        //设置饼图上显示数据的字体大小
        piedata.setValueTextSize(15);
        piedata.setValueTextColor(Color.TRANSPARENT);
        pieChartBig.setData(piedata);
        Description description = new Description();
        description.setText("");
        pieChartBig.setDescription(description);
        pieChartBig.setUsePercentValues(false);
        pieChartBig.setRotationAngle(270);
        pieChartBig.setDrawHoleEnabled(true);       //Boolean类型   设置图饼中心是否是空心
        pieChartBig.setHoleColor(Color.WHITE);       //Boolean类型   设置图饼中心是否是空心
        pieChartBig.setDrawSliceText(false);
        pieChartBig.setTransparentCircleColor(Color.parseColor("#1bccfa"));//设置环形图与中间空心圆之间的环形的颜色
        pieChartBig.setEntryLabelColor(Color.TRANSPARENT);
        pieChartBig.setTransparentCircleAlpha(100);//设置环形图与中间空心圆之间圆环的的透明度
        pieChartBig.setHoleRadius(50);//设置圆孔半径
        pieChartBig.setTransparentCircleRadius(0);//设置半透明圈的宽度
        pieChartBig.setDrawCenterText(false);         //Boolean类型    设置中心圆是否可以显示文字
        Legend l = pieChartBig.getLegend();
        pieChartBig.getLegend().setForm(Legend.LegendForm.CIRCLE);//获取饼图图例
        l.setEnabled(false);
    }

    private void initChartView() {
        //设置标题
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            titles.add(Integer.toString(i));
        }
        ArrayList<PieEntry> entrys = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            entrys.add(new PieEntry(i + 1, (i + 1) * 2));
        }
        //饼图数据集
        PieDataSet dataset = new PieDataSet(entrys, "星级评定");
        //饼图Item被选中时变化的距离
        dataset.setSelectionShift(0f);
        //颜色填充
        dataset.setColors(new int[]{Color.parseColor("#f96502"), Color.parseColor("#006bf7")});
        //数据填充
        PieData piedata = new PieData(dataset);
        //设置饼图上显示数据的字体大小
        piedata.setValueTextSize(15);
        piedata.setValueTextColor(Color.TRANSPARENT);
        pieChart.setData(piedata);
        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);
        pieChart.setUsePercentValues(false);
        pieChart.setRotationAngle(270);
        pieChart.setDrawHoleEnabled(true);       //Boolean类型   设置图饼中心是否是空心
        pieChart.setHoleColor(Color.parseColor("#1fd5fa"));       //Boolean类型   设置图饼中心是否是空心
        pieChart.setDrawSliceText(false);
        pieChart.setTransparentCircleColor(Color.parseColor("#1bccfa"));//设置环形图与中间空心圆之间的环形的颜色
        pieChart.setEntryLabelColor(Color.TRANSPARENT);
        pieChart.setTransparentCircleAlpha(100);//设置环形图与中间空心圆之间圆环的的透明度
        pieChart.setHoleRadius(60);//设置圆孔半径
        pieChart.setTransparentCircleRadius(0);//设置半透明圈的宽度
        pieChart.setDrawCenterText(true);         //Boolean类型    设置中心圆是否可以显示文字
        pieChart.setCenterTextSize(9);         //设置中心圆的文字大小
        pieChart.setCenterText("整改率\n88%");
        pieChart.setCenterTextColor(Color.parseColor("#000000"));        //设置中心圆的文字的颜色
        Legend l = pieChart.getLegend();
        pieChart.getLegend().setForm(Legend.LegendForm.CIRCLE);//获取饼图图例
        l.setEnabled(false);


    }


    private void initBarChart(BarChart barChart) {
        /***图表设置***/
        //背景颜色
        barChart.setBackgroundColor(Color.parseColor("#eeeeee"));
        //不显示图表网格
        barChart.setDrawGridBackground(false);
        //背景阴影
        barChart.setDrawBarShadow(false);
        barChart.setHighlightFullBarEnabled(false);
        //显示边框
        barChart.setDrawBorders(false);
        //是否双向指缩放
        barChart.setPinchZoom(false);
        //是否显示右下角的描述
        barChart.getDescription().setEnabled(false);

        barChart.setScaleMinima(1.0f, 1.0f);

        barChart.getViewPortHandler().refresh(new Matrix(), barChart, true);

        //设置动画效果
        barChart.animateY(1000, Easing.EasingOption.Linear);
        barChart.animateX(1000, Easing.EasingOption.Linear);

        /***XY轴的设置***/
        //X轴设置显示位置在底部
        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.TRANSPARENT); //网格线颜色
        xAxis.setEnabled(false);
        xAxis.setGranularity(1f);

        YAxis leftAxis = barChart.getAxisLeft();
        YAxis rightAxis = barChart.getAxisRight();
        leftAxis.setGridColor(Color.parseColor("#eaeaea")); //网格线颜色
        leftAxis.setAxisLineColor(Color.TRANSPARENT); //网格线颜色
        rightAxis.setGridColor(Color.TRANSPARENT); //网格线颜色
        rightAxis.setEnabled(false);
        //保证Y轴从0开始，不然会上移一点

        /***折线图例 标签 设置***/
        Legend legend = barChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setFormSize(8);
        legend.setTextSize(11f);
        //显示位置
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        legend.setWordWrapEnabled(true);
        //是否绘制在图表里面
        legend.setDrawInside(false);

    }


    /**
     * 柱状图始化设置 一个BarDataSet 代表一列柱状图
     *
     * @param barDataSet 柱状图
     * @param color      柱状图颜色
     */
    private void initBarDataSet(BarDataSet barDataSet, int color) {
        barDataSet.setColor(color);
        barDataSet.setFormLineWidth(1f);
        barDataSet.setFormSize(15.f);
        //不显示柱状图顶部值
        barDataSet.setDrawValues(false);
    }

}
