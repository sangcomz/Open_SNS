package xyz.sangcomz.sangcomz_n_study.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.widget.TextView;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;
import xyz.sangcomz.sangcomz_n_study.core.common.GlobalApplication;

public class SplashActivity extends BaseActivity {

    TextView txtLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GlobalApplication.setCurrentActivity(this);

        txtLogo = (TextView) findViewById(R.id.tv_logo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                redirectMainActivity();
            }
        }, 2000);

    }

    private void animTxt() {
        ViewCompat.animate(txtLogo);
    }
}
