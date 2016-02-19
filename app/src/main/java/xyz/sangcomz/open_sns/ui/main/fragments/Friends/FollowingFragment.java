package xyz.sangcomz.open_sns.ui.main.fragments.friends;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import de.greenrobot.event.EventBus;
import rx.Observer;
import rx.subjects.PublishSubject;
import xyz.sangcomz.open_sns.adapter.FollowAdapter;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.FollowMember;
import xyz.sangcomz.open_sns.util.ItemDecoration.DividerItemDecoration;
import xyz.sangcomz.open_sns.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    FollowController followController;
    FollowAdapter followAdapter;
    ArrayList<FollowMember> followMembers = new ArrayList<>();
    RecyclerView recyclerView;

    RelativeLayout areaNoData;
    NoDataController noDataController;

    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;

    FriendsFragment friendsFragment;

    int curPage = 1;
    int totalPage;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public static PublishSubject<FollowMember> refreshFollowPublishSubject;

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_following, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));

        areaNoData = (RelativeLayout) rootView.findViewById(R.id.area_nodata);
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_people_black_24dp, getString(R.string.msg_no_following));

        followController = new FollowController(this, followAdapter);
        followController.GetFollow(true, curPage++, this);
        // Inflate the layout for this fragment

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followMembers.clear();
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                followController.GetFollow(true, curPage++, FollowingFragment.this);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (curPage <= totalPage) {
                        followController.GetFollow(true, curPage++, FollowingFragment.this);
                    }
                }

            }
        });

        refreshFollowPublishSubject = PublishSubject.create();

        /**
         * data - >0 == memberSrl , 1 == true or false
         */
        refreshFollowPublishSubject.subscribe(new Observer<FollowMember>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(FollowMember followMember) {
                if (followMember.getFollowYN().equals("Y")) {
                    followMembers.add(0, followMember);
                    followAdapter.notifyDataSetChanged();
                    if (areaNoData.getVisibility() == View.VISIBLE)
                        areaNoData.setVisibility(View.GONE);
                } else {
                    for (int i = 0; i < followMembers.size(); i++) {
                        if (followMembers.get(i).getMemberSrl().equals(followMember.getMemberSrl())) {
                            followMembers.remove(i);
                            followAdapter.notifyItemRemoved(i);
                            followAdapter.notifyItemRangeChanged(i, followMembers.size());
                            if (!(followMembers.size() > 0))
                                areaNoData.setVisibility(View.VISIBLE);
                            break;
                        }
                    }
                }


            }
        });
        return rootView;
    }

    public void setFollowMembers(ArrayList<FollowMember> followMembers) {
        this.followMembers = followMembers;
        if (followMembers.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            followAdapter = new FollowAdapter(this, followMembers, true);
            recyclerView.setAdapter(followAdapter);

        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(String con) {
        /* Do something */
        if (!con.contains("FollowingFragment")) {
            followMembers.clear();
            curPage = 1;
            swipeRefreshLayout.setRefreshing(false);
            followController.GetFollow(true, curPage++, FollowingFragment.this);
        }
    }

}
