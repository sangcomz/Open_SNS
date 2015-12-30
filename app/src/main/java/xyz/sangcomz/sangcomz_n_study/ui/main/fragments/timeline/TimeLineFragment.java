package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.timeline;


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

import xyz.sangcomz.sangcomz_n_study.adapter.PostAdapter;
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
    LinearLayoutManager linearLayoutManager;



    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int curPage = 1;

    private int totalPage;

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
        linearLayoutManager = new LinearLayoutManager(getActivity());
        areaNoData = (RelativeLayout) rootView.findViewById(R.id.area_nodata);
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_public_black_24dp, getString(R.string.msg_no_post));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(getActivity(), posts);
        recyclerView.setAdapter(postAdapter);

        timeLineController.GetPost(curPage++);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                timeLineController.GetPost(curPage++);
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
                        timeLineController.GetPost(curPage++);
                    }
                }

            }
        });

        return rootView;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts.addAll(posts);
        if (posts.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            postAdapter.notifyDataSetChanged();
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }
}
