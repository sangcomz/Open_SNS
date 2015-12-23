package xyz.sangcomz.sangcomz_n_study.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;
import xyz.sangcomz.sangcomz_n_study.core.common.GlobalApplication;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.util.Utils;

public class SplashActivity extends BaseActivity {

    SplashController splashController;

    LinearLayout areaLogo;
    LinearLayout areaLogin;

    EditText etName;
    EditText etPassword;

    Button btnLogin;
    Button btnjoin;

    String memberSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        GlobalApplication.setCurrentActivity(this);
        splashController = new SplashController(this);


        areaLogo = (LinearLayout) findViewById(R.id.area_logo);
        areaLogin = (LinearLayout) findViewById(R.id.area_login);
        btnLogin = (Button) findViewById(R.id.btn_login);
        etName = (EditText) findViewById(R.id.et_nick_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                splashController.Login(etName.getText().toString(), etPassword.getText().toString());
            }
        });
        btnjoin = (Button) findViewById(R.id.btn_join);
        btnjoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectJoinActivity();
            }
        });


        memberSrl = sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_SRL);

//        try {
//            Utils.drawableFromUrl(this, sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_PROFILE));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//
////                    final Drawable drawable = (Utils.drawableFromUrl(SplashActivity.this, sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_PROFILE)));
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                            if (memberSrl != null && memberSrl.length() > 0) {
//                                splashController.Login(memberSrl);
//                            } else {
//                                animLogo();
//                            }
//
//                        }
//                    });
//
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();


        if (memberSrl != null && memberSrl.length() > 0) {
            splashController.Login(memberSrl);
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    animLogo();
                }
            }, 2000);
        }


    }

    protected void animLogo() {
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
