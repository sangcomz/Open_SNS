package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.setting;

import android.content.pm.PackageManager;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;

/**
 * Created by sangc on 2015-12-30.
 */
public class SettingController {
    SettingFragment settingFragment;

    public SettingController(SettingFragment settingFragment) {
        this.settingFragment = settingFragment;
    }

    public void SetSettings(String pushOnOff, String searchable) {
        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(settingFragment.getActivity())).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("push_on_off", pushOnOff);
        params.put("searchable", searchable);
        HttpClient.post(UrlDefine.URL_ACCOUNT_PHONE_SETTING, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());

                try {
                    int stat = response.getInt("stat");
                    if (stat == 1) {
                        JSONObject jsonObject = response.getJSONObject("response");

                        (new SharedPref(settingFragment.getActivity())).setSettings(jsonObject.getString("setting_push_on_off"),
                                jsonObject.getString("setting_searchable"));
                        settingFragment.setSwitch();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }

    public void getAppVersion() {
        RequestParams params = new RequestParams();

        params.put("device_os", "android");

        HttpClient.get(UrlDefine.URL_ACCOUNT_GET_APP_VERSION, params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    int stat = response.getInt("stat");
                    if (stat == 1) {
                        JSONObject jsonObject = response.getJSONObject("response");
                        settingFragment.setCurVer(jsonObject.getString("version"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
            }


            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }
}
