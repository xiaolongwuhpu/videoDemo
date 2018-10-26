package com.video.longwu.activity.surfaceview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.video.longwu.R;
import com.video.longwu.util.ToastUtil;
import com.video.longwu.widget.testSurfaceView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class SurfaceViewActivity extends Activity {
    @BindView(R.id.btn_reset)
    Button btnReset;
    @BindView(R.id.surface_view)
    testSurfaceView surfaceView;
    @BindView(R.id.btn_delete_one_step)
    Button btnDeleteOneStep;
    @BindView(R.id.btn_reset_one_step)
    Button btnResetOneStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_surface_view);
        ButterKnife.bind(this);
        ToastUtil.showShort("开始了");

    }

    @OnClick(R.id.btn_reset)
    public void onViewClicked() {
        ToastUtil.showShort("重置画布");
        surfaceView.ressetCanvas();
    }

    @OnClick({R.id.btn_delete_one_step, R.id.btn_reset_one_step})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_delete_one_step:
                surfaceView.undo();
                break;
            case R.id.btn_reset_one_step:
                surfaceView.redo();
                break;
        }
    }
}
