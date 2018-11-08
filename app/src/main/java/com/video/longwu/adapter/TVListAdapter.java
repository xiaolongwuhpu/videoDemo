package com.video.longwu.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.video.longwu.R;
import com.video.longwu.bean.TVListBean;

import java.util.List;

public class TVListAdapter extends BaseQuickAdapter<TVListBean,BaseViewHolder> {

    public TVListAdapter(@LayoutRes int layoutResId, List<TVListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, TVListBean item) {
        helper.setText(R.id.tv_name,item.getTvName());
    }
}
