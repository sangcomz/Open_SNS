package xyz.sangcomz.open_sns.ui.profile;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.FollowMember;
import xyz.sangcomz.open_sns.bean.Member;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.common.GlobalApplication;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;
import xyz.sangcomz.open_sns.ui.main.fragments.friends.FollowerFragment;
import xyz.sangcomz.open_sns.ui.main.fragments.friends.FollowingFragment;
import xyz.sangcomz.open_sns.ui.main.fragments.search.SearchFriendFragment;
import xyz.sangcomz.open_sns.util.Utils;

/**
 * Created by sangc on 2016-01-11.
 */
public class ProfileController {
    ProfileActivity profileActivity;

    public ProfileController(ProfileActivity profileActivity) {
        this.profileActivity = profileActivity;
    }


    public void getMember(String memberSrl) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(profileActivity, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(profileActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("profile_member_srl", memberSrl);
        HttpClient.get(UrlDefine.URL_ACCOUNT_GET_ACCOUNT, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                progressDialog.dismiss();

                try {
                    JSONObject jsonObject = response.getJSONObject("members");
                    Gson gson = new Gson();
                    String jsonOutput = jsonObject.toString();

                    Type type = new TypeToken<Member>() {
                    }.getType();
                    Member member = gson.fromJson(jsonOutput, type);
                    profileActivity.setProfileView(member);
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


    void setProfileBg(final Bitmap bitmap) {

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(profileActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));

        final File file = Utils.BitmapToFileCache(bitmap);

        try {
            params.put("member_profile_bg", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        HttpClient.syncPost(UrlDefine.URL_ACCOUNT_SET_PROFILE_BG, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                try {
                    JSONObject jsonObject = response.getJSONObject("response");

                    (new SharedPref(profileActivity)).setMemberPref(jsonObject.getString("member_srl"),
                            jsonObject.getString("member_name"),
                            jsonObject.getString("member_profile"),
                            jsonObject.getString("member_profile_bg"));

                    GlobalApplication.setDrawableBg((Utils.drawableFromUrl(profileActivity,
                            (new SharedPref(profileActivity)).getStringPref(SharedDefine.SHARED_MEMBER_PROFILE_BG))));

                    Gson gson = new Gson();
                    String jsonOutput = jsonObject.toString();

                    Type type = new TypeToken<Member>() {
                    }.getType();
                    final Member member = gson.fromJson(jsonOutput, type);
                    file.delete();
                    profileActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileActivity.setProfileView(member);
                        }
                    });

//                    MainActivity.refreshDrawerPublishSubject.onNext("success");

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                progressDialog.dismiss();
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }

    void getMyPost(int page, String member_srl) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(profileActivity, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();


        RequestParams params = new RequestParams();

        params.put("member_srl", member_srl);
        params.put("page", page);

        HttpClient.get(UrlDefine.URL_GET_MY_POSTS, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                progressDialog.dismiss();
                try {
                    JSONArray jsonArray = response.getJSONArray("posts");
                    JSONObject page = response.getJSONObject("page");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<Post>>() {
                    }.getType();
                    List<Post> posts = gson.fromJson(jsonOutput, listType);

                    profileActivity.setTotalPage(page.getInt("total_page"));
                    profileActivity.setPosts((ArrayList<Post>) posts);


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

    void Follow(final String followMemberSrl, final boolean isFollow, final int position) {

        // 프로그레스
        final ProgressDialog progressDialog = new ProgressDialog(profileActivity, R.style.MyProgressBarDialog);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(profileActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
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
                progressDialog.dismiss();
                profileActivity.setFollowStatus(isFollow);

                JSONObject jsonObject = null;
                try {
                    jsonObject = response.getJSONObject("follow_member");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Gson gson = new Gson();
                String jsonOutput = jsonObject.toString();

                Type listType = new TypeToken<FollowMember>() {
                }.getType();
                FollowMember followMember = gson.fromJson(jsonOutput, listType);


                ArrayList<Integer> integers = new ArrayList<>();
                integers.add(position);

                ArrayList<String> strings = new ArrayList<>();
                strings.add(followMemberSrl);

                if (isFollow) {
                    integers.add(1);
                    strings.add("Y");
                    followMember.setFollowYN("Y");
                } else {
                    integers.add(0);
                    strings.add("N");
                    followMember.setFollowYN("N");
                }


                if (SearchFriendFragment.refreshFollowPublishSubject != null)
                    SearchFriendFragment.refreshFollowPublishSubject.onNext(integers);
                if (FollowerFragment.refreshFollowPublishSubject != null)
                    FollowerFragment.refreshFollowPublishSubject.onNext(strings);
                if (FollowingFragment.refreshFollowPublishSubject != null)
                    FollowingFragment.refreshFollowPublishSubject.onNext(followMember);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                progressDialog.dismiss();
            }
        });
    }

    public void setProfile(final Bitmap bitmap) {

//        // 프로그레스
//        final ProgressDialog progressDialog = new ProgressDialog(profileActivity, R.style.MyProgressBarDialog);
//        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
//        progressDialog.show();

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(profileActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));

        final File file = Utils.BitmapToFileCache(bitmap);

        try {
            params.put("member_profile", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        HttpClient.syncPost(UrlDefine.URL_ACCOUNT_SET_PROFILE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
//                progressDialog.dismiss();
                System.out.println("onSuccess JSONObject :::: " + response.toString());

                try {
                    JSONObject jsonObject = response.getJSONObject("response");

                    (new SharedPref(profileActivity)).setMemberPref(jsonObject.getString("member_srl"),
                            jsonObject.getString("member_name"),
                            jsonObject.getString("member_profile"),
                            jsonObject.getString("member_profile_bg"));

                    GlobalApplication.setDrawableBg((Utils.drawableFromUrl(profileActivity,
                            (new SharedPref(profileActivity)).getStringPref(SharedDefine.SHARED_MEMBER_PROFILE_BG))));

                    Gson gson = new Gson();
                    String jsonOutput = jsonObject.toString();

                    Type type = new TypeToken<Member>() {
                    }.getType();
                    final Member member = gson.fromJson(jsonOutput, type);
                    file.delete();
                    profileActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            profileActivity.setProfileView(member);
                        }
                    });

//                    MainActivity.refreshDrawerPublishSubject.onNext("success");

                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
//                progressDialog.dismiss();
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }
}
