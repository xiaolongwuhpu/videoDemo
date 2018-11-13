package com.video.longwu.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.video.longwu.R;
import com.video.longwu.activity.surfaceview.SurfaceCameraActivity2;
import com.video.longwu.activity.surfaceview.SurfaceVideoActivity;
import com.video.longwu.activity.surfaceview.SurfaceViewActivity;
import com.video.longwu.activity.textureview.SimpleTextureViewActivity;
import com.video.longwu.activity.tvlist.TvListActivity;
import com.video.longwu.activity.videolist.VideoVerticalListActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FirstActivity extends AppCompatActivity {

    @BindView(R.id.surfaceview)
    Button surfaceview;
    @BindView(R.id.testpath)
    Button testpath;
    @BindView(R.id.surface_camera)
    Button surfaceCamera;
    @BindView(R.id.surface_video)
    Button surfaceVideo;
    @BindView(R.id.simpletextureview_video)
    Button simpletextureviewVideo;
    @BindView(R.id.video_list)
    Button videoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.surfaceview, R.id.testpath, R.id.surface_camera, R.id.simpletextureview_video, R.id.surface_video,R.id.video_list})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.surfaceview:
                startActivity(new Intent(FirstActivity.this, SurfaceViewActivity.class));
                break;
            case R.id.testpath:
                startActivity(new Intent(FirstActivity.this, TestPathActivity.class));
                break;
            case R.id.surface_camera:
                startActivity(new Intent(FirstActivity.this, SurfaceCameraActivity2.class));
                break;
            case R.id.surface_video:
                startActivity(new Intent(FirstActivity.this, SurfaceVideoActivity.class));
                break;
            case R.id.simpletextureview_video:
//                startActivity(new Intent(FirstActivity.this, SimpleTextureViewActivity.class));
                startActivity(new Intent(FirstActivity.this, TvListActivity.class));
                break;
                case R.id.video_list:
                startActivity(new Intent(FirstActivity.this, VideoVerticalListActivity.class));
                break;
        }
    }
}
