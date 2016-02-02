package xyz.sangcomz.open_sns.ui.post;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import de.greenrobot.event.EventBus;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;
import xyz.sangcomz.open_sns.event.DelPostEvent;

/**
 * Created by sangcomz on 1/28/16.
 */
public class PostController {

    PostActivity postActivity;

    public PostController(PostActivity postActivity) {
        this.postActivity = postActivity;
    }

    protected void getPost(String postSrl) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(postActivity, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();


        RequestParams params = new RequestParams();

        params.put("post_srl", postSrl);

        HttpClient.get(UrlDefine.URL_GET_POST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject("post");
                    Gson gson = new Gson();
                    String jsonOutput = jsonObject.toString();

                    Type type = new TypeToken<Post>() {
                    }.getType();
                    Post post = gson.fromJson(jsonOutput, type);

                    postActivity.setPost(post);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                System.out.println("getPost onFailure responseString :::: " + throwable.toString());
            }
        });
    }


    public static void deletePost(Context context, String postSrl, final int position) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();


        RequestParams params = new RequestParams();

        params.put("post_srl", postSrl);
        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));


        HttpClient.get(UrlDefine.URL_POST_DELETE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();
                System.out.println("deletePost onSuccess JSONObject :::: " + response.toString());

                EventBus.getDefault().post(new DelPostEvent(position));
//                try {
//                    JSONObject jsonObject = response.getJSONObject("post");
//                    Gson gson = new Gson();
//                    String jsonOutput = jsonObject.toString();
//
//                    Type type = new TypeToken<Post>() {
//                    }.getType();
//                    Post post = gson.fromJson(jsonOutput, type);
//
//                    postActivity.setPost(post);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
                System.out.println("deletePost onFailure responseString :::: " + throwable.toString());
            }
        });
    }

}
