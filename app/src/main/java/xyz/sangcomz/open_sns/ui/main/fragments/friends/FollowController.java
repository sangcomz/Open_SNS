package xyz.sangcomz.open_sns.ui.main.fragments.friends;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
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
import de.greenrobot.event.EventBus;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.adapter.FollowAdapter;
import xyz.sangcomz.open_sns.bean.FollowMember;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;

/**
 * Created by sangc on 2015-12-28.
 */
public class FollowController {
    Fragment fragment;
    FollowAdapter followAdapter;

    public FollowController(Fragment fragment, FollowAdapter followAdapter) {
        this.fragment = fragment;
        this.followAdapter = followAdapter;
    }

    public void Follow(String followMemberSrl, final boolean isFollow, final int position) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(fragment.getContext(), R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(fragment.getContext())).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
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

                EventBus.getDefault().post(fragment.toString());

                progressDialog.dismiss();
                if (isFollow)
                    followAdapter.refreshFollowYn(position, "Y");
                else
                    followAdapter.refreshFollowYn(position, "N");

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
            }
        });
    }



    public void GetFollow(final boolean isFollow, int page, final Fragment fragment) {
        RequestParams params = new RequestParams();

        params.put("member_srl", (new SharedPref(fragment.getContext())).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        if (isFollow)
            params.put("mode", "following");
        else
            params.put("mode", "follower");
        params.put("page", page);

        HttpClient.get(UrlDefine.URL_GET_FOLLOW, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONArray jsonArray = response.getJSONArray("follow_members");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<FollowMember>>() {
                    }.getType();
                    List<FollowMember> followMembers = gson.fromJson(jsonOutput, listType);

                    if (isFollow)
                        ((FollowingFragment) fragment).setFollowMembers((ArrayList<FollowMember>) followMembers);
                    else
                        ((FollowerFragment) fragment).setFollowMembers((ArrayList<FollowMember>) followMembers);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }


}
