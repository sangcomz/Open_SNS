package xyz.sangcomz.open_sns.ui.comment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.adapter.CommentAdapter;
import xyz.sangcomz.open_sns.bean.Comment;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.ui.main.fragments.timeline.TimeLineFragment;
import xyz.sangcomz.open_sns.util.ItemDecoration.DividerItemDecoration;
import xyz.sangcomz.open_sns.util.NoDataController;

public class CommentActivity extends BaseActivity implements View.OnClickListener {


    @DeclareView(id = R.id.area_background)
    FrameLayout areaBackground;
    @DeclareView(id = R.id.area_send, click = "this")
    RelativeLayout areaSend;
    @DeclareView(id = R.id.img_back, click = "this")
    ImageView imgBack;
    @DeclareView(id = R.id.et_comment)
    EditText etComment;
    @DeclareView(id = R.id.recyclerview)
    RecyclerView recyclerView;
    @DeclareView(id = R.id.area_nodata)
    RelativeLayout areaNoData;
    @DeclareView(id = R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefreshLayout;

    CommentController commentController;
    String postSrl;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments = new ArrayList<>();
    NoDataController noDataController;
    LinearLayoutManager linearLayoutManager;

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    int curPage = 1;

    private int totalPage;

    private int totalCommentCount;

    int position;//-1이면 다른곳에서 호출

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment, true);

        setAreaBackgroundColor(Color.BLACK);


        linearLayoutManager = new LinearLayoutManager(this);
        commentController = new CommentController(this, etComment, areaNoData);
        initAreaNoData();

        recyclerView.setLayoutManager(linearLayoutManager);

        commentAdapter = new CommentAdapter(this, comments, commentController);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        postSrl = getIntent().getStringExtra("post_srl");
        position = getIntent().getIntExtra("position", -1);

        commentController.getComment(postSrl, curPage++, true);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(false);
                curPage = 1;
                commentController.getComment(postSrl, curPage++, true);
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
                        commentController.getComment(postSrl, curPage++, false);
                    }
                }

            }
        });
    }

    protected void initAreaNoData() {
        noDataController = new NoDataController(areaNoData, this);
        noDataController.setNodata(R.drawable.ic_action_ic_comment_black_24dp, getString(R.string.msg_no_comment));
    }


    @Override
    public void finish() {
//        Intent i = new Intent();
//        i.putExtra("position", position);
//        i.putExtra("comment_count", totalCommentCount);
//        setResult(RESULT_OK, i);
        ArrayList<String> post = new ArrayList<>();
        post.add(String.valueOf(position));
        post.add(String.valueOf(totalCommentCount));
        post.add(postSrl);

        TimeLineFragment.refreshPostPublishSubject.onNext(post);
        super.finish();
        setAreaBackgroundColor(Color.TRANSPARENT);
        overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.slide_bottom_to_top);
    }

    private void setAreaBackgroundColor(final int color) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        areaBackground.setBackgroundColor(color);
                    }
                });
            }
        }, 500);
    }


    public void setComments(ArrayList<Comment> comments, boolean isReset) {
        if (isReset) {
            this.comments.clear();
        }
        this.comments.addAll(comments);
        if (comments.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            commentAdapter.notifyDataSetChanged();
            if (isReset)
                recyclerView.scrollToPosition(0);
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }


    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }


    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }


    public void setTotalCommentCount(int totalCommentCount) {
        this.totalCommentCount = totalCommentCount;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.img_back:
                finish();
                break;
            case R.id.area_send:
                commentController.addComment(postSrl, etComment.getText().toString());
                break;

        }
    }
}
