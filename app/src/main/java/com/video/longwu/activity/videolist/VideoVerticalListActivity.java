package com.video.longwu.activity.videolist;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.video.longwu.R;
import com.video.longwu.adapter.VideoListAdapter;
import com.video.longwu.bean.TVListBean;
import com.video.longwu.config.VideoUrl;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoVerticalListActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_vertical_list);
        ButterKnife.bind(this);
        for(int i=0;i<30;i++){
            tvlistBean.add(new TVListBean("我是"+i,VideoUrl.url));
        }
    }
    VideoListAdapter adapter;
    private List<TVListBean> tvlistBean = new ArrayList<>();
    private void initRecycle() {
        adapter = new VideoListAdapter(R.layout.item_video_player, tvlistBean);
        recycleview.setLayoutManager(new LinearLayoutManager(this));
        recycleview.addItemDecoration(new DividerDecoration(Color.GRAY, 1));
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

    }

}
