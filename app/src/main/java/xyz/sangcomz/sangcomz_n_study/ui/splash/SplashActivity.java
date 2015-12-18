package xyz.sangcomz.sangcomz_n_study.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;
import xyz.sangcomz.sangcomz_n_study.core.common.GlobalApplication;
import xyz.sangcomz.sangcomz_n_study.util.Utils;

public class SplashActivity extends BaseActivity {

    LinearLayout areaLogo;
    LinearLayout areaLogin;

    Button btnLogin;
    Button btnjoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GlobalApplication.setCurrentActivity(this);


        areaLogo = (LinearLayout) findViewById(R.id.area_logo);
        areaLogin = (LinearLayout) findViewById(R.id.area_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnjoin = (Button) findViewById(R.id.btn_join);
        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectJoinActivity();
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                animLogo();
            }
        }, 2000);

    }

    private void animLogo() {
        ViewCompat.animate(areaLogo)
                .setDuration(1000).withStartAction(new Runnable() {
            @Override
            public void run() {
                animLogin();
            }
        }).translationY(Utils.convertDP(this, -150)).start();
    }
    private void animLogin() {
        ViewCompat.animate(areaLogin)
                .alpha(1)
                .setDuration(1000).translationY(Utils.convertDP(this, -125)).start();
    }
}
