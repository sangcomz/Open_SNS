package xyz.sangcomz.sangcomz_n_study.ui.comment;

import android.graphics.Bitmap;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.sangcomz_n_study.adapter.CommentAdapter;
import xyz.sangcomz.sangcomz_n_study.bean.Comment;
import xyz.sangcomz.sangcomz_n_study.bean.Post;
import xyz.sangcomz.sangcomz_n_study.core.SharedPref.SharedPref;
import xyz.sangcomz.sangcomz_n_study.core.http.HttpClient;
import xyz.sangcomz.sangcomz_n_study.define.SharedDefine;
import xyz.sangcomz.sangcomz_n_study.define.UrlDefine;
import xyz.sangcomz.sangcomz_n_study.util.Utils;

import static xyz.sangcomz.sangcomz_n_study.core.Security.Security.MD5;

/**
 * Created by sangc on 2015-12-30.
 */
public class CommentController {
    CommentActivity commentActivity;
    EditText etComment;

    public CommentController(CommentActivity commentActivity, EditText etComment) {
        this.commentActivity = commentActivity;
        this.etComment = etComment;
    }


    public void addComment(String postSrl, String commentContent) {

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(commentActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("post_srl", postSrl);
        params.put("comment_content", commentContent);

        System.out.println("params :::: " + params.toString());

        HttpClient.post(UrlDefine.URL_COMMENT_CREATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject("comments");
//                    Gson gson = new Gson();
//                    String jsonOutput = jsonObject.toString();
//
//                    Type listType = new TypeToken<List<Comment>>() {
//                    }.getType();
//                    List<Comment> comments = (List<Comment>) gson.fromJson(jsonOutput, listType);

                    etComment.setText("");

                    commentActivity.setCurPage(1);
                    getComment(jsonObject.getString("post_srl"), 1, true);
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

    public void delComment(String commentSrl, final int position, final CommentAdapter commentAdapter) {

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(commentActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("comment_srl", commentSrl);


        HttpClient.post(UrlDefine.URL_COMMENT_DELETE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                commentAdapter.delComment(position);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                System.out.println("onFailure responseString :::: " + throwable.toString());
            }
        });
    }

    public void getComment(final String postSrl, int page, final boolean isReset) {

        RequestParams params = new RequestParams();


        params.put("member_srl", (new SharedPref(commentActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        params.put("post_srl", postSrl);
        params.put("page", page);


        HttpClient.get(UrlDefine.URL_COMMENT_GET, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {

                    JSONArray jsonArray = response.getJSONArray("comments");
                    JSONObject page = response.getJSONObject("page");
                    Gson gson = new Gson();
                    String jsonOutput = jsonArray.toString();

                    Type listType = new TypeToken<List<Comment>>() {
                    }.getType();
                    List<Comment> comments = (List<Comment>) gson.fromJson(jsonOutput, listType);


                    commentActivity.setComments((ArrayList<Comment>) comments, isReset);
                    commentActivity.setTotalPage(page.getInt("total_page"));
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
