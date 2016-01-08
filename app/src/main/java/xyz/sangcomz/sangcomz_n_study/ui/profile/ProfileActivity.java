package xyz.sangcomz.sangcomz_n_study.ui.profile;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;
import xyz.sangcomz.sangcomz_n_study.core.common.view.DeclareView;

public class ProfileActivity extends BaseActivity {

    @DeclareView(id = R.id.toolbar)
    Toolbar toolbar;

    @DeclareView(id = R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @DeclareView(id = R.id.appbar)
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile, true);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getString(R.string.txt_timeline));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout.setTitle(getString(R.string.txt_timeline));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent, null));
        else
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
