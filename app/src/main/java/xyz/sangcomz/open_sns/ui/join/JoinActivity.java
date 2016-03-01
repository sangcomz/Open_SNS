package xyz.sangcomz.open_sns.ui.join;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.util.custom.RoundedImageView;

public class JoinActivity extends BaseActivity {
//    Toolbar toolbar;

    JoinController joinController;
    RoundedImageView roundedImageView;
    EditText etNickName;
    EditText etPassword;
    EditText etRePassword;
    Button btnJoin;
    ImageButton btnBack;
    Bitmap bitProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);
        joinController = new JoinController(this);

        roundedImageView = (RoundedImageView) findViewById(R.id.riv_profile);
        etNickName = (EditText) findViewById(R.id.et_nick_name);
        etPassword = (EditText) findViewById(R.id.et_password);
        etRePassword = (EditText) findViewById(R.id.et_repassword);
        btnJoin = (Button) findViewById(R.id.btn_join);
        btnBack = (ImageButton) findViewById(R.id.btn_back);

        roundedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FishBun
                        .with(JoinActivity.this)
                        .setCamera(true)
                        .setActionBarColor(Color.parseColor("#009688"), Color.parseColor("#00796B"))
                        .setPickerCount(1)
                        .startAlbum();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEt())
                    return;
                joinController.Join(etNickName.getText().toString(), etPassword.getText().toString(), bitProfile);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                                    bitProfile = resource;
                                    roundedImageView.setImageBitmap(bitProfile);
                                }
                            });
                    //You can get image path(ArrayList<String>
                    break;
                }
        }
    }


    private boolean checkEt() {
        if (etNickName.getText().toString().equals("")) {
            return false;
        } else
            return !etPassword.getText().toString().equals("") &&
                    !etRePassword.getText().toString().equals("") &&
                    etPassword.getText().toString().equals(etRePassword.getText().toString());
    }
}
