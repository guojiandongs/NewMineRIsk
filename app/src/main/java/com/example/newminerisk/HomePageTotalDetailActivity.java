package com.example.newminerisk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.newminerisk.adapter.IdentificationEvaluationAdapter;
import com.example.newminerisk.bean.HiddenDangerRecord;
import com.example.newminerisk.bean.IdentificationEvaluation;
import com.example.newminerisk.net.BaseJsonRes;
import com.example.newminerisk.net.NetClient;
import com.example.newminerisk.tools.Constants;
import com.example.newminerisk.tools.UserUtils;
import com.example.newminerisk.tools.Utils;
import com.example.newminerisk.util.DensityUtil;
import com.juns.health.net.loopj.android.http.RequestParams;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomePageTotalDetailActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = "SuperviseRecordManageme";
    private TextView txtLeft;
    private ImageView imgLeft;
    private TextView txtTitle;
    private ImageView imgRight;
    private TextView txtRight;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    protected NetClient netClient;
    private boolean onLoading = false;
    private int page = 1;
    private int pagesize = 1;
    private List<IdentificationEvaluation> evaluationLists = new ArrayList<IdentificationEvaluation>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage_totaldetail);
        netClient = new NetClient(HomePageTotalDetailActivity.this);
        initView();
        setView();
        evaluationLists.clear();
        initdata(Constants.PAGE);
    }

    private void setView() {
        txtTitle.setText(getIntent().getStringExtra("topname"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void initView() {
        txtLeft = findViewById(R.id.txt_left);
        imgLeft = findViewById(R.id.img_left);
        txtTitle = findViewById(R.id.txt_title);
        imgRight = findViewById(R.id.img_right);
        txtRight = findViewById(R.id.txt_right);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                System.out.println("recyclerView==========="+newState);
                if (recyclerView.getAdapter().getItemCount() != 0) {
                    //得到当前显示的最后一个item的view
                    View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                    if (lastChildView != null) {
                        //得到lastChildView的bottom坐标值
                        int lastChildBottom = lastChildView.getBottom();
                        //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                        int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                        //通过这个lastChildView得到这个view当前的position值
                        int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);
                        System.out.println("findFirstCompletelyVisibleItemPosition:" + (((LinearLayoutManager) recyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPosition()));
                        System.out.println("Math.abs(lastChildBottom - recyclerBottom):" + Math.abs(lastChildBottom - recyclerBottom));
                        System.out.println("lastPosition:" + lastPosition);
                        System.out.println("recyclerView.getLayoutManager().getItemCount() - 1:" + (recyclerView.getLayoutManager().getItemCount() - 1));
                        //判断lastChildView的bottom值跟recyclerBottom
                        //判断lastPosition是不是最后一个position
                        //如果两个条件都满足则说明是真正的滑动到了底部
                        if (!onLoading && 0 != (((LinearLayoutManager) recyclerView.getLayoutManager())
                                .findFirstCompletelyVisibleItemPosition())
                                && Math.abs(lastChildBottom - recyclerBottom) <= DensityUtil.dip2px(HomePageTotalDetailActivity.this,
                                8)
                                && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1
                                && newState == RecyclerView.SCROLL_STATE_IDLE) {
                            //loading more data

                            if (page < pagesize) {
                                initdata(Integer.toString(page + 1));
                            } else {
                                Toast.makeText(HomePageTotalDetailActivity.this, "全部加载完毕", Toast.LENGTH_SHORT).show();
                            }


                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    private void initdata(final String curpage){
        if(curpage.equals("1")){
            evaluationLists.clear();
        }
        RequestParams params = new RequestParams();
        Map<String, String> paramsMap = new HashMap<String, String>();
        params.put("page", curpage);
        params.put("rows", Constants.ROWS);
        //paramsMap.put("employeeId", UserUtils.getUserID(HomePageTotalDetailActivity.this));
        String datatype = getIntent().getStringExtra("datatype");
        String url = "";
        if(datatype.equals("mLlDeleteNum")){
            paramsMap.put("ishandle","1");
            String jsonString = JSON.toJSONString(paramsMap);
            params.put("hiddenDangerRecordJsonData", jsonString);
            //url = Constants.GET_XIAOHAOLIST;
        }else if(datatype.equals("mLlWithinTheTimeLimitNum")){
            paramsMap.put("customParamsSix","0");
            String jsonString = JSON.toJSONString(paramsMap);
            params.put("threeFixJsonData", jsonString);
            //url = Constants.GET_WITHINTHETIMELIST;
        }else if(datatype.equals("mLlUnchangeNum")){
            paramsMap.put("ishandle","0");
            String jsonString = JSON.toJSONString(paramsMap);
            params.put("hiddenDangerRecordJsonData", jsonString);
            //url = Constants.GET_XIAOHAOLIST;
        }else if(datatype.equals("mLlForAcceptanceNum")){
            paramsMap.put("customParamsSix","0");
            String jsonString = JSON.toJSONString(paramsMap);
            params.put("threeFixJsonData", jsonString);
            //url = Constants.GET_FORACCEPTANCELIST;
        }else if(datatype.equals("mLlNianduNum")){
            params.put("evaluationType","0");
            url = Constants.GET_YEARORZHUANG_BILITYIDENTIF_LIST;
        }else if(datatype.equals("mLlZhuanxiangNum")){
            params.put("evaluationType","1");
            url = Constants.GET_YEARORZHUANG_BILITYIDENTIF_LIST;
        }else if(datatype.equals("mLlDangyueNum")){
            url = Constants.get_IdentificMonth_List;
        }else if(datatype.equals("mLlRiskStatisticsNum")){
            paramsMap.put("customParamsTen", UserUtils.getUserID(HomePageTotalDetailActivity.this));//状态
            String jsonString = JSON.toJSONString(paramsMap);
            params.put("evaluationJson", jsonString);
            //url = Constants.GET_RISKSTATISTICS_LIST;
        }else if(datatype.equals("mLlonNum_num")||datatype.equals("mLlopenNum_num")||datatype.equals("mLlcloseNum_num")){
            String openType = getIntent().getStringExtra("openType");
            params.put("openType", openType);//状态
            url = Constants.GET_IDENTIFIC_LIST_BYOPENTYPE;
        }
        String kuangQuId = getIntent().getStringExtra("kuangQuId");
        String customParamsOne = getIntent().getStringExtra("customParamsOne");
        String customParamsTwo = getIntent().getStringExtra("customParamsTwo");
        params.put("kuangQuId", kuangQuId);//状态
        params.put("customParamsOne", customParamsOne);//状态
        params.put("customParamsTwo", customParamsTwo);//状态
        getDetailRecord(url,params,datatype,curpage);
    }

    //查询督办列表
    private void getDetailRecord(String url, RequestParams params, final String datatype, final String curpage) {
        netClient.post(Constants.MAIN_ENGINE+url, params, new BaseJsonRes() {
            @Override
            public void onStart() {
                super.onStart();
                onLoading = true;
                if (curpage.equals("1")) {
                    if (!swipeRefreshLayout.isRefreshing()) {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                } else {
                    Toast.makeText(HomePageTotalDetailActivity.this, "正在加载" + curpage + "/" + pagesize, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onMySuccess(String data) {
                Log.i(TAG, "首页统计详情列表返回数据：" + data);
                if (!TextUtils.isEmpty(data)) {
                    JSONObject returndata = JSON.parseObject(data);
                    String rows = returndata.getString("rows");
                    page = Integer.parseInt(returndata.getString("page"));
                    pagesize = Integer.parseInt(returndata.getString("totalPage"));
                    if(datatype.equals("mLlDeleteNum")||datatype.equals("mLlUnchangeNum")){
                        List<HiddenDangerRecord> recordList = JSONArray.parseArray(rows, HiddenDangerRecord.class);
                        //recordLists.addAll(recordList);
                        //recyclerView.setAdapter(new HomePageTotalDetailAdapter(recordLists,threeFixLists,datatype));
                    }else{
                        //List<ThreeFix> recordList = JSONArray.parseArray(rows, ThreeFix.class);
                        //threeFixLists.addAll(recordList);
                        //recyclerView.setAdapter(new HomePageTotalDetailAdapter(recordLists,threeFixLists,datatype));
                    }
                    if(datatype.equals("mLlZhuanxiangNum")||datatype.equals("mLlNianduNum")||datatype.equals("mLlDangyueNum")||datatype.equals("mLlRiskStatisticsNum")
                        ||datatype.equals("mLlonNum_num")||datatype.equals("mLlcloseNum_num")||datatype.equals("mLlopenNum_num")){
                        List<IdentificationEvaluation> recordList = JSONArray.parseArray(rows, IdentificationEvaluation.class);
                        evaluationLists.addAll(recordList);
                        recyclerView.setAdapter(new IdentificationEvaluationAdapter(evaluationLists,HomePageTotalDetailActivity.this,datatype));
                    }
                }

            }

            @Override
            public void onMyFailure(String content) {
                Log.e(TAG, "查询督办列表返回错误信息：" + content);
                Utils.showLongToast(HomePageTotalDetailActivity.this, content);
                return;
            }

            @Override
            public void onFinish() {
                super.onFinish();
                swipeRefreshLayout.setRefreshing(false);
                onLoading = false;
            }
        });
    }

    @Override
    public void onRefresh() {
        evaluationLists.clear();
        initdata(Constants.PAGE);
    }

    @Override
    public void startActivity(Intent intent) {
        startActivityForResult(intent, -1);
    }

    public void onBack(View view) {
        onBackPressed();
    }

}
