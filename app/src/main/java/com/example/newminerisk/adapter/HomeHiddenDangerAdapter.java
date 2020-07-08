package com.example.newminerisk.adapter;

import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.newminerisk.R;
import com.example.newminerisk.bean.GroupCount;
import com.example.newminerisk.tools.Constants;
import com.example.newminerisk.tools.OnItemClickListener;

import java.util.List;

public class HomeHiddenDangerAdapter extends RecyclerView.Adapter {
    private static final String TAG = "HomeHiddenDangerAdapter";
    List<GroupCount> recordList;
    String type = "";
    private boolean isShowMoreButton = true;
    private int needShowMoreButtonNum = 3;
    private OnItemClickListener itemClickListener;

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public HomeHiddenDangerAdapter() {
    }

    public HomeHiddenDangerAdapter(List<GroupCount> recordList,String type) {
        this.recordList = recordList;
        this.type = type;
        //if(type.equals("0")){
            needShowMoreButtonNum = recordList.size();
        //}else{
            //needShowMoreButtonNum = 3;
        //}
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == Constants.TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hidden_danger_statistics_of_each_unit, parent, false);
            return new ViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).mLlTop.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            ((ViewHolder) holder).mLlTopView.setVisibility(position == 0 ? View.VISIBLE : View.GONE);
            if(type.equals("0")){
                ((ViewHolder) holder).mTvName01.setText(recordList.get(position).getCollieryName());
            }else{
                ((ViewHolder) holder).mTvName01.setText(recordList.get(position).getKuangQuName());
            }
            ((ViewHolder) holder).mTvProcessedNum01.setText(recordList.get(position).getCloseNum());
            ((ViewHolder) holder).mTvUntreatedNum01.setText(recordList.get(position).getOpenNum());
            ((ViewHolder) holder).mTvTotalNum01.setText(Integer.parseInt(recordList.get(position).getCloseNum())+Integer.parseInt(recordList.get(position).getOpenNum())+"");
            ((ViewHolder) holder).mTvName01.setSelected(true);
            ((ViewHolder) holder).mTvProcessedNum01.setSelected(true);
            ((ViewHolder) holder).mTvUntreatedNum01.setSelected(true);
            ((ViewHolder) holder).ll_into_detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, position, -1);
                    }
                }
            });

            ((ViewHolder) holder).mTvProcessedNum01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, position, 1);
                    }
                }
            });

            ((ViewHolder) holder).mTvUntreatedNum01.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onItemClick(v, position, 2);
                    }
                }
            });
        } else if (holder instanceof FootViewHolder) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isShowMoreButton = false;
                    notifyItemRangeChanged(needShowMoreButtonNum + 1, recordList.size() - needShowMoreButtonNum);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return recordList.size() > needShowMoreButtonNum && isShowMoreButton ? needShowMoreButtonNum + 1 : recordList.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        private LinearLayoutCompat mLlTop;
        private LinearLayoutCompat ll_into_detail;
        private View mLlTopView;
        private TextView mTvName01;
        private TextView mTvProcessedNum01;
        private TextView mTvUntreatedNum01;
        private TextView mTvTotalNum01;

        ViewHolder(View view) {
            super(view);
            mLlTop = view.findViewById(R.id.ll_top);
            ll_into_detail = view.findViewById(R.id.ll_into_detail);
            mLlTopView = view.findViewById(R.id.ll_top_view);
            mTvName01 = view.findViewById(R.id.tv_name_01);
            mTvProcessedNum01 = view.findViewById(R.id.tv_processed_num_01);
            mTvUntreatedNum01 = view.findViewById(R.id.tv_untreated_num_01);
            mTvTotalNum01 = view.findViewById(R.id.tv_total_num_01);

        }
    }

    static class FootViewHolder extends RecyclerView.ViewHolder {

        FootViewHolder(View view) {
            super(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position != 0 && (isShowMoreButton && recordList.size() > needShowMoreButtonNum && position > needShowMoreButtonNum - 1)) {
            return Constants.TYPE_FOOTER;
        } else {
            return Constants.TYPE_ITEM;
        }
    }


}
