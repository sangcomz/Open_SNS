package xyz.sangcomz.sangcomz_n_study.ui.main.fragments;

import android.content.Context;

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
import xyz.sangcomz.sangcomz_n_study.bean.Post;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;

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

    public void GetPost(int page) {
        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("page", page);

        HttpClient.get(UrlDefine.URL_GET_POST, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("posts");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<Post>>() {
                    }.getType();
                    List<Post> posts = (List<Post>) gson.fromJson(jsonOutput, listType);


                    timeLineFragment.setPosts((ArrayList<Post>) posts);

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
