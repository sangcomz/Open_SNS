package xyz.sangcomz.open_sns.core.common;

import android.support.v4.app.Fragment;
import android.view.View;

import xyz.sangcomz.open_sns.core.common.view.ViewMapper;

/**
 * Created by sangc on 2016-01-06.
 */
public class BaseFragment extends Fragment {
    protected void bindView(View view) {
        ViewMapper.mapLayout(this, view);
    }
}
