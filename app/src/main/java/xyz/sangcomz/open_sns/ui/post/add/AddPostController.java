package xyz.sangcomz.open_sns.ui.post.add;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.Window;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.http.HttpClient;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.define.UrlDefine;
import xyz.sangcomz.open_sns.util.Utils;

/**
 * Created by sangc on 2015-12-27.
 */
public class AddPostController {

    AddPostActivity addPostActivity;

    public AddPostController(AddPostActivity addPostActivity) {
        this.addPostActivity = addPostActivity;
    }


    public void AddPost(String content, Bitmap bitImage) {

        final ProgressDialog progressDialog = new ProgressDialog(addPostActivity, R.style.MyProgressBarDialog);
        progressDialog.setCancelable(false);
        progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progressDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Small);
        progressDialog.show();

        RequestParams params = new RequestParams();

        params.put("post_content", content);


        params.put("member_srl", (new SharedPref(addPostActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        final File file = Utils.BitmapToFileCache(bitImage);

        try {
            params.put("post_image", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpClient.post(UrlDefine.URL_POST_CREATE, params, new JsonHttpResponseHandler() {
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
                    final Post post = gson.fromJson(jsonOutput, type);
                    file.delete();


                    Intent i = new Intent();
                    i.putExtra("post", post);
                    addPostActivity.setResult(Activity.RESULT_OK, i);
                    addPostActivity.finish();
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
