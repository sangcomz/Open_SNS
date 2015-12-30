package xyz.sangcomz.sangcomz_n_study.ui.comment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;

public class CommentActivity extends BaseActivity {

    FrameLayout areaBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        areaBackground = (FrameLayout) findViewById(R.id.area_background);
        setAreaBackgroundColor(Color.BLACK);


    }

    @Override
    public void finish() {
        super.finish();
        setAreaBackgroundColor(Color.TRANSPARENT);
        overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_bottom_to_top);
    }

    private void setAreaBackgroundColor(final int color) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        areaBackground.setBackgroundColor(color);
                    }
                });
            }
        }, 500);
    }
}
