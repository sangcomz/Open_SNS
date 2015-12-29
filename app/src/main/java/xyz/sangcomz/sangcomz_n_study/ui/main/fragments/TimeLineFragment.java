package xyz.sangcomz.sangcomz_n_study.ui.main.fragments;


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

import xyz.sangcomz.sangcomz_n_study.Adapter.PostAdapter;
import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.bean.Post;
import xyz.sangcomz.sangcomz_n_study.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends Fragment {

    TimeLineController timeLineController;
    RecyclerView recyclerView;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();
    RelativeLayout areaNoData;
    NoDataController noDataController;

    SwipeRefreshLayout swipeRefreshLayout;

    int curPage = 1;

    public TimeLineFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_time_line, container, false);
        timeLineController = new TimeLineController(getActivity(), this);
        swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh);

        areaNoData = (RelativeLayout) rootView.findViewById(R.id.area_nodata);
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_public_black_24dp, getString(R.string.msg_no_search));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        timeLineController.GetPost(curPage);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                timeLineController.GetPost(curPage);
            }
        });

        return rootView;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
        if (posts.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            postAdapter = new PostAdapter(getActivity(), posts);
            recyclerView.setAdapter(postAdapter);
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

}
