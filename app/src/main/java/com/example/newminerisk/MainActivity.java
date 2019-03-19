package com.example.newminerisk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.alibaba.fastjson.JSONArray;
import com.example.newminerisk.bean.UserInfo;
import com.example.newminerisk.common.NetUtil;
import com.example.newminerisk.dialog.FlippingLoadingDialog;
import com.example.newminerisk.net.BaseJsonRes;
import com.example.newminerisk.net.NetClient;
import com.example.newminerisk.tools.Constants;
import com.example.newminerisk.tools.UserUtils;
import com.example.newminerisk.tools.Utils;
import com.juns.health.net.loopj.android.http.RequestParams;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    protected NetClient netClient;
    private EditText et_username;
    private EditText et_password;
    private Button login;
    protected FlippingLoadingDialog mLoadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        netClient = new NetClient(this);
        initView();
    }

    private void initView() {
        et_username = findViewById(R.id.username);
        et_password = findViewById(R.id.password);
        final String userName = et_username.getText().toString().trim();
        final String password = et_password.getText().toString().trim();
        login = findViewById(R.id.login);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLogin(userName,password);
                //startActivity(new Intent(MainActivity.this, MainWindowActivity.class));
            }
        });
    }

    private void getLogin(final String userName, final String password) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
            if (!NetUtil.checkNetWork(MainActivity.this)) {
                String employeeList = Utils.getValue(MainActivity.this, Constants.OfLineEmployeeList);
                if(!TextUtils.isEmpty(employeeList)){
                    List<UserInfo> userInfoList = JSONArray.parseArray(employeeList, UserInfo.class);
                    String userinfoStr="";
                    for(UserInfo userInfo : userInfoList) {
                        if(userInfo.getUserName().equals(userName)&&userInfo.getPassword().equals(password)){
                            List<UserInfo> userInfoList1 = new ArrayList<>();
                            userInfoList1.add(userInfo);
                            userinfoStr = JSONArray.toJSONString(userInfoList1);
                            resultLogin(userName,password,userinfoStr);
                        }
                    }
                    if(TextUtils.isEmpty(userinfoStr)){
                        Utils.showShortToast(MainActivity.this, "用户名或密码错误");
                    }
                }else{
                    Utils.showShortToast(MainActivity.this, "没有联网，登录过的用户才可以离线登录");
                }
            }else{
                //getSystemTime();
                RequestParams params = new RequestParams();
                params.put("name", userName);
                params.put("password", password);
                getLoadingDialog("正在登录...  ").show();
                netClient.post(Constants.MAIN_ENGINE + Constants.Login_URL, params, new BaseJsonRes() {

                    @Override
                    public void onMySuccess(String data) {
                        //try{
                        getLoadingDialog("正在登录").dismiss();
                        String resultStr = data;
                        String employeeList = Utils.getValue(MainActivity.this, Constants.OfLineEmployeeList);
                        List<UserInfo> userInfoList = new ArrayList<>();
                        if(!TextUtils.isEmpty(employeeList)){
                            userInfoList = JSONArray.parseArray(employeeList, UserInfo.class);
                            for(int i =0;i<userInfoList.size();i++){
                                if(userInfoList.get(i).getUserName().equals(userName)){
                                    userInfoList.remove(userInfoList.get(i));
                                }
                            }
                        }
                        List<UserInfo>  resultUserInfoList = JSONArray.parseArray(resultStr, UserInfo.class);
                        UserInfo userInfo = resultUserInfoList.get(0);
                        userInfo.setUserName(userName);
                        userInfo.setPassword(password);
                        userInfoList.add(userInfo);
                        String OfLineEmployeeList = JSONArray.toJSONString(userInfoList);
                        Utils.putValue(MainActivity.this, Constants.OfLineEmployeeList, OfLineEmployeeList);
                        resultLogin(userName,password,data);
                    }

                    @Override
                    public void onMyFailure(String content) {
                        getLoadingDialog("正在登录").dismiss();
                        Utils.showShortToast(MainActivity.this, content);
                    }
                });
            }
        } else {
            Utils.showShortToast(MainActivity.this, "请填写账号或密码！");
        }
    }

    private void resultLogin(String userName,String password,String data){
        Utils.putValue(MainActivity.this, Constants.UserInfo, data);
        Utils.putBooleanValue(MainActivity.this,
                Constants.LoginState, true);
        UserUtils.getUserModel(MainActivity.this);
        Utils.putValue(MainActivity.this, Constants.NAME, userName);
        Utils.putValue(MainActivity.this, Constants.PWD,
                password);
        Intent intent = new Intent(MainActivity.this,
                IndexActivity.class);
        startActivity(intent);
        et_password.setText("");
    }

    public FlippingLoadingDialog getLoadingDialog(String msg) {
        if (mLoadingDialog == null)
            mLoadingDialog = new FlippingLoadingDialog(this, msg);
        return mLoadingDialog;
    }

}
