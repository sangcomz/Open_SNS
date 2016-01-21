package xyz.sangcomz.open_sns.ui.profile;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Member;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.util.AnimUtils;
import xyz.sangcomz.open_sns.util.custom.RoundedImageView;

public class ProfileActivity extends BaseActivity {

    @DeclareView(id = R.id.toolbar)
    Toolbar toolbar;

    @DeclareView(id = R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @DeclareView(id = R.id.appbar)
    AppBarLayout appBarLayout;

    @DeclareView(id = R.id.profile_img)
    RoundedImageView rivProfile;

    @DeclareView(id = R.id.txt_member_name)
    TextView txtMemberName;

    @DeclareView(id = R.id.txt_post_count)
    TextView txtPostCount;

    @DeclareView(id = R.id.txt_follower_count)
    TextView txtFollowerCount;

    @DeclareView(id = R.id.txt_following_count)
    TextView txtFollowingCount;

    @DeclareView(id = R.id.fab)
    FloatingActionButton fab;

    @DeclareView(id = R.id.recyclerview)
    RecyclerView recyclerView;


    ProfileController profileController;

    String memberSrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile, true);
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle(getString(R.string.txt_timeline));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setBehavior(null); //should disable default animations
        p.setAnchorId(View.NO_ID); //should let you set visibility
        fab.setLayoutParams(p);

        collapsingToolbarLayout.setTitle(getString(R.string.txt_timeline));

        profileController = new ProfileController(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent, null));
        else
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        profileController.getMember(getIntent().getStringExtra("member_srl"));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_profile, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }

    protected void setProfileView(Member member) {
        String isMyProfile = member.getIsMyProfile();
        String isFollow = member.getFollowYN();
        txtMemberName.setText(member.getMemberName());
        txtFollowerCount.setText(member.getMemberFollowerCount());
        txtFollowingCount.setText(member.getMemberFollowingCount());
        txtPostCount.setText(member.getMemberPostCount());

        if (isMyProfile.equals("Y")) {
            fab.setVisibility(View.GONE); // View.INVISIBLE might also be worth trying
        }
        fab.setAlpha(1f);
    }


    /**
     * 스케일 애니메이션
     *
     * @param scale 0 = 사라짐 1 = 원래 크기
     */
    private void animFab(final float scale) {
        ViewCompat.animate(fab)
                .setInterpolator(AnimUtils.FAST_OUT_LINEAR_IN_INTERPOLATOR)
                .scaleX(scale)
                .scaleY(scale)
                .withStartAction(new Runnable() {
                    @Override
                    public void run() {
                        if (scale == 1) fab.setVisibility(View.VISIBLE);
                    }
                })
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (scale == 0) fab.setVisibility(View.GONE);
                    }
                })
                .setDuration(250)   //기간
                .withLayer()        //Software Type Hardware Type
                .start();

    }
}