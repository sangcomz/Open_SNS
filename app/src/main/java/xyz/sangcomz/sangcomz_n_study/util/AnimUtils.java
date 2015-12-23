package xyz.sangcomz.sangcomz_n_study.util;

import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;

/**
 * 액션 INTERPOLATOR 선언해둔곳
 * INTERPOLATOR = 안드로이드에서 정의해둔 애니메이션들.
 */
public class AnimUtils {

    public static final FastOutSlowInInterpolator FAST_OUT_SLOW_IN_INTERPOLATOR
            = new FastOutSlowInInterpolator();

    public static final FastOutLinearInInterpolator FAST_OUT_LINEAR_IN_INTERPOLATOR
            = new FastOutLinearInInterpolator();

    public static final LinearOutSlowInInterpolator LINEAR_OUT_SLOW_IN_INTERPOLATOR
            = new LinearOutSlowInInterpolator();

}
