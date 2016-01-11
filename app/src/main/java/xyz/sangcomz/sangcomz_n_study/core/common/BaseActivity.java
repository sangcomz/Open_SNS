package xyz.sangcomz.sangcomz_n_study.core.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.common.view.ViewMapper;
import xyz.sangcomz.sangcomz_n_study.ui.join.JoinActivity;
import xyz.sangcomz.sangcomz_n_study.ui.main.MainActivity;
import xyz.sangcomz.sangcomz_n_study.ui.profile.ProfileActivity;

/**
 * @author leoshin, created at 15. 7. 20..
 */
public class BaseActivity extends AppCompatActivity {
    protected static Activity self;
    protected static SharedPref sharedPref;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);
    }

    public void setContentView(int layoutResID, boolean isViewMap) {
        if (isViewMap == true) {
            setContentView(layoutResID);
            ViewMapper.mapLayout(this, getWindow().getDecorView());
        } else {
            setContentView(layoutResID);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalApplication.setCurrentActivity(this);
        self = BaseActivity.this;
    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = GlobalApplication.getCurrentActivity();
        if (currActivity != null && currActivity.equals(this)) {
            GlobalApplication.setCurrentActivity(null);
        }
    }

    public void redirectMainActivity() {
        final Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    protected void redirectJoinActivity() {
        final Intent intent = new Intent(this, JoinActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
    }

    protected void redirectProfileActivity(String memberSrl) {
        final Intent intent = new Intent(this, ProfileActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra("member_srl", memberSrl);
        startActivity(intent);
    }
}
