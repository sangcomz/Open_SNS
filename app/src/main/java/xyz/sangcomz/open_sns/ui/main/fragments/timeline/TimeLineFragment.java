package xyz.sangcomz.open_sns.ui.main.fragments.timeline;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import rx.Subscription;
import rx.subjects.PublishSubject;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.adapter.PostAdapter;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.common.BaseFragment;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.define.RequeDefine;
import xyz.sangcomz.open_sns.event.DelPostEvent;
import xyz.sangcomz.open_sns.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeLineFragment extends BaseFragment {

    private static final String TAG = TimeLineFragment.class.getName();

    TimeLineController timeLineController;
    NoDataController noDataController;
    PostAdapter postAdapter;
    ArrayList<Post> posts = new ArrayList<>();

    public static PublishSubject<String> delPostPublishSubject;
    public static PublishSubject<ArrayList<String>> refreshPostPublishSubject;

    @DeclareView(id = R.id.recyclerview)
    RecyclerView recyclerView;
    @DeclareView(id = R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;
    @DeclareView(id = R.id.area_nodata)
    RelativeLayout areaNoData;


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
        bindView(rootView);
        timeLineController = new TimeLineController(getActivity(), this);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        initAreaNoData();
        curPage = 1;
        posts.clear();
        recyclerView.setLayoutManager(linearLayoutManager);

        postAdapter = new PostAdapter(posts, this);
        recyclerView.setAdapter(postAdapter);

        timeLineController.getPosts(curPage++);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                posts.clear();
                curPage = 1;
                swipeRefreshLayout.setRefreshing(false);
                timeLineController.getPosts(curPage++);
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
                        timeLineController.getPosts(curPage++);
                    }
                }

            }
        });

        delPostPublishSubject = PublishSubject.create();

        Subscription delSubscription = delPostPublishSubject.subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String postSrl) {
                for (int i = 0; i < posts.size(); i++) {
                    if (posts.get(i).getPostSrl().equals(postSrl)) {
                        posts.remove(i);
                        postAdapter.notifyItemRemoved(i);
                        postAdapter.notifyItemRangeChanged(i, posts.size());
                    }

                }
            }
        });

        refreshPostPublishSubject = PublishSubject.create();

        Subscription refreshSubscription = refreshPostPublishSubject.subscribe(new Observer<ArrayList<String>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ArrayList<String> infos) {
                if (Integer.parseInt(infos.get(0)) != -1) {
                    posts.get(Integer.parseInt(infos.get(0))).setPostCommentCount(infos.get(1));
                    postAdapter.notifyItemChanged(Integer.parseInt(infos.get(0)));
                } else {
                    for (int i = 0; i < posts.size(); i++) {
                        if (posts.get(i).getPostSrl().equals(infos.get(2))) {
                            posts.get(i).setPostCommentCount(infos.get(1));
                            postAdapter.notifyItemChanged(i);
                        }

                    }
                }
            }

        });

        return rootView;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequeDefine.REQUEST_CODE_CREATE_POST && resultCode == Activity.RESULT_OK) {
            posts.add(0, (Post) data.getSerializableExtra("post"));
            postAdapter.notifyDataSetChanged();
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    recyclerView.scrollToPosition(0);
                }
            });
        }
//        if (requestCode == RequeDefine.REQUEST_CODE_CHANGE_COMMENT && resultCode == Activity.RESULT_OK) {
//            int position = data.getIntExtra("position", -1);
//            int commentCount = data.getIntExtra("comment_count", -1);
//            System.out.println("position : " + position);
//            System.out.println("commentCount : " + commentCount);
//            if (position != -1 && commentCount != -1) {
//                posts.get(position).setPostCommentCount(String.valueOf(commentCount));
//                postAdapter.notifyItemChanged(position);
//            }
//        }
    }

    protected void initAreaNoData() {
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_public_black_24dp, getString(R.string.msg_no_post));
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

    public void onEvent(DelPostEvent delPostEvent) {
        posts.remove(delPostEvent.getPosition());
        postAdapter.notifyItemRemoved(delPostEvent.getPosition());
        postAdapter.notifyItemRangeChanged(delPostEvent.getPosition(), posts.size());

    }
}
