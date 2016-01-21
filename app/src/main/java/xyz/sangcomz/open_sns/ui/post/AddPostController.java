package xyz.sangcomz.open_sns.ui.post;

import android.graphics.Bitmap;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;

import cz.msebera.android.httpclient.Header;
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

        RequestParams params = new RequestParams();

        params.put("post_content", content);


        params.put("member_srl", (new SharedPref(addPostActivity)).getStringPref(SharedDefine.SHARED_MEMBER_SRL));
        File file = Utils.BitmapToFileCache(bitImage);

        try {
            params.put("post_image", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        HttpClient.post(UrlDefine.URL_POST_CREATE, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                System.out.println("onSuccess JSONObject :::: " + response.toString());
                try {
                    JSONObject jsonObject = response.getJSONObject("response");
                    addPostActivity.finish();
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
