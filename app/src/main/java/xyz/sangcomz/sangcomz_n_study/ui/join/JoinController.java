package xyz.sangcomz.sangcomz_n_study.ui.join;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.Window;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;
import xyz.sangcomz.sangcomz_n_study.util.Utils;

import static xyz.sangcomz.sangcomz_n_study.core.Security.Security.MD5;

/**
 * Created by sangc on 2015-12-20.
 */
public class JoinController {

    JoinActivity joinActivity;

    JoinController(JoinActivity joinActivity) {
        this.joinActivity = joinActivity;
    }

    public void Join(String nickName, String password, Bitmap bitProfile) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(joinActivity, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();

        params.put("member_name", nickName);

        params.put("member_pwd", MD5(password, "").toLowerCase(Locale.getDefault()));

        File file = Utils.BitmapToFileCache(bitProfile);

        try {
            params.put("member_profile", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpClient.post(UrlDefine.URL_ACCOUNT_JOIN, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject("response");
                    (new SharedPref(joinActivity)).setMemberPref(jsonObject.getString("member_srl"),
                            jsonObject.getString("member_name"),
                            jsonObject.getString("member_profile"),
                            jsonObject.getString("member_profile_bg"));

                    (new SharedPref(joinActivity)).setSettings("Y", "Y");

                    joinActivity.finish();
                    joinActivity.redirectMainActivity();
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
}
