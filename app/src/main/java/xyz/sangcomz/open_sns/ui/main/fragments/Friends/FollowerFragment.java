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
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.adapter.FollowAdapter;
import xyz.sangcomz.open_sns.bean.FollowMember;
import xyz.sangcomz.open_sns.core.common.BaseFragment;
import xyz.sangcomz.open_sns.util.ItemDecoration.DividerItemDecoration;
import xyz.sangcomz.open_sns.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowerFragment extends BaseFragment {

    FollowController followController;
    FollowAdapter followAdapter;
    ArrayList<FollowMember> followMembers = new ArrayList<>();
    NoDataController noDataController;

    RecyclerView recyclerView;
    RelativeLayout areaNoData;
    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;

    int curPage = 1;
    int totalPage;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    public static PublishSubject<ArrayList<String>> refreshFollowPublishSubject;

    public FollowerFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_follower, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
//        followAdapter = new FollowAdapter(getActivity(), followMembers, false);
        followController = new FollowController(this, followAdapter);
        followController.GetFollow(false, 1, this);

        areaNoData = (RelativeLayout) rootView.findViewById(R.id.area_nodata);
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_people_black_24dp, getString(R.string.msg_no_follower));
        // Inflate the layout for this fragment

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                followMembers.clear();
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                followController.GetFollow(false, curPage++, FollowerFragment.this);
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
                        followController.GetFollow(false, curPage++, FollowerFragment.this);
                    }
                }

            }
        });

        refreshFollowPublishSubject = PublishSubject.create();

        /**
         * data - >0 == memberSrl , 1 == true or false
         */
        refreshFollowPublishSubject.subscribe(new Observer<ArrayList<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList<String> data) {

                if (followMembers != null && followMembers.size() > 0) {
                    for (int i = 0; i < followMembers.size(); i++) {
                        if (followMembers.get(i).getMemberSrl().equals(data.get(0))) {
                            followMembers.get(i).setFollowYN(data.get(1));
                            followAdapter.notifyItemChanged(i);
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
            followAdapter = new FollowAdapter(this, followMembers, false);
            recyclerView.setAdapter(followAdapter);
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

    public void onEvent(String con) {
        if (!con.contains("FollowerFragment")){
            followMembers.clear();
            curPage = 1;
            swipeRefreshLayout.setRefreshing(false);
            followController.GetFollow(false, curPage++, FollowerFragment.this);
        }
    }

}
