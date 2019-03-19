package com.example.newminerisk;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.newminerisk.dialog.MyAlertDialog;
import com.example.newminerisk.util.UpdateVersionUtil;

public class IndexActivity extends FragmentActivity {
    protected static final String TAG = "MainActivity";
    private Fragment[] fragments;
    public Fragment_hidden hiddenStatisticsfragment;
    private Fragment_hidden riskStatisticsfragment;
    private ImageView[] imagebuttons;
    private TextView[] textviews;
    private String connectMsg = "aa";
    public int index;
    private int currentTabIndex;// 当前fragment的index
    private LinearLayoutCompat mLlDialog;
    private LinearLayoutCompat mLlManageDetail;
    private LinearLayoutCompat mLlManageRelease;
    private LinearLayoutCompat mLlManageRectification;
    private LinearLayoutCompat mLlManageTracking;
    private LinearLayoutCompat mLlManageOverdue;
    private LinearLayoutCompat mLlManageReview;
    private LinearLayoutCompat mLlManage;
    private LinearLayoutCompat mLlChart;
    public static boolean isForeground = false;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        initTabView();
        new UpdateVersionUtil().versionUpdata(IndexActivity.this, true);
    }

    private void initTabView() {
        hiddenStatisticsfragment = new Fragment_hidden();
        riskStatisticsfragment = new Fragment_hidden();
        fragments = new Fragment[]{hiddenStatisticsfragment,riskStatisticsfragment};
        imagebuttons = new ImageView[2];
        imagebuttons[0] = (ImageView) findViewById(R.id.ib_contact_list);
        imagebuttons[1] = (ImageView) findViewById(R.id.ib_weixin);
        imagebuttons[0].setSelected(true);
        textviews = new TextView[5];
        textviews[0] = (TextView) findViewById(R.id.tv_contact_list);
        textviews[1] = (TextView) findViewById(R.id.tv_weixin);
        textviews[0].setTextColor(getResources().getColor(R.color.blue1));
        // 添加显示第一个fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, hiddenStatisticsfragment)
                .add(R.id.fragment_container, riskStatisticsfragment)
                .hide(riskStatisticsfragment).show(hiddenStatisticsfragment).commit();
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mLlDialog.getVisibility() == View.VISIBLE) {
            mLlDialog.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }

    //重写onKeyDown方法,对按键(不一定是返回按键)监听
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {//当返回按键被按下
            MyAlertDialog alertDialog = new MyAlertDialog(IndexActivity.this
                    , new MyAlertDialog.DialogListener() {
                @Override
                public void affirm() {
                    finish();//结束当前Activity
                }

                @Override
                public void cancel() {

                }
            }, "你确定要退出登录吗？");
            alertDialog.show();
        }
        return false;
    }

    public void onTabClicked(View view) {
        //img_right.setVisibility(View.GONE);
        switch (view.getId()) {
            case R.id.re_contact_list:
                //img_right.setVisibility(View.VISIBLE);
                index = 0;
                if (hiddenStatisticsfragment != null) {
                    hiddenStatisticsfragment.refresh();
                }
                break;
            case R.id.re_weixin:
                index = 1;
                break;
        }
        if (currentTabIndex != index) {
            FragmentTransaction trx = getSupportFragmentManager()
                    .beginTransaction();
            trx.hide(fragments[currentTabIndex]);
            System.out.println("fragments.isadd" + fragments[index].getClass().getSimpleName() + ":" + Boolean.toString(fragments[index].isAdded()));
            if (!fragments[index].isAdded()) {
                trx.add(R.id.fragment_container, fragments[index]);
            }
            System.out.println("index:" + Integer.toString(index));
            System.out.println("getSimpleName:" + fragments[index].getClass().getSimpleName());
            trx.show(fragments[index]).commit();
        }
        imagebuttons[currentTabIndex].setSelected(false);
        // 把当前tab设为选中状态
        imagebuttons[index].setSelected(true);
        textviews[currentTabIndex].setTextColor(getResources().getColor(R.color.colorGraylighter));
        textviews[index].setTextColor(getResources().getColor(R.color.blue1));
        currentTabIndex = index;
    }
}