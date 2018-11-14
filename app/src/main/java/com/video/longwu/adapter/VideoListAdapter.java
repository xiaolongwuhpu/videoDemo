package com.video.longwu.adapter;

import android.support.annotation.LayoutRes;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.video.longwu.R;
import com.video.longwu.activity.videolist.view.VideoPlayer;
import com.video.longwu.bean.VideoListBean;

import java.util.List;

public class VideoListAdapter extends BaseQuickAdapter<VideoListBean, BaseViewHolder> {
    //记录之前播放的条目下标
    public int currentPosition = -1;

    public VideoListAdapter(@LayoutRes int layoutResId, List<VideoListBean> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoListBean item) {
        int position = helper.getLayoutPosition();
        ImageView ivBg = helper.getView(R.id.iv_bg);
        RequestOptions options = new RequestOptions()
                .error(R.mipmap.video_bg)
                .centerInside()
                .fitCenter();
        Glide.with(helper.getConvertView()).load(item.getVpic()).apply(options).into(ivBg);
        helper.setText(R.id.tv_play_count, item.getPlayCount() + "万次播放");
        helper.setText(R.id.tv_comment_count, item.getCommentCount()+"");

        VideoPlayer videoPlayer = helper.getView(R.id.videoPlayer);
        videoPlayer.setDataSouresUrl(item);
        videoPlayer.mediaplayercontroller.setAdapter(this);
        videoPlayer.mediaplayercontroller.setPosition(position);
        if (position != currentPosition) {
            //设置为初始化状态
            videoPlayer.initViewDisplay();
        }
    }

    public void setPlayPosition(int position) {
        currentPosition = position;
    }
}
