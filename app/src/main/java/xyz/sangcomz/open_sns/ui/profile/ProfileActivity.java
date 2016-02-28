package xyz.sangcomz.open_sns.ui.profile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sangcomz.fishbun.FishBun;
import com.sangcomz.fishbun.define.Define;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.adapter.GridPostImageAdapter;
import xyz.sangcomz.open_sns.bean.Member;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.define.RequeDefine;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.ui.main.fragments.friends.FollowController;
import xyz.sangcomz.open_sns.util.NoDataController;
import xyz.sangcomz.open_sns.util.custom.RoundedImageView;

public class ProfileActivity extends BaseActivity implements View.OnClickListener {

    @DeclareView(id = R.id.toolbar)
    Toolbar toolbar;

    @DeclareView(id = R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @DeclareView(id = R.id.appbar)
    AppBarLayout appBarLayout;

    @DeclareView(id = R.id.profile_img, click = "this")
    RoundedImageView rivProfile;

    @DeclareView(id = R.id.txt_member_name)
    TextView txtMemberName;

    @DeclareView(id = R.id.txt_post_count)
    TextView txtPostCount;

    @DeclareView(id = R.id.txt_follower_count)
    TextView txtFollowerCount;

    @DeclareView(id = R.id.txt_following_count)
    TextView txtFollowingCount;

    @DeclareView(id = R.id.fab, click = "this")
    FloatingActionButton fab;

    @DeclareView(id = R.id.recyclerview)
    RecyclerView recyclerView;

    @DeclareView(id = R.id.backdrop)
    ImageView backDrop;

    @DeclareView(id = R.id.area_nodata)
    RelativeLayout areaNoData;

    @DeclareView(id = R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;


    private ProfileController profileController;

    private String memberSrl;


    private FollowController followController;
    private NoDataController noDataController;
    private GridPostImageAdapter gridPostImageAdapter;
    private ArrayList<Post> posts = new ArrayList<>();

    private GridLayoutManager gridLayoutManager;

    private int pastVisiblesItems, visibleItemCount, totalItemCount;

    private int curPage = 1;

    private int totalPage;

    private boolean isFollow;

    private String isMyProfile;

    private int position;


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


        profileController = new ProfileController(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent, null));
        else
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        memberSrl = getIntent().getStringExtra("member_srl");
        position = getIntent().getIntExtra("position", -1);
        profileController.getMember(memberSrl);


        gridLayoutManager = new GridLayoutManager(this, 3);
        initAreaNoData();
        curPage = 1;
        recyclerView.setLayoutManager(gridLayoutManager);
        gridPostImageAdapter = new GridPostImageAdapter(this, posts);
        recyclerView.setAdapter(gridPostImageAdapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                profileController.getMyPost(curPage++, memberSrl);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = gridLayoutManager.getChildCount();
                totalItemCount = gridLayoutManager.getItemCount();
                pastVisiblesItems = gridLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (curPage <= totalPage) {
                        profileController.getMyPost(curPage++, memberSrl);
                    }
                }

            }
        });

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0)
                    swipeRefreshLayout.setEnabled(true);
                else
                    swipeRefreshLayout.setEnabled(false);
            }
        });


        profileController.getMyPost(curPage++, memberSrl);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (memberSrl.equals(new SharedPref(this).getStringPref(SharedDefine.SHARED_MEMBER_SRL)))
            getMenuInflater().inflate(R.menu.menu_profile, menu);
        else
            getMenuInflater().inflate(R.menu.menu_null, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();
        else if (id == R.id.action_change_profile_bg) {
            FishBun
                    .with(ProfileActivity.this)
                    .setCamera(true)
                    .setActionBarColor(Color.parseColor("#009688"), Color.parseColor("#00796B"))
                    .setPickerCount(1)
                    .startAlbum();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Define.ALBUM_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    final ArrayList<String> path = data.getStringArrayListExtra(Define.INTENT_PATH);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bmProfileBg = Glide.with(getApplicationContext())
                                        .load(path.get(0))
                                        .asBitmap()
                                        .override(1200, 600)
                                        .into(1200, 600)
                                        .get();
                                profileController.setProfileBg(bmProfileBg);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    //You can get image path(ArrayList<String>
                }
                break;

            case RequeDefine.REQUEST_CODE_GET_PROFILE:
                if (resultCode == RESULT_OK) {
                    final ArrayList<String> path = data.getStringArrayListExtra(Define.INTENT_PATH);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Bitmap bmProfile = Glide.with(getApplicationContext())
                                        .load(path.get(0))
                                        .asBitmap()
                                        .override(600, 600)
                                        .into(600, 600)
                                        .get();
                                profileController.setProfile(bmProfile);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                    //You can get image path(ArrayList<String>
                }
                break;

            case RequeDefine.REQUEST_CODE_DELETE_POST:
                if (resultCode == RESULT_OK) {
                    int position = data.getIntExtra("position", -1);
                    posts.remove(position);
                    gridPostImageAdapter.notifyItemRemoved(position);
                    gridPostImageAdapter.notifyItemRangeChanged(position, posts.size());

                    if (posts.size() > 0) {
                        areaNoData.setVisibility(View.GONE);
                        gridPostImageAdapter.notifyDataSetChanged();
                    } else {
                        areaNoData.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }

    }

    protected void setProfileView(Member member) {
        isMyProfile = member.getIsMyProfile();
        String isFollow = member.getFollowYN();


        collapsingToolbarLayout.setTitle(member.getMemberName());
        txtMemberName.setText(member.getMemberName());
        txtFollowerCount.setText(member.getMemberFollowerCount());
        txtFollowingCount.setText(member.getMemberFollowingCount());
        txtPostCount.setText(member.getMemberPostCount());

        Glide.with(this)
                .load(member.getProfilePath())
                .centerCrop()
                .error(R.drawable.default_profile)
                .into(rivProfile);

        Glide.with(this)
                .load(member.getMemberProfileBg())
                .centerCrop()
                .into(backDrop);

        if (isMyProfile.equals("Y")) {
            fab.setVisibility(View.GONE); // View.INVISIBLE might also be worth trying
        } else {
            setFabGravity(isFollow);
            fab.setAlpha(1f);
        }


    }


    protected void initAreaNoData() {
        noDataController = new NoDataController(areaNoData, this);
        noDataController.setNodata(R.drawable.ic_public_black_24dp, getString(R.string.msg_no_post));
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts.addAll(posts);
        if (posts.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            gridPostImageAdapter.notifyDataSetChanged();
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    private void setFabGravity(String isFollow) {
        CoordinatorLayout.LayoutParams p = (CoordinatorLayout.LayoutParams) fab.getLayoutParams();
        p.setAnchorId(appBarLayout.getId());
        p.anchorGravity = Gravity.END | Gravity.BOTTOM;
        fab.setLayoutParams(p);
        if (isFollow.equals("Y"))
            setFollowStatus(true);
        else
            setFollowStatus(false);

    }

    protected void setFollowStatus(boolean isFollow) {
        this.isFollow = isFollow;
        if (isFollow) {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4BAD4F")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_remove_follow, getTheme()));
            } else {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_remove_follow));
            }
        } else {
            fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#303F9F")));
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_add_follow, getTheme()));
            } else {
                fab.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_add_follow));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                profileController.Follow(memberSrl, !isFollow, position);
                break;

            case R.id.profile_img:
                if (isMyProfile.equals("Y"))
                    FishBun
                            .with(ProfileActivity.this)
                            .setCamera(true)
                            .setActionBarColor(Color.parseColor("#009688"), Color.parseColor("#00796B"))
                            .setPickerCount(1)
                            .setRequestCode(RequeDefine.REQUEST_CODE_GET_PROFILE)
                            .startAlbum();


        }
    }
}
