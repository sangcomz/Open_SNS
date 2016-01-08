package xyz.sangcomz.sangcomz_n_study.ui.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;
import xyz.sangcomz.sangcomz_n_study.core.common.view.DeclareView;

public class AddPostActivity extends BaseActivity implements View.OnClickListener {

    @DeclareView(id = R.id.toolbar)
    Toolbar toolbar;
    @DeclareView(id = R.id.area_photo, click = "this")
    RelativeLayout areaPhoto;
    @DeclareView(id = R.id.img_post)
    ImageView imgPost;
    @DeclareView(id = R.id.et_content)
    EditText etContent;

    Bitmap bitPost = null;

    AddPostController addPostController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post, true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.txt_add_post));
        addPostController = new AddPostController(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent imageData) {
        super.onActivityResult(requestCode, resultCode, imageData);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    ArrayList<String> path = imageData.getStringArrayListExtra(Define.INTENT_PATH);
                    Glide
                            .with(this)
                            .load(path.get(0))
                            .asBitmap()
                            .override(600, 600)
                            .centerCrop()
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                    bitPost = resource;
                                    imgPost.setImageBitmap(bitPost);
                                }
                            });
                    //You can get image path(ArrayList<String>
                    break;
                }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_done) {
            if (bitPost != null && etContent.getText().toString().length() > 0) {
                addPostController.AddPost(etContent.getText().toString(), bitPost);
            } else {
                Snackbar.make(etContent, getString(R.string.msg_insert_image_or_content), Snackbar.LENGTH_SHORT).show();
            }

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.area_photo:
                FishBun
                        .with(AddPostActivity.this)
                        .setCamera(true)
                        .setActionBarColor(Color.parseColor("#009688"), Color.parseColor("#00796B"))
                        .setPickerCount(1)
                        .startAlbum();
                break;
        }

    }
}
