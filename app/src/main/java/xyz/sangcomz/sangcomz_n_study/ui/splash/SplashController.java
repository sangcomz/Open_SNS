package xyz.sangcomz.sangcomz_n_study.ui.splash;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;

import static xyz.sangcomz.sangcomz_n_study.core.Security.Security.MD5;

/**
 * Created by sangc on 2015-12-23.
 */
public class SplashController {

    SplashActivity splashActivity;

    SplashController(SplashActivity splashActivity) {
        this.splashActivity = splashActivity;
    }

    protected void Login(String memberSrl) {

        RequestParams params = new RequestParams();
        params.put("member_srl", memberSrl);

        System.out.println("params :::: " + params.toString());

        HttpClient.post(UrlDefine.URL_ACCOUNT_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    int stat = response.getInt("stat");
                    if (stat == 1) {
                        splashActivity.redirectMainActivity();
                        splashActivity.finish();
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
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }

    protected void Login(String memberName, String memberPassword) {

        RequestParams params = new RequestParams();
        try {
            params.put("member_name", URLEncoder.encode(memberName, "UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        params.put("member_pwd", MD5(memberPassword, "").toLowerCase(Locale.getDefault()));

        HttpClient.post(UrlDefine.URL_ACCOUNT_LOGIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    int stat = response.getInt("stat");

                    if (stat == 1) {
                        JSONObject jsonObject = null;


                        jsonObject = response.getJSONObject("response");
                        (new SharedPref(splashActivity)).setMemberPref(jsonObject.getString("member_srl"),
                                jsonObject.getString("member_name"),
                                jsonObject.getString("member_profile"),
                                jsonObject.getString("member_profile_bg"));


                        splashActivity.redirectMainActivity();
                        splashActivity.finish();
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
}
