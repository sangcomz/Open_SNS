package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.setting;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.ui.splash.SplashActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    SettingController settingController;
    Button btnLogout;
    Switch swSearchable;
    Switch swPushAlarm;
    SharedPref sharedPref;

    String pushOnOff;
    String searchable;

    TextView txtCurVer;
    TextView txtDevVer;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        settingController = new SettingController(this);
        sharedPref = new SharedPref(getActivity());
        swPushAlarm = (Switch) rootView.findViewById(R.id.sw_push_alarm);
        swSearchable = (Switch) rootView.findViewById(R.id.sw_searchable);
        txtCurVer = (TextView) rootView.findViewById(R.id.txt_current_version);
        txtDevVer = (TextView) rootView.findViewById(R.id.txt_device_version);
        try {
            txtDevVer.setText("v" + getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        btnLogout = (Button) rootView.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new SharedPref(getActivity())).clearPref();
                goSplashActivity(getActivity());

            }
        });

        setSwitch();
        settingController.getAppVersion();

        swPushAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swPushAlarm.isChecked())
                    pushOnOff = "Y";
                else
                    pushOnOff = "N";
                settingController.SetSettings(pushOnOff, searchable);
            }
        });

        swSearchable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (swSearchable.isChecked())
                    searchable = "Y";
                else
                    searchable = "N";
                settingController.SetSettings(pushOnOff, searchable);
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }


    private void goSplashActivity(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        activity.startActivity(mainIntent);
    }

    protected void setSwitch() {
        pushOnOff = sharedPref.getStringPref(SharedDefine.SHARED_PUSH_ON_OFF);
        searchable = sharedPref.getStringPref(SharedDefine.SHARED_SEARCHABLE);
        if (pushOnOff.equals("Y"))
            swPushAlarm.setChecked(true);
        else
            swPushAlarm.setChecked(false);

        if (searchable.equals("Y"))
            swSearchable.setChecked(true);
        else
            swSearchable.setChecked(false);
    }

    protected void setCurVer(String ver) throws PackageManager.NameNotFoundException {
        txtCurVer.setText("v" + ver);
        String curVer = ver;
        String devVer = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;

        int val = curVer.compareTo(devVer);
        if (val > 0) {
            Snackbar.make(txtCurVer, getString(R.string.msg_need_app_version_update), Snackbar.LENGTH_INDEFINITE)
                    .setAction(getString(R.string.txt_update), new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final String appPackageName = getActivity().getPackageName(); // getPackageName() from Context or Activity object
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                            } catch (android.content.ActivityNotFoundException anfe) {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                            }
                        }
                    }).show();
        }
    }
}
