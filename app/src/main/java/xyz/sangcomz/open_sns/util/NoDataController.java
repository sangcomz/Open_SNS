package xyz.sangcomz.open_sns.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

/**
 * Created by sangc on 2015-12-28.
 */
public class NoDataController {

    Context context;
    RelativeLayout areaNodata;

    public NoDataController(RelativeLayout areaNodata, Context context) {
        this.areaNodata = areaNodata;
        this.context = context;
    }

    public void setNodata(int imgNoData, String txtNoData) {
        for (int i = 0; i < areaNodata.getChildCount(); i++) {
            if (areaNodata.getChildAt(i) instanceof TextView)
                ((TextView) areaNodata.getChildAt(i)).setText(txtNoData);
            else if (areaNodata.getChildAt(i) instanceof ImageView)
                Glide.with(context).load(imgNoData).into((ImageView) areaNodata.getChildAt(i));
        }
    }


}
