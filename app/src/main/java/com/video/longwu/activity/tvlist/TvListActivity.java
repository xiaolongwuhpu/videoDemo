package com.video.longwu.activity.tvlist;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jude.easyrecyclerview.decoration.DividerDecoration;
import com.video.longwu.R;
import com.video.longwu.activity.textureview.SimpleTextureViewActivity;
import com.video.longwu.adapter.TVListAdapter;
import com.video.longwu.bean.TVListBean;
import com.video.longwu.util.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TvListActivity extends AppCompatActivity {

    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    private List<String> tvlist;
    private List<TVListBean> tvlistBean = new ArrayList<>();
    TVListAdapter adapter = null;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_list);
        ButterKnife.bind(this);
        mContext = this;
        try {
            InputStream is = getAssets().open("chanel.txt");
            tvlist = FileUtil.ReadAssetsTxt(is);
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (String str : tvlist) {
            String[] strs = str.split(",");
            if(strs.length==2){
                tvlistBean.add(new TVListBean(strs[0], strs[1]));
            }
        }
        initRecycle();
    }

    private void initRecycle() {
        adapter = new TVListAdapter(R.layout.tv_item_layout, tvlistBean);
        recycleview.setLayoutManager(new GridLayoutManager(this, 3));
        recycleview.addItemDecoration(new DividerDecoration(Color.GRAY, 1));
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(mContext, SimpleTextureViewActivity.class);
                intent.putExtra("url", tvlistBean.get(position).getTvUrl());
                startActivity(intent);
            }
        });

    }


}
