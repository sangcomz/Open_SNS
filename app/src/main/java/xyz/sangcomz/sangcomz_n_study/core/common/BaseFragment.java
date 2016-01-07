package xyz.sangcomz.sangcomz_n_study.core.common;

import android.support.v4.app.Fragment;
import android.view.View;

import xyz.sangcomz.sangcomz_n_study.core.common.view.ViewMapper;

/**
 * Created by sangc on 2016-01-06.
 */
public class BaseFragment extends Fragment {

//    public void onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState, boolean isViewMap) {
////        return super.onCreateView(inflater, container, savedInstanceState);
//        if (isViewMap == true) {
//            onCreateView(inflater, container, savedInstanceState);
//            ViewMapper.mapLayout(this, getView());
//        } else {
//            onCreateView(inflater, container, savedInstanceState);
//        }
//    }



//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    protected void bindView(View view) {
        ViewMapper.mapLayout(this, view);
    }
}
