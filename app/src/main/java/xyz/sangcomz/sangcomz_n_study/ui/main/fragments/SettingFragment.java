package xyz.sangcomz.sangcomz_n_study.ui.main.fragments;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.content.IntentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.ui.splash.SplashActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingFragment extends Fragment {

    Button btnLogout;


    public SettingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_setting, container, false);
        btnLogout = (Button) rootView.findViewById(R.id.btn_logout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                (new SharedPref(getActivity())).clearPref();
                goSplashActivity(getActivity());

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }


    public void goSplashActivity(Activity activity) {
        Intent intent = new Intent(activity, SplashActivity.class);
        ComponentName cn = intent.getComponent();
        Intent mainIntent = IntentCompat.makeRestartActivityTask(cn);
        activity.startActivity(mainIntent);
    }
}
