package xyz.sangcomz.sangcomz_n_study.ui.post;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class AddPostActivity extends BaseActivity {

    Toolbar toolbar;
    RelativeLayout areaPhoto;
    ImageView imgPost;
    Bitmap bitPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.txt_add_post));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imgPost = (ImageView) findViewById(R.id.img_post);

        areaPhoto = (RelativeLayout) findViewById(R.id.area_photo);
        areaPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FishBun
                        .with(AddPostActivity.this)
                        .setCamera(true)
                        .setActionBarColor(Color.parseColor("#009688"), Color.parseColor("#00796B"))
                        .setPickerCount(1)
                        .startAlbum();
            }
        });
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
        else if (id==R.id.action_done) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    //    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        if (id == R.id.action_ok) {
//            if (pickedImageBeans.size() == 0) {
//                Toast.makeText(this, getString(R.string.msg_no_slected), Toast.LENGTH_SHORT).show();
////                Snackbar.make(recyclerView, getString(R.string.msg_no_slected), Snackbar.LENGTH_SHORT).show();
//            } else {
//                ArrayList<String> path = new ArrayList<>();
//                for (int i = 0; i < pickedImageBeans.size(); i++) {
//                    path.add(pickedImageBeans.get(i).getImgPath());
//                }
//                Intent i = new Intent();
//                i.putStringArrayListExtra(Define.INTENT_PATH, path);
//                setResult(RESULT_OK, i);
//                finish();
//            }
//            return true;
//        } else if (id == android.R.id.home)
//            finish();
//        return super.onOptionsItemSelected(item);
//    }

}
