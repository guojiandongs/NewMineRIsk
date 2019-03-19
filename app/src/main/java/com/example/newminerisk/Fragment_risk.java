package com.example.newminerisk;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.example.newminerisk.bean.Colliery;
import com.example.newminerisk.bean.GroupCount;
import com.example.newminerisk.bean.GroupCountJb;
import com.example.newminerisk.common.NetUtil;
import com.example.newminerisk.net.BaseJsonRes;
import com.example.newminerisk.net.NetClient;
import com.example.newminerisk.tools.Constants;
import com.example.newminerisk.tools.Utils;
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
import com.juns.health.net.loopj.android.http.RequestParams;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

//消息
public class Fragment_risk extends Fragment {
    private static final String TAG = "MainWindowActivity";
    protected NetClient netClient;
    private TextView title;
    private TextView tvNum10;
    private TextView tvNum11;
    private TextView tvNum20;
    private TextView tvNum21;
    private TextView tvNum30;
    private PieChart pieChart;
    private TextView tvPiechartTop;
    private TextView totalType;
    private TextView tbStyle;
    private PieChart pieChartBig;
    private BarChart lineChartTop;
    private BarChart lineChartBotoom;
    private List<String> mineList = new ArrayList<>();
    private String[] colorList = new String[]{"#3bbd0a", "#0068f7", "#21d7fb",
            "#f19ec2", "#80c269", "#facd89", "#c490bf", "#f39800", "#b28850", "#e5004f","#3bbd0a", "#0068f7", "#21d7fb",
            "#f19ec2", "#80c269", "#facd89", "#c490bf", "#f39800", "#b28850", "#e5004f"};
    private ImageView ivMonth;
    private ImageView ivSelect;
    String mineName = "总局";
    String collieryId = "";
    GroupCountJb groupCountJb;
    List<Colliery> collierys;
    List<Colliery> collieryList;
    String timeName = getTime(Calendar.getInstance().getTime()).toString();
    private LinearLayoutCompat ll10;
    private LinearLayoutCompat ll11;
    private LinearLayoutCompat ll20;
    private LinearLayoutCompat ll21;
    private LinearLayoutCompat ll30;
    private View layout;
    private Activity ctx;
    private IndexActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        netClient = new NetClient(getActivity());
        if (layout == null) {
            ctx = this.getActivity();
            parentActivity = (IndexActivity) getActivity();
            layout = ctx.getLayoutInflater().inflate(R.layout.activity_main_window_risk,
                    null);
        } else {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        initView(layout);
        return layout;
    }

    private void initView(View layout) {
        title = layout.findViewById(R.id.title);
        tvNum10 = layout.findViewById(R.id.tv_num_10);
        tvNum11 = layout.findViewById(R.id.tv_num_11);
        tvNum20 = layout.findViewById(R.id.tv_num_20);
        tvNum21 = layout.findViewById(R.id.tv_num_21);
        tvNum30 = layout.findViewById(R.id.tv_num_30);
        pieChart = layout.findViewById(R.id.pieChart);
        tvPiechartTop = layout.findViewById(R.id.tv_piechart_top);
        pieChartBig = layout.findViewById(R.id.pieChart_big);
        lineChartTop = layout.findViewById(R.id.lineChartTop);
        lineChartBotoom = layout.findViewById(R.id.lineChartBotoom);
        ivMonth = layout.findViewById(R.id.iv_month);
        ivSelect = layout.findViewById(R.id.iv_select);
        totalType = layout.findViewById(R.id.tb_totalType);
        tbStyle = layout.findViewById(R.id.tb_style);
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
        ll10 = layout.findViewById(R.id.ll_10);
        ll11 = layout.findViewById(R.id.ll_11);
        ll20 = layout.findViewById(R.id.ll_20);
        ll21 = layout.findViewById(R.id.ll_21);
        ll30 = layout.findViewById(R.id.ll_30);
        ll10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(collieryId)){
                    if(!tvNum10.getText().toString().equals("0")){
                        Intent intent = new Intent(ctx, ListActivity.class);
                        intent.putExtra("total", Integer.parseInt(tvNum10.getText().toString()));
                        intent.putExtra("collieryId", collieryId);
                        intent.putExtra("url", Constants.GET_HIDDENDANGER_RECORD_LISTTOTAL);
                        int yesr = Integer.parseInt(timeName.split("-")[0]);
                        int month = Integer.parseInt(timeName.split("-")[1]);
                        String data = getSupportEndDayofMonth(yesr,month);
                        intent.putExtra("customParamsOne", timeName+"-0100:00:00");
                        intent.putExtra("customParamsTwo", data);
                        startActivity(intent);
                    }
                }else{
                    Utils.showShortToast(ctx, "总局无法查询数据！");
                }
            }
        });
        ll11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(collieryId)){
                    if(!tvNum11.getText().toString().equals("0")){
                        Intent intent = new Intent(ctx, ListActivity.class);
                        intent.putExtra("total", Integer.parseInt(tvNum11.getText().toString()));
                        intent.putExtra("collieryId", collieryId);
                        intent.putExtra("url", Constants.GET_HIDDENDANGER_RECORD_LIST_IMPORTANT);
                        int yesr = Integer.parseInt(timeName.split("-")[0]);
                        int month = Integer.parseInt(timeName.split("-")[1]);
                        String data = getSupportEndDayofMonth(yesr,month);
                        intent.putExtra("customParamsOne", timeName+"-0100:00:00");
                        intent.putExtra("customParamsTwo", data);
                        startActivity(intent);
                    }
                }else{
                    Utils.showShortToast(ctx, "总局没有提供查询接口");
                }
            }
        });
        ll20.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(collieryId)){
                    if(!tvNum20.getText().toString().equals("0")){
                        Intent intent = new Intent(ctx, ListActivity.class);
                        intent.putExtra("total", Integer.parseInt(tvNum20.getText().toString()));
                        intent.putExtra("collieryId", collieryId);
                        intent.putExtra("url", Constants.GET_HIDDENDANGER_RECORD_LIST_SUPPER);
                        int yesr = Integer.parseInt(timeName.split("-")[0]);
                        int month = Integer.parseInt(timeName.split("-")[1]);
                        String data = getSupportEndDayofMonth(yesr,month);
                        intent.putExtra("customParamsOne", timeName+"-0100:00:00");
                        intent.putExtra("customParamsTwo", data);
                        startActivity(intent);
                    }
                }else{
                    Utils.showShortToast(ctx, "总局没有提供查询接口");
                }
            }
        });
        ll21.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(collieryId)){
                    if(!tvNum21.getText().toString().equals("0")){
                        Intent intent = new Intent(ctx, ListActivity.class);
                        intent.putExtra("total", Integer.parseInt(tvNum21.getText().toString()));
                        intent.putExtra("collieryId", collieryId);
                        intent.putExtra("url", Constants.GET_HIDDENDANGER_RECORD_LIST_CLOSE);
                        int yesr = Integer.parseInt(timeName.split("-")[0]);
                        int month = Integer.parseInt(timeName.split("-")[1]);
                        String data = getSupportEndDayofMonth(yesr,month);
                        intent.putExtra("customParamsOne", timeName+"-0100:00:00");
                        intent.putExtra("customParamsTwo", data);
                        startActivity(intent);
                    }
                }else{
                    Utils.showShortToast(ctx, "总局没有提供查询接口");
                }
            }
        });
        ll30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(collieryId)){
                    if(!tvNum30.getText().toString().equals("0")){
                        Intent intent = new Intent(ctx, ListActivity.class);
                        intent.putExtra("total", Integer.parseInt(tvNum30.getText().toString()));
                        intent.putExtra("collieryId", collieryId);
                        intent.putExtra("url", Constants.GET_HIDDENDANGER_RECORD_LIST_OPEN);
                        int yesr = Integer.parseInt(timeName.split("-")[0]);
                        int month = Integer.parseInt(timeName.split("-")[1]);
                        String data = getSupportEndDayofMonth(yesr,month);
                        intent.putExtra("customParamsOne", timeName+"-0100:00:00");
                        intent.putExtra("customParamsTwo", data);
                        startActivity(intent);
                    }
                }else{
                    Utils.showShortToast(ctx, "总局没有提供查询接口");
                }
            }
        });
    }

    private void initData(){
        title.setText(mineName + "[" + timeName + "]");
        getCollieryList();
        getGroupCount();
        getGroupCountJb();
        getGroupRecordCount();
        getGroupImportRecordCount();
    }

    //获取矿区
    private void getCollieryList() {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_COLLIERY_LIST);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultColliery(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_COLLIERY_LIST, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获取矿区返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultColliery(data);
                    }

                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取矿区返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取总局隐患
    private void getGroupCount() {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_GROUP_COUNT_);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupCount(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_GROUP_COUNT_, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "总局隐患返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupCount(data);
                    }

                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "总局隐患返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取总局隐患等级
    private void getGroupCountJb() {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_GROUP_COUNT_JB);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupCountJb(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_GROUP_COUNT_JB, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "总局隐患等级返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupCountJb(data);
                    }

                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "总局隐患等级返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取矿区隐患级别统计信息
    private void getRecordCountNumByJb(String collieryId) {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_RECORD_COUNTNUMBYJB);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupCountJb(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("collieryId",collieryId);
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_RECORD_COUNTNUMBYJB, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获取矿区隐患级别统计信息返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupCountJb(data);
                    }
                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取矿区隐患级别统计信息返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取所有矿的隐患分矿统计信息
    private void getGroupRecordCount() {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_GROUP_RECORD_COUNT);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupRecordCount(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_GROUP_RECORD_COUNT, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获取所有矿的隐患分矿统计信息返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupRecordCount(data);
                    }

                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取所有矿的隐患分矿统计信息返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取所有矿的重大隐患分矿统计信息
    private void getGroupImportRecordCount() {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_GROUP_IMPORTANT_RECORD_COUNT);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupImportRecordCount(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_GROUP_IMPORTANT_RECORD_COUNT, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获取所有矿的隐患分矿统计信息返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupImportRecordCount(data);
                    }
                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取所有矿的隐患分矿统计信息返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取单个矿区隐患统计信息
    private void getKuangQuCountGroup(String collieryId) {
        System.out.println("collieryid================================="+collieryId);
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_KUANGQU_COUNT_GROUP);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupCount(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("collieryId",collieryId);
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_KUANGQU_COUNT_GROUP, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获获取单个矿区隐患统计信息返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupCount(data);
                    }
                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取单个矿区隐患统计信息返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取矿区隐患专业统计信息
    private void getRecordCountNumBySid(String collieryId) {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_RECORD_COUNTNUMBYSID);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupRecordCount(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("collieryId",collieryId);
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_RECORD_COUNTNUMBYSID, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获取矿区隐患专业统计信息返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupRecordCount(data);
                    }
                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取矿区隐患专业统计信息返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    //获取矿区隐患类型统计信息
    private void getRecordCountNumByTid(String collieryId) {
        if (!NetUtil.checkNetWork(ctx)) {
            String jsondata = Utils.getValue(ctx, Constants.GET_RECORD_COUNTNUMBYTID);
            if("".equals(jsondata)){
                Utils.showShortToast(ctx, "没有联网，没有请求到数据");
            }else{
                resultGroupImportRecordCount(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("collieryId",collieryId);
            params.put("customParamsOne",timeName+"-0100:00:00");
            int yesr = Integer.parseInt(timeName.split("-")[0]);
            int month = Integer.parseInt(timeName.split("-")[1]);
            String data = getSupportEndDayofMonth(yesr,month);
            params.put("customParamsTwo",data);
            netClient.post(Constants.MAIN_ENGINE+Constants.GET_RECORD_COUNTNUMBYTID, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "获取矿区隐患类型统计信息返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultGroupImportRecordCount(data);
                    }
                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "获取矿区隐患类型统计信息返回错误信息：" + content);
                    Utils.showShortToast(ctx, content);
                }
            });
        }
    }

    private void resultColliery(String data){
        collieryList = JSONArray.parseArray(data, Colliery.class);
        mineList.add("总局");
        for (int i = 0; i < collieryList.size(); i++) {
            mineList.add(collieryList.get(i).getCollieryName());
        }
    }

    private void resultGroupCount(String data){
        GroupCount groupCount = JSONObject.parseObject(data, GroupCount.class);
        tvNum21.setText(groupCount.getCloseNum());
        tvNum11.setText(groupCount.getImportantNum());
        tvNum10.setText(groupCount.getTotalNum());
        tvNum30.setText(groupCount.getOpenNum());
        tvNum20.setText(groupCount.getSupperNum());
    }
    private void resultGroupCountJb(String data){
        groupCountJb = JSONObject.parseObject(data, GroupCountJb.class);
        initBigChartView();
        initChartView();
    }

    private void resultGroupRecordCount(String data){
        collierys = JSONArray.parseArray(data, Colliery.class);
        setUpBarTopData(collieryId);
        initBarChart(lineChartTop);
    }

    private void resultGroupImportRecordCount(String data){
        collierys = JSONArray.parseArray(data, Colliery.class);
        setUpBarBotoomData(collieryId);
        initBarChart(lineChartBotoom);
    }

    private void selectMine() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(ctx, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                mineName = mineList.get(options1);
                title.setText(mineName + "[" + timeName + "]");
                if(options1==0){
                    collieryId = "";
                    getGroupCount();
                    getGroupCountJb();
                    getGroupRecordCount();
                    getGroupImportRecordCount();
                    totalType.setText("各矿隐患统计");
                    tbStyle.setText("各矿个月重大隐患统计");
                }else{
                    collieryId = collieryList.get(options1-1).getId();
                    getKuangQuCountGroup(collieryId);
                    getRecordCountNumByJb(collieryId);
                    totalType.setText("隐患专业统计");
                    tbStyle.setText("隐患类型统计");
                    getRecordCountNumBySid(collieryId);
                    getRecordCountNumByTid(collieryId);
                }
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

        TimePickerView pvTime = new TimePickerBuilder(ctx, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                timeName = getTime(date).toString();
                title.setText(mineName + "[" + timeName + "]");
                if(TextUtils.isEmpty(collieryId)){
                    getGroupCount();
                    getGroupCountJb();
                    getGroupRecordCount();
                    getGroupImportRecordCount();
                    totalType.setText("各矿隐患统计");
                    tbStyle.setText("各矿个月重大隐患统计");
                }else{
                    getKuangQuCountGroup(collieryId);
                    getRecordCountNumByJb(collieryId);
                    totalType.setText("隐患专业统计");
                    tbStyle.setText("隐患类型统计");
                    getRecordCountNumBySid(collieryId);
                    getRecordCountNumByTid(collieryId);
                }
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

    private void setUpBarBotoomData(String collieryId) {
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        for (int i = 0; i < collierys.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            String name;
            String totalNum;
            if(TextUtils.isEmpty(collieryId)){
                name = collierys.get(i).getCollieryName();
                totalNum = collierys.get(i).getImportantNum();
            }else{
                name = collierys.get(i).getTname();
                totalNum = collierys.get(i).getTotalNum();
            }
            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
            //i: 位置  (float) new Random().nextInt(100):值
            BarEntry barEntry = new BarEntry(i, Integer.parseInt(totalNum));
            entries.add(barEntry);
            // 每一个BarDataSet代表一类柱状图
            BarDataSet barDataSet = new BarDataSet(entries, name);

            initBarDataSet(barDataSet, Color.parseColor(colorList[i]));
            dataSets.add(barDataSet);
        }
        // 添加多个BarDataSet时
        BarData data = new BarData(dataSets);
        //BarChart控件宽度 / 柱状图数量  * mBarWidth
        data.setBarWidth(0.5f);
        lineChartBotoom.setData(data);
    }

    private void setUpBarTopData(String collieryId) {
        ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
        for (int i = 0; i < collierys.size(); i++) {
            /**
             * 此处还可传入Drawable对象 BarEntry(float x, float y, Drawable icon)
             * 即可设置柱状图顶部的 icon展示
             */
            ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
            //i: 位置  (float) new Random().nextInt(100):值
            String name;
            if(TextUtils.isEmpty(collieryId)){
                name = collierys.get(i).getCollieryName();
            }else{
                name = collierys.get(i).getSname();
            }
            System.out.println("name==========================="+name);
            BarEntry barEntry = new BarEntry(i, Integer.parseInt(collierys.get(i).getTotalNum()));
            entries.add(barEntry);
            // 每一个BarDataSet代表一类柱状图
            BarDataSet barDataSet = new BarDataSet(entries, name);
            initBarDataSet(barDataSet, Color.parseColor(colorList[i]));
            dataSets.add(barDataSet);
        }
        // 添加多个BarDataSet时
        BarData data = new BarData(dataSets);
        //BarChart控件宽度 / 柱状图数量  * mBarWidth
        data.setBarWidth(0.5f);
        lineChartTop.setData(data);
    }

    private void initBigChartView() {
        pieChartBig.clear();
        //设置标题
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            titles.add(Integer.toString(i));
        }
        ArrayList<PieEntry> entrys = new ArrayList<>();
        //for (int i = 0; i < 4; i++) {
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getC()),"一般"));
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getB()),"较大"));
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getA()),"重大"));
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getD()),"轻微"));
        //}
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
        pieChartBig.notifyDataSetChanged();
        l.setEnabled(false);
    }

    private void initChartView() {
        //设置标题
        ArrayList<String> titles = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            titles.add(Integer.toString(i));
        }
        ArrayList<PieEntry> entrys = new ArrayList<>();
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getC()),"一般"));
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getB()),"较大"));
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getA()),"重大"));
        entrys.add(new PieEntry(Integer.parseInt(groupCountJb.getD()),"轻微"));
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

    /**
     * 根据提供的年月获取该月份的最后一天
     * @Description: (这里用一句话描述这个方法的作用)
     * @Author: gyz
     * @Since: 2017-1-9下午2:29:38
     * @param year
     * @param monthOfYear
     * @return
     */
    public static String getSupportEndDayofMonth(int year, int monthOfYear) {
        Calendar cal = Calendar.getInstance();
        // 不加下面2行，就是取当前时间前一个月的第一天及最后一天
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, monthOfYear);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);

        cal.add(Calendar.DAY_OF_MONTH, -1);
        Date lastDate = cal.getTime();

        SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-ddHH:mm:ss" );
        String str = sdf.format(lastDate);
        return str;
    }

    /**
     * 刷新页面
     */
    public void refresh() {
        //initViews();
    }

}
