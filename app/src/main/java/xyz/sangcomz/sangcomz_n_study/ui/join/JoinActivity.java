package xyz.sangcomz.sangcomz_n_study.ui.join;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.makeramen.roundedimageview.RoundedImageView;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;

public class JoinActivity extends BaseActivity {
//    Toolbar toolbar;

    JoinController joinController;
    RoundedImageView roundedImageView;
    EditText etNickName;
    EditText etPassword;
    EditText etRePassword;
    Button btnJoin;

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

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkEt())
                    return;

                joinController.Join(etNickName.getText().toString(), etPassword.getText().toString());
            }
        });

    }

    private boolean checkEt() {
        if (etNickName.getText().toString().equals("")) {
            return false;
        } else if (etPassword.getText().toString().equals("")) {
            return false;
        } else if (etRePassword.getText().toString().equals("")) {
            return false;
        } else if (!(etPassword.getText().toString().equals(etRePassword.getText().toString()))) {
            return false;
        } else
            return true;
    }
}
