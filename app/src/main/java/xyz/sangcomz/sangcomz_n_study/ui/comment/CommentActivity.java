package xyz.sangcomz.sangcomz_n_study.ui.comment;

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

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.adapter.CommentAdapter;
import xyz.sangcomz.sangcomz_n_study.bean.Comment;
import xyz.sangcomz.sangcomz_n_study.core.common.BaseActivity;
import xyz.sangcomz.sangcomz_n_study.util.ItemDecoration.DividerItemDecoration;
import xyz.sangcomz.sangcomz_n_study.util.NoDataController;

public class CommentActivity extends BaseActivity {

    CommentController commentController;
    FrameLayout areaBackground;
    RelativeLayout areaSend;

    ImageView imgBack;
    EditText etComment;
    String postSrl;

    RecyclerView recyclerView;
    CommentAdapter commentAdapter;
    ArrayList<Comment> comments = new ArrayList<>();
    RelativeLayout areaNoData;
    NoDataController noDataController;

    SwipeRefreshLayout swipeRefreshLayout;
    LinearLayoutManager linearLayoutManager;

    private int curPage = 1;
    private int totalPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        imgBack = (ImageView) findViewById(R.id.img_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        etComment = (EditText) findViewById(R.id.et_comment);
        areaSend = (RelativeLayout) findViewById(R.id.area_send);
        areaBackground = (FrameLayout) findViewById(R.id.area_background);
        setAreaBackgroundColor(Color.BLACK);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        linearLayoutManager = new LinearLayoutManager(this);
        areaNoData = (RelativeLayout) findViewById(R.id.area_nodata);
        commentController = new CommentController(this, etComment, areaNoData);
        initAreaNoData();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(linearLayoutManager);

        commentAdapter = new CommentAdapter(this, comments, commentController);
        recyclerView.setAdapter(commentAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

        postSrl = getIntent().getStringExtra("postSrl");
        areaSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commentController.addComment(postSrl, etComment.getText().toString());
            }
        });

        commentController.getComment(postSrl, curPage++, true);
    }

    protected void initAreaNoData() {
        noDataController = new NoDataController(areaNoData, this);
        noDataController.setNodata(R.drawable.ic_action_ic_comment_black_24dp, getString(R.string.msg_no_comment));
    }


    @Override
    public void finish() {
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


}
