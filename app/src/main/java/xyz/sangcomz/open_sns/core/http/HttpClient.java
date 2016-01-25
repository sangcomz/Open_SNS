package xyz.sangcomz.open_sns.core.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

/**
 * Created by 석원 on 7/2/2015.
 */
public class HttpClient {

    private static final String BASE_URL = "";

    private static AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
    private static SyncHttpClient syncHttpClient = new SyncHttpClient();


    public static void post(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        asyncHttpClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        asyncHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void cancel(Context context) {
        asyncHttpClient.cancelRequests(context, true);
    }






    public static void syncPost(String url, RequestParams params, JsonHttpResponseHandler responseHandler) {
        syncHttpClient.post(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void syncGet(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        syncHttpClient.get(getAbsoluteUrl(url), params, responseHandler);
    }

    public static void syncCancel(Context context) {
        syncHttpClient.cancelRequests(context, true);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

//    /**
//     * Kakao 아이디로 가입
//     */
//    public static void KakaoJoin(final AppCompatActivity activity, final String nickName,
//                                 final String profileImg, long snsId, final Intent i) {
//
//        // 프로그레스
////        final ProgressDialog progressDialog = new ProgressDialog(Lo.this, R.style.MyTheme);
////        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
////        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
////        progressDialog.show();
//        RequestParams params = new RequestParams();
//        params.put("join_type", "KAKAO");
//        params.put("nick_name", nickName);
//        try {
//            params.put("profile_img", URLEncoder.encode(profileImg, "UTF-8"));
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        params.put("sns_id", "KAKAO_" + String.valueOf(snsId));
//        params.put("user_app_os", "android");
//        params.put("user_app_version", Utils.getVersion(activity));
//        System.out.println("param ::: " + params.toString());
//        HttpClient.post(Define.URL_ACCOUNT_JOIN, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//                System.out.println(response.toString());
//                if (response != null) {
//                    String stat = response.optString("stat");
//                    if (stat != null && stat.equals("1")) {
//                        try {
//
//                            JSONObject json = response.getJSONObject("response");
//
//                            JSONObject user = json.getJSONObject("user");
//                            JSONObject setting_alarm = json.getJSONObject("setting_alarm");
//                            JSONObject setting_basic = json.getJSONObject("setting_basic");
//
//                            JSONObject info = json.getJSONObject("info");
//                            int contents_count = info.getInt("contents_count");
//                            String follower = info.optString("follower");
//                            String following = info.optString("following");
//                            String alarm_count = info.optString("alarm_count");
//                            int note_count = info.getInt("note_count");
//                            String note_name = info.optString("note_nm");
//                            String note_code = info.optString("note_code");
//
//                            String alarm_comments = setting_alarm.optString("comments");
//                            String alarm_likes = setting_alarm.optString("likes");
//                            String alarm_follows = setting_alarm.optString("follows");
//
//                            String basic_contents = setting_basic.optString("contents");
//                            String basic_comments = setting_basic.optString("comments");
//                            String basic_scrap = setting_basic.optString("scrap");
//
//                            String joinType = user.optString("join_type");
//                            String memberSrl = user.optString("member_srl");
//                            String email = user.optString("email");
//                            String email_auth = user.optString("email_auth");
//                            String nick_name = user.optString("nick_name");
//                            String birthday = user.optString("birthday");
//                            String sex = user.optString("sex");
//                            String sns_id = user.optString("sns_id");
//                            String img_path = user.optString("profile_img");
//
//                            JSONObject memberLevelInfo = user.getJSONObject("member_level_info");
//                            String memberLevel = user.optString("member_level");
//                            String postA = memberLevelInfo.getString("post_a");
//                            String postB = memberLevelInfo.getString("post_b");
//                            String postC = memberLevelInfo.getString("post_c");
//                            String nowLevelId = memberLevelInfo.getString("now_level_id");
//                            String nowLevelKo = memberLevelInfo.getString("now_level_ko");
//                            String nowPost = memberLevelInfo.getString("now_post");
//                            String nowPostCount = memberLevelInfo.getString("now_post_count");
//                            String nextLevelKo = memberLevelInfo.getString("next_level_ko");
//                            String nextLevelId = memberLevelInfo.getString("next_level_id");
//
//
//                            SharedPreferences pref = activity.getSharedPreferences(Define.SHARED_INFO, activity.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = pref.edit();
//                            editor.putString(Define.SHARED_JOIN_TYPE, joinType);
//                            editor.putString(Define.SHARED_MEMBER_SRL, memberSrl);
//                            editor.putString(Define.SHARED_EMAIL, email);
//                            editor.putString(Define.SHARED_EMAIL_AUTH, email_auth);
//                            editor.putString(Define.SHARED_NICKNAME, nick_name);
//                            editor.putString(Define.SHARED_BIRTH, birthday);
//                            editor.putString(Define.SHARED_SEX, sex);
//                            editor.putString(Define.SHARED_SNS_ID, sns_id);
//                            editor.putString(Define.SHARED_PROFILE_PATH, img_path);
//
//                            editor.putInt(Define.SHARED_CONTENTS_COUNT, contents_count);
//                            editor.putString(Define.SHARED_FOLLOWER, follower);
//                            editor.putString(Define.SHARED_FOLLOWING, following);
//                            editor.putString(Define.SHARED_ALARM_COUNT, alarm_count);
//                            editor.putString(Define.SHARED_TUTO_NOTE_NAME, note_name);
//                            editor.putString(Define.SHARED_TUTO_NOTE_CODE, note_code);
//
//                            editor.putString(Define.SHARED_ALERT_COMMENT, alarm_comments);
//                            editor.putString(Define.SHARED_ALERT_LIKE, alarm_likes);
//                            editor.putString(Define.SHARED_ALERT_FOLLOW, alarm_follows);
//
//                            editor.putString(Define.SHARED_BASIC_POST, basic_contents);
//                            editor.putString(Define.SHARED_BASIC_COMMENT, basic_comments);
//                            editor.putString(Define.SHARED_BASIC_SCRAP, basic_scrap);
//
//                            editor.putString(Define.SHARED_MEMBER_LEVEL, memberLevel);
//                            editor.putString(Define.SHARED_POST_A, postA);
//                            editor.putString(Define.SHARED_POST_B, postB);
//                            editor.putString(Define.SHARED_POST_C, postC);
//                            editor.putString(Define.SHARED_NOW_LEVEL_ID, nowLevelId);
//                            editor.putString(Define.SHARED_NOW_LEVEL_KO, nowLevelKo);
//                            editor.putString(Define.SHARED_NOW_POST, nowPost);
//                            editor.putString(Define.SHARED_NOW_POST_COUNT, nowPostCount);
//                            editor.putString(Define.SHARED_NEXT_LEVEL_ID, nextLevelId);
//                            editor.putString(Define.SHARED_NEXT_LEVEL_KO, nextLevelKo);
//
//                            if (contents_count > 0 || note_count > 1) {
//                                editor.putBoolean(Define.SHARED_TUTO_1, true);
//                                editor.putBoolean(Define.SHARED_TUTO_2, true);
//                                editor.putBoolean(Define.SHARED_TUTO_3, true);
//                            }
//
//                            editor.commit();
//
////                            Intent i = new Intent(activity, MainActivity.class);
////                            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            activity.startActivity(i);
//
//                            activity.finish();
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//
//                    } else {
//                        JSONObject errorJson = null;
//                        try {
//                            errorJson = response.getJSONObject("error");
//                            final String errcode = errorJson.optString("code");
//                            ErrorController.HandleError(activity, errcode);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//                        UserManagement.requestLogout(new LogoutResponseCallback() {
//                            @Override
//                            public void onSuccess(final long userId) {
//
//                            }
//
//                            @Override
//                            public void onFailure(final APIErrorResult apiErrorResult) {
//                            }
//                        });
//
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                System.out.println(responseString);
//            }
//        });
//    }
}
