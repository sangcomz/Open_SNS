package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.friends;

import android.content.Context;
import android.support.v4.app.Fragment;

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
import xyz.sangcomz.sangcomz_n_study.adapter.FollowAdapter;
import xyz.sangcomz.sangcomz_n_study.bean.Member;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;

/**
 * Created by sangc on 2015-12-28.
 */
public class FollowController {
    Context context;
    FollowAdapter followAdapter;

    public FollowController(Context context, FollowAdapter followAdapter) {
        this.context = context;
        this.followAdapter = followAdapter;
    }

    public void Follow(String followMemberSrl, final boolean isFollow, final int position) {
        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("follow_member_srl", followMemberSrl);

        String url;
        if (isFollow)
            url = UrlDefine.URL_FOLLOW;
        else
            url = UrlDefine.URL_UNFOLLOW;
        HttpClient.post(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                if (isFollow)
                    followAdapter.refreshFollowYn(position, "Y");
                else
                    followAdapter.refreshFollowYn(position, "N");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }


    public void GetFollow(final boolean isFollow, int page, final Fragment fragment) {
        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        if (isFollow)
            params.put("mode", "following");
        else
            params.put("mode", "follower");
        params.put("page", page);

        HttpClient.get(UrlDefine.URL_GET_FOLLOW, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONArray jsonArray = response.getJSONArray("members");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<Member>>() {
                    }.getType();
                    List<Member> members = (List<Member>) gson.fromJson(jsonOutput, listType);

                    if (isFollow)
                        ((FollowingFragment) fragment).setMembers((ArrayList<Member>) members);
                    else
                        ((FollowerFragment) fragment).setMembers((ArrayList<Member>) members);

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
