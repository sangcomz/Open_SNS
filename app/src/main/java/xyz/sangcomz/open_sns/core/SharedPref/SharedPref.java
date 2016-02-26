package xyz.sangcomz.open_sns.core.SharedPref;

import android.content.Context;
import android.content.SharedPreferences;

import xyz.sangcomz.open_sns.define.SharedDefine;

/**
 * Created by sangc on 2015-12-23.
 */
public class SharedPref {
    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(SharedDefine.SHARED_INFO, Context.MODE_PRIVATE);
        editor = pref.edit();


    }

    public void setMemberPref(String memberSrl, String memberName, String memberProfile, String memberProfileBg) {
        editor.putString(SharedDefine.SHARED_MEMBER_SRL, memberSrl);
        editor.putString(SharedDefine.SHARED_MEMBER_NAME, memberName);
        editor.putString(SharedDefine.SHARED_MEMBER_PROFILE, memberProfile);
        editor.putString(SharedDefine.SHARED_MEMBER_PROFILE_BG, memberProfileBg);
        editor.commit();
    }


    public void setSettings(String pushOnOff, String searchable) {
        if (pushOnOff == null)
            pushOnOff = getStringPref(SharedDefine.SHARED_PUSH_ON_OFF);
        if (searchable == null)
            pushOnOff = getStringPref(SharedDefine.SHARED_SEARCHABLE);
        editor.putString(SharedDefine.SHARED_PUSH_ON_OFF, pushOnOff);
        editor.putString(SharedDefine.SHARED_SEARCHABLE, searchable);
        editor.commit();
    }


    public String getStringPref(String key) {
        return pref.getString(key, null);
    }

    public void clearPref() {
        editor.clear();
        editor.commit();
    }


}
