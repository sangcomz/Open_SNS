package xyz.sangcomz.open_sns.ui.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.core.common.BaseActivity;
import xyz.sangcomz.open_sns.core.common.view.DeclareView;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.ui.comment.CommentActivity;
import xyz.sangcomz.open_sns.util.Utils;
import xyz.sangcomz.open_sns.util.custom.RoundedImageView;
import xyz.sangcomz.open_sns.util.custom.SquareImageView;

public class PostActivity extends BaseActivity implements View.OnClickListener {
    @DeclareView(id = R.id.toolbar)
    Toolbar toolbar;

    @DeclareView(id = R.id.riv_profile, click = "this")
    protected RoundedImageView rivProfile;

    @DeclareView(id = R.id.txt_member_name)
    protected TextView txtMemberName;

    @DeclareView(id = R.id.txt_date)
    protected TextView txtDate;

    @DeclareView(id = R.id.siv_post_image)
    protected SquareImageView sivPostImage;

    @DeclareView(id = R.id.txt_content)
    protected TextView txtContent;

    @DeclareView(id = R.id.txt_comment_count)
    protected TextView txtCommentCount;

    @DeclareView(id = R.id.area_comment, click = "this")
    protected LinearLayout areaComment;

    @DeclareView(id = R.id.area_more, click = "this")
    protected RelativeLayout areaMore;

    PostController postController;
    boolean isPush;

    Post post = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post, true);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.txt_view_post));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        postController = new PostController(this);

        isPush = getIntent().getBooleanExtra("is_push", false);

    }

    @Override
    protected void onResume() {
        super.onResume();

        postController.getPost(getIntent().getStringExtra("post_srl"));
        if (isPush) {
            areaComment.performClick();
            isPush = false;
        }
    }

    protected void setPost(final Post post) {
        this.post = post;
        Glide.with(this).load(post.getMemberProfile()).centerCrop().into(rivProfile);
        Glide.with(this).load(post.getPostImage()).centerCrop().into(sivPostImage);
        txtMemberName.setText(post.getMemberName());
        txtContent.setText(post.getPostContent());
        txtCommentCount.setText(post.getPostCommentCount());
        txtDate.setText(Utils.getDateString(txtDate.getContext(),
                "yyyy.MM.dd", Integer.parseInt(post.getPostDate())));
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.area_comment:
                Intent intent = new Intent(this, CommentActivity.class);
                intent.putExtra("post_srl", getIntent().getStringExtra("post_srl"));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_top_to_bottom, R.anim.slide_top_to_bottom);
                break;

            case R.id.area_more:
                PopupMenu popup = new PopupMenu(areaMore.getContext(), areaMore, Gravity.BOTTOM);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_more_post, popup.getMenu());

                MenuItem menuDelete = popup.getMenu().findItem(R.id.action_delete);

                if (post.getMemberSrl().equals(new SharedPref(this).getStringPref(SharedDefine.SHARED_MEMBER_SRL)))
                    menuDelete.setVisible(true);
                else
                    menuDelete.setVisible(false);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {
                            case R.id.action_delete:
                                PostController.deletePost(PostActivity.this, post.getPostSrl());
                                break;
                            case R.id.action_share:
                                break;
                        }
                        return false;
                    }

                });
                popup.show();
                break;

//            case R.id.riv_profile:
//                intent = new Intent(this, ProfileActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.putExtra("member_srl", post.getMemberSrl());
//                startActivity(intent);
//                break;

        }
    }

//    holder.areaMore.setOnClickListener(new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            PopupMenu popup = new PopupMenu(holder.areaMore.getContext(), holder.areaMore, Gravity.BOTTOM);
//            //Inflating the Popup using xml file
//            popup.getMenuInflater().inflate(R.menu.menu_more_post, popup.getMenu());
//
//            MenuItem menuDelete = popup.getMenu().findItem(R.id.action_delete);
//
//            if (posts.get(position).getMemberSrl().equals(new SharedPref(context).getStringPref(SharedDefine.SHARED_MEMBER_SRL)))
//                menuDelete.setVisible(true);
//            else
//                menuDelete.setVisible(false);
//
//            //registering popup with OnMenuItemClickListener
//            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                @Override
//                public boolean onMenuItemClick(MenuItem item) {
//                    int id = item.getItemId();
//                    switch (id){
//                        case R.id.action_delete:
//                            PostController.deletePost(context, posts.get(position).getPostSrl());
//                            break;
//                        case R.id.action_share:
//                            break;
//                    }
//                    return false;
//                }
//
//            });
//            popup.show();
//        }
//    });

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_add_post, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home)
            finish();

        return super.onOptionsItemSelected(item);
    }
}
