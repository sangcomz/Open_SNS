package xyz.sangcomz.open_sns.ui.splash;

import android.app.ProgressDialog;
import android.view.Window;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.common.GlobalApplication;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;
import xyz.sangcomz.open_sns.util.Utils;

import static xyz.sangcomz.open_sns.core.Security.Security.MD5;

/**
 * Created by sangc on 2015-12-23.
 */
public class SplashController {

    SplashActivity splashActivity;

    SplashController(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    protected void Login(String memberSrl) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(splashActivity, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();
        params.put("member_srl", memberSrl);

        System.out.println("params :::: " + params.toString());

        HttpClient.post(UrlDefine.URL_ACCOUNT_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, final JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    int stat = response.getInt("stat");
                    if (stat == 1) {
                        setProfile(response);
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//
//                                    JSONObject jsonObject = null;
//
//                                    jsonObject = response.getJSONObject("response");
//                                    (new SharedPref(splashActivity)).setMemberPref(jsonObject.getString("member_srl"),
//                                            jsonObject.getString("member_name"),
//                                            jsonObject.getString("member_profile"),
//                                            jsonObject.getString("member_profile_bg"));
//
//                                    (new SharedPref(splashActivity)).setSettings(jsonObject.getString("setting_push_on_off"),
//                                            jsonObject.getString("setting_searchable"));
//
//                                    GlobalApplication.setDrawableBg((Utils.drawableFromUrl(splashActivity,
//                                            (new SharedPref(splashActivity)).getStringPref(SharedDefine.SHARED_MEMBER_PROFILE_BG))));
//
//                                    splashActivity.redirectMainActivity();
//                                    splashActivity.finish();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();
                    } else {
                        splashActivity.animLogo();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
            }
        });
    }

    protected void Login(String memberName, String memberPassword) {

        RequestParams params = new RequestParams();

        params.put("member_name", memberName);

        params.put("member_pwd", MD5(memberPassword, "").toLowerCase(Locale.getDefault()));

        HttpClient.post(UrlDefine.URL_ACCOUNT_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    int stat = response.getInt("stat");

                    if (stat == 1) {

                        setProfile(response);
//                        JSONObject jsonObject = null;
//
//                        jsonObject = response.getJSONObject("response");
//                        (new SharedPref(splashActivity)).setMemberPref(jsonObject.getString("member_srl"),
//                                jsonObject.getString("member_name"),
//                                jsonObject.getString("member_profile"),
//                                jsonObject.getString("member_profile_bg"));
//
//                        (new SharedPref(splashActivity)).setSettings(jsonObject.getString("setting_push_on_off"),
//                                jsonObject.getString("setting_searchable"));
//
//                        new Thread(new Runnable() {
//                            @Override
//                            public void run() {
//                                try {
//                                    GlobalApplication.setDrawableBg((Utils.drawableFromUrl(splashActivity,
//                                            (new SharedPref(splashActivity)).getStringPref(SharedDefine.SHARED_MEMBER_PROFILE_BG))));
//
//                                    splashActivity.redirectMainActivity();
//                                    splashActivity.finish();
//                                } catch (IOException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        }).start();


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

    private void setProfile(JSONObject response){
        try {
            JSONObject jsonObject = response.getJSONObject("response");
            (new SharedPref(splashActivity)).setMemberPref(jsonObject.getString("member_srl"),
                    jsonObject.getString("member_name"),
                    jsonObject.getString("member_profile"),
                    jsonObject.getString("member_profile_bg"));

            (new SharedPref(splashActivity)).setSettings(jsonObject.getString("setting_push_on_off"),
                    jsonObject.getString("setting_searchable"));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GlobalApplication.setDrawableBg((Utils.drawableFromUrl(splashActivity,
                            (new SharedPref(splashActivity)).getStringPref(SharedDefine.SHARED_MEMBER_PROFILE_BG))));

                    splashActivity.redirectMainActivity();
                    splashActivity.finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
