package com.example.newminerisk;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.newminerisk.bean.HiddenDangerRecord;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ExpandAdapter extends BaseQuickAdapter<HiddenDangerRecord, BaseViewHolder> {


    public ExpandAdapter(int layoutResId, @Nullable List<HiddenDangerRecord> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final HiddenDangerRecord item) {
        TextView tv_processed_num_0 = helper.getView(R.id.tv_processed_num_0);
        TextView tv_professional = helper.getView(R.id.tv_professional);
        TextView tv_area = helper.getView(R.id.tv_area);
        TextView tv_type = helper.getView(R.id.tv_type);
        TextView tv_hazard_type = helper.getView(R.id.tv_hazard_type);
        TextView tv_time = helper.getView(R.id.tv_time);
        TextView tv_classes = helper.getView(R.id.tv_classes);
        TextView tv_hidden_danger_belongs = helper.getView(R.id.tv_hidden_danger_belongs);
        TextView tv_is_processed = helper.getView(R.id.tv_is_processed);
        tv_processed_num_0.setText(item.getTeamGroupName());
        tv_professional.setText(item.getSname());
        tv_area.setText(item.getAreaName());
        tv_type.setText(item.getJbName());
        tv_hazard_type.setText(item.getTname());
        Date date=new Date(Long.parseLong(item.getFindTime()));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timeStr = sdf.format(date);
        tv_time.setText(timeStr);
        tv_classes.setText(item.getClassName());
        tv_hidden_danger_belongs.setText(item.getHiddenBelong());
        String isHandle ="未处理";
        if (item.getIshandle().equals("1")){
            isHandle = "已处理";
        }
        tv_is_processed.setText(isHandle);
        LinearLayoutCompat llexpand = helper.getView(R.id.ll_expand);
        LinearLayoutCompat clickMore = helper.getView(R.id.click_more);
        llexpand.setVisibility(item.isExpand() ? View.VISIBLE : View.GONE);
        helper.addOnClickListener(R.id.click_more);
        helper.setText(R.id.tv_status, item.isExpand() ? "收回" : "更多");
    }
}
