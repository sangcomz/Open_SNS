package xyz.sangcomz.open_sns.ui.main.fragments.friends;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.ui.main.MainActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class FriendsFragment extends Fragment {
    ViewPager viewPager;
    MainActivity mainActivity;
    FollowFragmentAdapter followFragmentAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_friends, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        mainActivity = (MainActivity) getActivity();
        followFragmentAdapter = new FollowFragmentAdapter(getChildFragmentManager());
//        followFragmentAdapter = new FollowFragmentAdapter();

        setUpViewPager(viewPager, followFragmentAdapter);
        mainActivity.setupTabinViewPager(viewPager);

        return rootView;
    }
    /**
     * viewPager에 adapter를 설정해준다.
     */
    public void setUpViewPager(ViewPager viewPager, FollowFragmentAdapter followFragmentAdapter) {
        followFragmentAdapter.addFragment(new FollowerFragment(), getString(R.string.txt_follower)); //adapter에 Fragment를 더해준다.
        followFragmentAdapter.addFragment(new FollowingFragment(), getString(R.string.txt_following)); //adapter에 Fragment를 더해준다.
        viewPager.setAdapter(followFragmentAdapter);
    }


    //http://blog.daum.net/mailss/19 FragmentPagerAdapter 설명
    public class FollowFragmentAdapter extends FragmentPagerAdapter {
        private final List<android.support.v4.app.Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public FollowFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(android.support.v4.app.Fragment fragment, String title) {
            mFragments.add(fragment); //받은 프레그먼트를 리스트에 더해준다.
            mFragmentTitles.add(title);//받은 String을 리스트에 더해준다.
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
