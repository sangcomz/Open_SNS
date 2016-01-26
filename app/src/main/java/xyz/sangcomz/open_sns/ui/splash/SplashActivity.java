package xyz.sangcomz.open_sns.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.GlobalApplication;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.gcm.RegistrationIntentService;
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

    String deviceToken;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashActivity";

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


        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
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


    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


}
