package com.example.newminerisk;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.newminerisk.bean.IdentificationEvaluation;
import com.example.newminerisk.net.NetClient;
import com.example.newminerisk.tools.UserUtils;

/**
 * 辨识评估
 */
public class IndentificationEvaluationActivity extends AppCompatActivity {
    private static final String TAG = "IndentificationEvaluationActivity";
    protected NetClient netClient;
    private TextView txtLeft;
    private ImageView imgLeft;
    private TextView txtTitle;
    private ImageView imgRight;
    private TextView txtRight;
    private TextView tvKuangquName;
    private TextView tvRiskPlace;
    private TextView tvRiskContent;
    private TextView tvDsName;
    private TextView tvPossibility;
    private TextView tvExposureRate;
    private TextView tvResult;
    private TextView tvRiskScore;
    private TextView ivRiskGName;
    private TextView tvMeasures;
    private TextView tvDutyDeptName;
    private TextView tvDutyPersonName;
    private TextView tvControlTeamName;
    private TextView tvControlTime;
    private TextView tvPersonsControl;
    private TextView tvStartTime;
    private TextView tvEndTime;
    private TextView tvOpenType;
    private TextView tvRecordTime;
    private IdentificationEvaluation record;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idenfitication_evaluation);
        netClient = new NetClient(IndentificationEvaluationActivity.this);
        initView();
        setView();
        initdate();
    }

    private void setView() {
        txtTitle.setText("评估详情");
        final Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("recordBund");
        record = (IdentificationEvaluation) bundle.getSerializable("hiddenDangerRecord");
    }

    private void initView() {
        txtLeft = findViewById(R.id.txt_left);
        imgLeft = findViewById(R.id.img_left);
        txtTitle = findViewById(R.id.txt_title);
        imgRight = findViewById(R.id.img_right);
        txtRight = findViewById(R.id.txt_right);
        tvKuangquName = findViewById(R.id.tv_kuangqu_name);
        tvRiskPlace = findViewById(R.id.tv_risk_place);
        tvRiskContent = findViewById(R.id.tv_risk_content);
        tvDsName = findViewById(R.id.tv_ds_name);
        tvPossibility = findViewById(R.id.tv_possibility);
        tvExposureRate = findViewById(R.id.tv_exposure_rate);
        tvResult = findViewById(R.id.tv_result);
        tvRiskScore = findViewById(R.id.tv_riskScore);
        ivRiskGName = findViewById(R.id.tv_risk_gname);
        tvMeasures = findViewById(R.id.tv_measures);
        tvDutyDeptName = findViewById(R.id.tv_duty_dept_name);
        tvDutyPersonName = findViewById(R.id.tv_duty_person_name);
        tvControlTeamName = findViewById(R.id.tv_control_team_name);
        tvControlTime = findViewById(R.id.tv_control_time);
        tvPersonsControl = findViewById(R.id.tv_persons_control);
        tvStartTime = findViewById(R.id.tv_start_time);
        tvEndTime = findViewById(R.id.tv_end_time);
        tvOpenType = findViewById(R.id.tv_open_type);
        tvRecordTime = findViewById(R.id.tv_record_time);
    }

    private void initdate(){
        tvKuangquName.setText(record.getKuangQuName());
        tvRiskPlace.setText(record.getRiskPlace());
        tvRiskContent.setText(record.getRiskContent());
        tvDsName.setText(record.getDsName());
        tvPossibility.setText(record.getPossibility());
        tvExposureRate.setText(record.getExposureRate());
        tvResult.setText(record.getResult());
        tvRiskScore.setText(record.getRiskScore());
        tvMeasures.setText(record.getMeasures());
        ivRiskGName.setText(record.getRiskGname());
        tvDutyDeptName.setText(record.getDutyDeptName());
        tvDutyPersonName.setText(record.getDutyPersonName());
        tvControlTeamName.setText(record.getControlTeamName());
        String year = record.getControlYear();
        if(record.getControlYear()==null){
            year = "";
        }
        String Months = record.getControlMonths();
        if(record.getControlMonths()==null){
            Months = "";
        }
        tvControlTime.setText(year+Months);
        tvPersonsControl.setText(record.getPersonsControl());
        tvStartTime.setText(record.getStarTime());
        tvEndTime.setText(record.getEndTime());
        tvRecordTime.setText(record.getRecordTime());
        String openType = record.getOpenType();
        String username = UserUtils.getUserName(IndentificationEvaluationActivity.this);
        String userRolea = UserUtils.getUserRoleids(IndentificationEvaluationActivity.this);
        if(!TextUtils.isEmpty(openType)){
            if("1".equals(openType)){
                openType = "开启";
            }else{
                openType = "关闭";
            }
        }
        tvOpenType.setText(openType);
    }

    public void onBack(View view) {
        onBackPressed();
    }

}
