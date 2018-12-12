package com.example.newminerisk;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private ImageView imageView;
    private RecyclerView recyclerView;
    private TextView title;
    private List<ExpandEntity> list;
    private ExpandAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
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
                if (list.size() == 30) {
                    adapter.loadMoreEnd();
                } else {
                    adapter.loadMoreComplete();
                }
            }
        }, recyclerView);

        adapter.setEnableLoadMore(true);

        addData();
    }

    private void addData() {
        for (int i = 0; i < 10; i++) {
            ExpandEntity entity = new ExpandEntity();
            list.add(entity);
        }
        adapter.notifyDataSetChanged();
    }
}
