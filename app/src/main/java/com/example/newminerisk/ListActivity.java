package com.example.newminerisk;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.newminerisk.bean.Colliery;
import com.example.newminerisk.bean.HiddenDangerRecord;
import com.example.newminerisk.common.NetUtil;
import com.example.newminerisk.net.BaseJsonRes;
import com.example.newminerisk.net.NetClient;
import com.example.newminerisk.tools.Constants;
import com.example.newminerisk.tools.Utils;
import com.juns.health.net.loopj.android.http.RequestParams;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {
    private static final String TAG = "ListActivity";
    protected NetClient netClient;
    private ImageView imageView;
    private RecyclerView recyclerView;
    private TextView title;
    private List<HiddenDangerRecord> list;
    private ExpandAdapter adapter;
    private Integer total;
    private int page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        netClient = new NetClient(this);
        imageView = findViewById(R.id.img_left);
        recyclerView = findViewById(R.id.recyclerView);
        title = findViewById(R.id.txt_title);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        title.setText(getIntent().getStringExtra("title"));
        getHiddenDangerRecordListTotal(Constants.PAGE);
        list = new ArrayList<>();


        adapter = new ExpandAdapter(R.layout.item_listing_supervision, list);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.click_more) {
                    list.get(position).setExpand(!list.get(position).isExpand());
                    adapter.notifyItemChanged(position);
                }
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                addData();
                if (list.size() == total) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.loadMoreComplete();
                }
            }
        }, recyclerView);

        adapter.setEnableLoadMore(true);

        //addData();
    }

    private void addData() {
        getHiddenDangerRecordListTotal((page+1)+"");
        adapter.notifyDataSetChanged();
    }

    //获取隐患统计
    private void getHiddenDangerRecordListTotal(String page) {
        String collieryId = getIntent().getExtras().getString("collieryId");
        String openType = getIntent().getExtras().getString("openType");
        String customParamsOne = getIntent().getExtras().getString("customParamsOne");
        String customParamsTwo = getIntent().getExtras().getString("customParamsTwo");
        String url = getIntent().getExtras().getString("url");
        System.out.println("url============================"+url);
        System.out.println("collieryId============================"+collieryId);
        System.out.println("customParamsOne============================"+customParamsOne);
        System.out.println("customParamsTwo============================"+customParamsTwo);
        total =getIntent().getExtras().getInt("total");
        System.out.println("total============================"+total);
        if (!NetUtil.checkNetWork(ListActivity.this)) {
            String jsondata = Utils.getValue(ListActivity.this, url);
            if("".equals(jsondata)){
                Utils.showShortToast(ListActivity.this, "没有联网，没有请求到数据");
            }else{
                resultHiddenDangerRecordList(jsondata);
            }
        }else{
            RequestParams params = new RequestParams();
            params.put("kuangQuId",collieryId);
            params.put("openType",openType);
            params.put("customParamsOne",customParamsOne);
            params.put("customParamsTwo",customParamsTwo);
            params.put("page",page);
            params.put("rows",Constants.ROWS);
            netClient.post(Constants.MAIN_ENGINE+url, params, new BaseJsonRes() {

                @Override
                public void onMySuccess(String data) {
                    Log.i(TAG, "总局隐患返回数据：" + data);
                    if (!TextUtils.isEmpty(data)) {
                        resultHiddenDangerRecordList(data);
                    }

                }

                @Override
                public void onMyFailure(String content) {
                    Log.e(TAG, "总局隐患返回错误信息：" + content);
                    Utils.showShortToast(ListActivity.this, content);
                }
            });
        }
    }

    private void resultHiddenDangerRecordList(String data){
        if (!TextUtils.isEmpty(data)) {
            JSONObject returndata = JSON.parseObject(data);
            String rows = returndata.getString("rows");
            page = Integer.parseInt(returndata.getString("page"));
            List<HiddenDangerRecord> hiddenDangerRecords = JSONArray.parseArray(rows, HiddenDangerRecord.class);
            list.addAll(hiddenDangerRecords);
            adapter.notifyDataSetChanged();
        }
    }

}
