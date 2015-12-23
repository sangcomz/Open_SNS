package xyz.sangcomz.sangcomz_n_study.ui.main.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.sangcomz.sangcomz_n_study.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {


    public TimeLineFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_line, container, false);
    }

}
