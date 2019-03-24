package com.example.newminerisk.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newminerisk.IndentificationEvaluationActivity;
import com.example.newminerisk.R;
import com.example.newminerisk.bean.IdentificationEvaluation;

import java.util.List;

public class IdentificationEvaluationAdapter extends RecyclerView.Adapter {

    Context context;
    List<IdentificationEvaluation> hiddenDangerRecordList;
    String datatype;

    public IdentificationEvaluationAdapter(List<IdentificationEvaluation> recordList, Context context, String datatype) {
            this.hiddenDangerRecordList = recordList;
            this.context = context;
            this.datatype = datatype;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_identification_evaluatioin, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        String tvKuangquName = hiddenDangerRecordList.get(position).getKuangQuName();
        String tvRiskContent = hiddenDangerRecordList.get(position).getRiskContent();
        String tvRiskPlace = hiddenDangerRecordList.get(position).getRiskPlace();
        String tvDutyDeptName = hiddenDangerRecordList.get(position).getDutyDeptName();
        String tvDutyPersonName = hiddenDangerRecordList.get(position).getDutyPersonName();
        String tvControlTeamName = hiddenDangerRecordList.get(position).getControlTeamName();
        String riskGname = hiddenDangerRecordList.get(position).getRiskGname();
        ((ViewHolder) holder).tvKuangquName.setText(tvKuangquName);
        ((ViewHolder) holder).tvRiskContent.setText(tvRiskContent);
        ((ViewHolder) holder).tvRiskPlace.setText(tvRiskPlace);
        ((ViewHolder) holder).tvDutyDeptName.setText(tvDutyDeptName);
        ((ViewHolder) holder).tvDutyPersonName.setText(tvDutyPersonName);
        ((ViewHolder) holder).tvControlTeamName.setText(tvControlTeamName);
        ((ViewHolder) holder).tvRiskGrade.setText(riskGname);
        ((IdentificationEvaluationAdapter.ViewHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(),
                        IndentificationEvaluationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hiddenDangerRecord", hiddenDangerRecordList.get(position));
                intent.putExtra("recordBund", bundle);
                intent.putExtra("datatype", datatype);
                Activity activity = findActivity(context);
                activity.startActivityForResult(intent,10);
            }
        });
    }

    @Nullable
    public static Activity findActivity(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            ContextWrapper wrapper = (ContextWrapper) context;
            return findActivity(wrapper.getBaseContext());
        } else {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return hiddenDangerRecordList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvKuangquName;
        private TextView tvRiskContent;
        private TextView tvRiskPlace;
        private TextView tvDutyDeptName;
        private TextView tvDutyPersonName;
        private TextView tvControlTeamName;
        private TextView tvRiskGrade;
        private TextView tvRecordTime;

        ViewHolder(View view) {
            super(view);

            tvKuangquName = view.findViewById(R.id.tv_kuangqu_name);
            tvRiskContent = view.findViewById(R.id.tv_risk_content);
            tvRiskPlace = view.findViewById(R.id.tv_risk_place);
            tvDutyDeptName = view.findViewById(R.id.tv_duty_dept_name);
            tvDutyPersonName = view.findViewById(R.id.tv_duty_person_name);
            tvControlTeamName = view.findViewById(R.id.tv_control_team_name);
            tvRiskGrade = view.findViewById(R.id.tv_risk_grade);
            tvRecordTime = view.findViewById(R.id.tv_record_time);
        }
    }
}