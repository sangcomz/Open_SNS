package xyz.sangcomz.open_sns.ui.splash;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.GlobalApplication;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.util.Utils;

public class SplashActivity extends BaseActivity implements View.OnClickListener {

    SplashController splashController;

    @DeclareView(id = R.id.area_logo)
    LinearLayout areaLogo;
    @DeclareView(id = R.id.area_login)
    LinearLayout areaLogin;
    @DeclareView(id = R.id.et_nick_name)
    EditText etName;
    @DeclareView(id = R.id.et_password)
    EditText etPassword;
    @DeclareView(id = R.id.btn_login, click = "this")
    Button btnLogin;
    @DeclareView(id = R.id.btn_join, click = "this")
    Button btnjoin;

    String memberSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash, true);
        GlobalApplication.setCurrentActivity(this);
        splashController = new SplashController(this);


        memberSrl = sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_SRL);

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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                splashController.Login(etName.getText().toString(), etPassword.getText().toString());
                break;

            case R.id.btn_join:
                redirectJoinActivity();
                break;
        }
    }
}
