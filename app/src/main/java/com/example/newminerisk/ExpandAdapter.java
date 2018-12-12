package com.example.newminerisk;

import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ExpandAdapter extends BaseQuickAdapter<ExpandEntity, BaseViewHolder> {


    public ExpandAdapter(int layoutResId, @Nullable List<ExpandEntity> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final ExpandEntity item) {
        LinearLayoutCompat llexpand = helper.getView(R.id.ll_expand);
        LinearLayoutCompat clickMore = helper.getView(R.id.click_more);
        llexpand.setVisibility(item.isExpand() ? View.VISIBLE : View.GONE);
        helper.addOnClickListener(R.id.click_more);
        helper.setText(R.id.tv_status, item.isExpand() ? "收回" : "更多");
    }
}
