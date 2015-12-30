package xyz.sangcomz.sangcomz_n_study.ui.join;

import android.graphics.Bitmap;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
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
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }


//    public static void isDangolPosting(final Context context, final String contentsSrl,
//                                       final boolean isDangol, final ImageView btnDangol, final boolean isList) {
//
//
//        SharedPreferences pref = context.getSharedPreferences(Define.SHARED_INFO, context.MODE_PRIVATE);
//        String memberSrl = pref.getString(Define.SHARED_MEMBER_SRL, null);
//
//        final ProgressDialog progressDialog = new ProgressDialog(context, R.style.MyTheme);
//        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
//////        progressDialog.setCancelable(false);
//        progressDialog.show();
//
//        RequestParams params = new RequestParams();
//        params.put("member_srl", memberSrl);
//        params.put("contents_srl", contentsSrl);
//
//        System.out.println("params :::: " + params);
//
//        String url;
//        if (isDangol) url = Define.URL_DANGOL;
//        else url = Define.URL_UNDANGOL;
//
//        HttpClient.post(url, params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
//
////                if (progressDialog != null && progressDialog.isShowing()) {
////                    progressDialog.dismiss();
////                }
//                System.out.println("성공? isDangolPosting " + response.toString());
//
//
//                if (response != null) {
//                    String stat = response.optString("stat");
//                    if (stat != null && stat.equals("1")) {
////                                TextView a = (TextView) activity.findViewById(R.id.txt_view);
////                                a.setText(finalTitle);
//                        if (isDangol) btnDangol.setImageResource(R.mipmap.ic_cancel_dangol);
//                        else btnDangol.setImageResource(R.mipmap.ic_add_dangol);
//
//                        CommonAplication commonAplication = (CommonAplication) context.getApplicationContext();
//                        commonAplication.isDangol = true;
//                        commonAplication.isRefresh = true;
//
//                        if (isDangol) {
////                            Toast.makeText(context, "단골맛집에 등록됐습니다.", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(btnDangol, "단골맛집에 등록됐습니다.", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();
//                        } else {
////                            Toast.makeText(context, "단골맛집 등록이 취소됐습니다.", Toast.LENGTH_SHORT).show();
//                            Snackbar.make(btnDangol, "단골맛집 등록이 취소됐습니다.", Snackbar.LENGTH_LONG)
//                                    .setAction("Action", null).show();
//                        }
//
//                        if (!isList)
//                            DetailPostActivity.switchDangol(isDangol);
//                        else {
//                            List<Fragment> fragments;
//                            if (context.toString().contains("MainActivity")) {
//                                fragments = ((MainActivity) context).getSupportFragmentManager().getFragments();
//                                for (int i = 0; i < fragments.size(); i++) {
//                                    if (fragments.get(i).toString().contains("MyNote")) {
//                                        ((MyNote) fragments.get(i)).refreshFragments(); //단골 경우 모든 화면 리프레시
//                                        ((MyNote) fragments.get(i)).refreshDangol();
//                                        MyNote.myNote.initBottomBar();
//                                    }
//
//                                }
//                            } else if (context.toString().contains("EachFolderListActivity")) {
//                                ((EachFolderListActivity) context).refreshFragments();
////                                for (int i = 0; i < fragments.size(); i++) {
////                                    if (fragments.get(i).toString().contains("MyNote"))
////                                        ((MyNote) fragments.get(i)).refreshFragments(); //자신을 제외한 fragment refresh!
////
////                                }
//                            } else if (context.toString().contains("OtherProfileActivity")) {
//                                ((OtherProfileActivity) context).refreshDangol();
//                                ((OtherProfileActivity) context).refreshFragments();
////                                for (int i = 0; i < fragments.size(); i++) {
////                                    if (fragments.get(i).toString().contains("MyNote"))
////                                        ((MyNote) fragments.get(i)).refreshFragments(); //자신을 제외한 fragment refresh!
////
////                                }
//                            }
//
//                        }
//
//                    } else {
//                        JSONObject errorJson = null;
//                        try {
//                            errorJson = response.getJSONObject("error");
//                            String errcode = errorJson.optString("code");
//                            ErrorController.HandleError(((Activity) context), errcode);
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                        Utils.showBasicDialog(context, errorJson.optString("message"));
//                    }
//
//                    if (progressDialog != null && progressDialog.isShowing()) {
//
//                        progressDialog.dismiss();
//                    }
////                    }
////
//////                        final ContentsAdapter adapter = new ContentsAdapter(activity, contentsListBeans);
////
//////                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                super.onFailure(statusCode, headers, responseString, throwable);
//                if (progressDialog != null && progressDialog.isShowing()) {
//                    progressDialog.dismiss();
//                }
////                Utils.showBasicDialog(JoinActivity.this, getResources().getString(R.string.msg_errcode_basic));
//            }
//        });
//    }
}
