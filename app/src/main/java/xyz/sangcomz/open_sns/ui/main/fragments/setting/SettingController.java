package xyz.sangcomz.open_sns.ui.main.fragments.setting;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.view.Window;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;

/**
 * Created by sangc on 2015-12-30.
 */
public class SettingController {
    SettingFragment settingFragment;

    public SettingController(SettingFragment settingFragment) {
        this.settingFragment = settingFragment;
    }

    public void SetSettings(String pushOnOff, String searchable) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(settingFragment.getContext(), R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();
        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(settingFragment.getActivity())).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("push_on_off", pushOnOff);
        params.put("searchable", searchable);
        HttpClient.post(UrlDefine.URL_ACCOUNT_PHONE_SETTING, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
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
                progressDialog.dismiss();
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
                } catch (JSONException | PackageManager.NameNotFoundException e) {
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
