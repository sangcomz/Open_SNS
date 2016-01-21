package xyz.sangcomz.open_sns.ui.main.fragments.search;

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
import xyz.sangcomz.open_sns.bean.FollowMember;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;

/**
 * Created by sangc on 2015-12-28.
 */
public class SeachController {
    Context context;
    SearchFriendFragment searchFriendFragment;

    public SeachController(Context context, SearchFriendFragment searchFriendFragment) {
        this.context = context;
        this.searchFriendFragment = searchFriendFragment;
    }


    public void SearchMember(String query, int page) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();

        params.put("query", query);

        params.put("member_srl", (new SharedPref(context)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("page", page);
        HttpClient.get(UrlDefine.URL_SEARCH, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();
                System.out.println("onSuccess JSONObject :::: " + response.toString());

                try {
                    JSONArray jsonArray = response.getJSONArray("follow_members");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<FollowMember>>() {
                    }.getType();
                    List<FollowMember> followMembers = (List<FollowMember>) gson.fromJson(jsonOutput, listType);

                    searchFriendFragment.setFollowMembers((ArrayList<FollowMember>) followMembers);
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
