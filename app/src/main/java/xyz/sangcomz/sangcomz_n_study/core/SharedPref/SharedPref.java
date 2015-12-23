package xyz.sangcomz.sangcomz_n_study.core.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;

/**
 * Created by sangc on 2015-12-23.
 */
public class SharedPref {
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(SharedDefine.SHARED_INFO, context.MODE_PRIVATE);
        editor = pref.edit();
    }

    public void setMemberPref(String memberSrl, String memberName, String memberProfile, String memberProfileBg) {
        editor.putString(SharedDefine.SHARED_MEMBER_SRL, memberSrl);
        editor.putString(SharedDefine.SHARED_MEMBER_NAME, memberName);
        editor.putString(SharedDefine.SHARED_MEMBER_PROFILE, memberProfile);
        editor.putString(SharedDefine.SHARED_MEMBER_PROFILE_BG, memberProfileBg);
        editor.commit();
    }

    public String getStringPref(String key) {
        return pref.getString(key, null);
    }
}
