package xyz.sangcomz.open_sns.ui.main.fragments.timeline;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;

/**
 * Created by sangc on 2015-12-29.
 */
public class TimeLineController {
    Context context;
    TimeLineFragment timeLineFragment;


    public TimeLineController(Context context, TimeLineFragment timeLineFragment) {
        this.context = context;
        this.timeLineFragment = timeLineFragment;
    }

    public void getPosts(int page) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();


        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("page", page);

        HttpClient.get(UrlDefine.URL_GET_POSTS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("posts");
                    JSONObject page = response.getJSONObject("page");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<Post>>() {
                    }.getType();
                    List<Post> posts = gson.fromJson(jsonOutput, listType);

                    timeLineFragment.setTotalPage(page.getInt("total_page"));


                    timeLineFragment.setPosts((ArrayList<Post>) posts);

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
