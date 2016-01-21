package xyz.sangcomz.open_sns.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.mikepenz.crossfadedrawerlayout.view.CrossfadeDrawerLayout;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.MiniDrawer;
import com.mikepenz.materialdrawer.adapter.BaseDrawerAdapter;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.DrawerUIUtils;
import com.mikepenz.materialize.util.UIUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.GlobalApplication;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.ui.main.fragments.friends.FriendsFragment;
import xyz.sangcomz.open_sns.ui.main.fragments.search.SearchFriendFragment;
import xyz.sangcomz.open_sns.ui.main.fragments.setting.SettingFragment;
import xyz.sangcomz.open_sns.ui.main.fragments.timeline.TimeLineFragment;
import xyz.sangcomz.open_sns.ui.post.AddPostActivity;
import xyz.sangcomz.open_sns.util.AnimUtils;
import xyz.sangcomz.open_sns.util.Utils;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @DeclareView(id = R.id.appbar)
    AppBarLayout appBarLayout;
    @DeclareView(id = R.id.toolbar)
    Toolbar toolbar;
    @DeclareView(id = R.id.tabs)
    TabLayout tabLayout;
    @DeclareView(id = R.id.area_fragment)
    FrameLayout areaFragment;
    @DeclareView(id = R.id.fab, click = "this")
    FloatingActionButton fab;

    Fragment timeLineFragment;
    Fragment friendsFragment;
    Fragment searchFriendFragment;
    Fragment settingFragment;
//    Fragment profileFragment;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;
    private MiniDrawer miniResult = null;
    private CrossfadeDrawerLayout crossfadeDrawerLayout = null;
    private IProfile profile = null;

    int curPosition = 1;
//    Fragment curFragment = null;

//    SearchView searchView;

    MainController mainController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main, true);
        mainController = new MainController(this);
        tabLayout.setSelectedTabIndicatorHeight(Utils.convertDP(this, 4));

        timeLineFragment = new TimeLineFragment();
        friendsFragment = new FriendsFragment();
        searchFriendFragment = new SearchFriendFragment();
        settingFragment = new SettingFragment();
//        profileFragment = new ProfileFragment();
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.txt_timeline));


        setFragment(1);
        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        try {
            profile = new ProfileDrawerItem().withName(URLDecoder.decode(sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_NAME), "UTF-8"))
                    .withIcon(sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_PROFILE));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(GlobalApplication.getDrawableBg()) //펼쳤을때 백그라운드 색
                .addProfiles(profile)
                .withSelectionListEnabledForSingleProfile(false)
                .withSavedInstance(savedInstanceState)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        redirectProfileActivity(sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_SRL));
                        return false;
                    }
                })
                .build();

        //create the CrossfadeDrawerLayout which will be used as alternative DrawerLayout for the Drawer
        crossfadeDrawerLayout = new CrossfadeDrawerLayout(this);

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDrawerLayout(crossfadeDrawerLayout)
                .withHasStableIds(true)
                .withDrawerWidthDp(72)
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(getString(R.string.txt_timeline)).withIcon(R.drawable.ic_public_black_24dp).withIdentifier(1),
                        new PrimaryDrawerItem().withName(getString(R.string.txt_friends)).withIcon(R.drawable.ic_people_black_24dp).withIdentifier(2),
                        new PrimaryDrawerItem().withName(getString(R.string.txt_search_friend)).withIcon(R.drawable.ic_search_black_24dp).withIdentifier(3),
                        new PrimaryDrawerItem().withName(getString(R.string.txt_setting)).withIcon(R.drawable.ic_settings_black_24dp).withIdentifier(4)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

//                        System.out.println("position :::: " + position);
                        setFragment(position);
//                        if (drawerItem instanceof Nameable) {
//                            Toast.makeText(MainActivity.this, ((Nameable) drawerItem).getName().getText(MainActivity.this), Toast.LENGTH_SHORT).show();
//                        }

                        //IMPORTANT notify the MiniDrawer about the onItemClick
                        return miniResult.onItemClick(drawerItem);
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //define maxDrawerWidth
        crossfadeDrawerLayout.setMaxWidthPx(DrawerUIUtils.getOptimalDrawerWidth(this));
        //add second view (which is the miniDrawer)
        miniResult = new MiniDrawer()
                .withDrawer(result)
                .withOnMiniDrawerItemClickListener(new BaseDrawerAdapter.OnClickListener() {
                    @Override
                    public void onClick(View v, int position, IDrawerItem item) {
//                        System.out.println("mini :::: " + position);
                        result.setSelectionAtPosition(position);
//                        setFragment(position);

                        crossfadeDrawerLayout.closeDrawers();
//                        if (position == 0) {
//                            crossfadeDrawerLayout.crossfade();
//                        } else {
//                            crossfadeDrawerLayout.closeDrawers();
//                        }

                    }
                })
                .withAccountHeader(headerResult);
        //build the view for the MiniDrawer
        View view = miniResult.build(this);
        //set the background of the MiniDrawer as this would be transparent
        view.setBackgroundColor(UIUtils.getThemeColorFromAttrOrRes(this, com.mikepenz.materialdrawer.R.attr.material_drawer_background, com.mikepenz.materialdrawer.R.color.material_drawer_background));
        //we do not have the MiniDrawer view during CrossfadeDrawerLayout creation so we will add it here
        crossfadeDrawerLayout.getSmallView().addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //define the crossfader to be used with the miniDrawer. This is required to be able to automatically toggle open / close
        miniResult.withCrossFader(new ICrossfader() {
            @Override
            public void crossfade() {
                boolean isFaded = isCrossfaded();
                crossfadeDrawerLayout.crossfade(400);

                //only close the drawer if we were already faded and want to close it now
                if (isFaded) {
                    result.getDrawerLayout().closeDrawer(GravityCompat.START);
                }
            }

            @Override
            public boolean isCrossfaded() {
                return crossfadeDrawerLayout.isCrossfaded();
            }
        });

//        handleIntent(getIntent());

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (curPosition == 1) {
                    if (verticalOffset < 0)
                        animFab(0);
                    else
                        animFab(1);

                }

            }
        });


    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    public void setupTabinViewPager(ViewPager viewPager) {
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setFragment(int position) {
        curPosition = position;
        invalidateOptionsMenu();
        switch (position) {
            case 0:
                redirectProfileActivity(sharedPref.getStringPref(SharedDefine.SHARED_MEMBER_SRL));
                break;
            case 1:
                animFab(1);
                tabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.txt_timeline));
                getSupportFragmentManager().beginTransaction().replace(areaFragment.getId(), timeLineFragment).commit();//초기화
                break;
            case 2:
                animFab(0);
                tabLayout.setVisibility(View.VISIBLE);
                getSupportActionBar().setTitle(getString(R.string.txt_friends));
                getSupportFragmentManager().beginTransaction().replace(areaFragment.getId(), friendsFragment).commit();//초기화
                break;
            case 3:
                animFab(0);
                tabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.txt_search_friend));
                getSupportFragmentManager().beginTransaction().replace(areaFragment.getId(), searchFriendFragment).commit();//초기화
                break;
            case 4:
                animFab(0);
                tabLayout.setVisibility(View.GONE);
                getSupportActionBar().setTitle(getString(R.string.txt_setting));
                getSupportFragmentManager().beginTransaction().replace(areaFragment.getId(), settingFragment).commit();//초기화
                break;
        }
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


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (curPosition == 3) {
            getMenuInflater().inflate(R.menu.menu_main, menu);

            // Associate searchable configuration with the SearchView
            SearchManager searchManager =
                    (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView =
                    (SearchView) menu.findItem(R.id.menu_search).getActionView();
            searchView.setSearchableInfo(
                    searchManager.getSearchableInfo(getComponentName()));
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
//                    System.out.println("onQueryTextSubmit :::: " + query);
                    ((SearchFriendFragment) searchFriendFragment).searchMember(query);
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });

        } else {
            return false;
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.fab:
                Intent i = new Intent(MainActivity.this, AddPostActivity.class);
                startActivity(i);
                break;

        }
    }
}
