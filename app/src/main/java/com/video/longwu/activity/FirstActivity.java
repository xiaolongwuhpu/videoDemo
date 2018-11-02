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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.surfaceview, R.id.testpath, R.id.surface_camera, R.id.surface_video})
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
        }
    }
}
