package xyz.sangcomz.open_sns.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.core.SharedPref.SharedPref;
import xyz.sangcomz.open_sns.define.SharedDefine;
import xyz.sangcomz.open_sns.ui.comment.CommentActivity;
import xyz.sangcomz.open_sns.ui.main.MainActivity;
import xyz.sangcomz.open_sns.ui.post.PostController;
import xyz.sangcomz.open_sns.ui.profile.ProfileActivity;
import xyz.sangcomz.open_sns.util.Utils;
import xyz.sangcomz.open_sns.util.custom.RoundedImageView;
import xyz.sangcomz.open_sns.util.custom.SquareImageView;

/**
 * Created by sangc on 2015-12-28.
 */
public class PostAdapter
        extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    ArrayList<Post> posts = new ArrayList<>();
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final RoundedImageView rivProfile;
        public final TextView txtMemberName;
        public final TextView txtDate;
        public final SquareImageView sivPostImage;
        public final TextView txtContent;
        public final TextView txtCommentCount;
        public final LinearLayout areaComment;
        public final RelativeLayout areaMore;


        public ViewHolder(View view) {
            super(view);
            rivProfile = (RoundedImageView) view.findViewById(R.id.riv_profile);
            txtMemberName = (TextView) view.findViewById(R.id.txt_member_name);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            sivPostImage = (SquareImageView) view.findViewById(R.id.siv_post_image);
            txtContent = (TextView) view.findViewById(R.id.txt_content);
            txtCommentCount = (TextView) view.findViewById(R.id.txt_comment_count);
            areaComment = (LinearLayout) view.findViewById(R.id.area_comment);
            areaMore = (RelativeLayout) view.findViewById(R.id.area_more);
        }
    }

    public PostAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(posts.get(position).getMemberProfile()).centerCrop().into(holder.rivProfile);
        Glide.with(context).load(posts.get(position).getPostImage()).centerCrop().into(holder.sivPostImage);
        holder.txtMemberName.setText(posts.get(position).getMemberName());
        holder.txtContent.setText(posts.get(position).getPostContent());
        holder.txtCommentCount.setText(posts.get(position).getPostCommentCount());
        holder.txtDate.setText(Utils.getDateString(holder.txtDate.getContext(),
                "yyyy.MM.dd", Integer.parseInt(posts.get(position).getPostDate())));

        holder.areaComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = (MainActivity) context;
                Intent intent = new Intent(holder.areaComment.getContext(), CommentActivity.class);
                intent.putExtra("post_srl", posts.get(position).getPostSrl());
                activity.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_top_to_bottom, R.anim.slide_top_to_bottom);

            }
        });

        holder.rivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent intent = new Intent(context, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("member_srl", posts.get(position).getMemberSrl());
                context.startActivity(intent);
            }
        });

        holder.areaMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(holder.areaMore.getContext(), holder.areaMore, Gravity.BOTTOM);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.menu_more_post, popup.getMenu());

                MenuItem menuDelete = popup.getMenu().findItem(R.id.action_delete);

                if (posts.get(position).getMemberSrl().equals(new SharedPref(context).getStringPref(SharedDefine.SHARED_MEMBER_SRL)))
                    menuDelete.setVisible(true);
                else
                    menuDelete.setVisible(false);

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id){
                            case R.id.action_delete:
                                PostController.deletePost(context, posts.get(position).getPostSrl());
                                break;
                            case R.id.action_share:
                                break;
                        }
                        return false;
                    }

                });
                popup.show();
            }
        });

//        setBtnColor(followMembers.get(position).getFollowYN(), holder.btnFollow);
//        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (followMembers.get(position).getFollowYN().equals("Y")) {
//                    followController.Follow(followMembers.get(position).getMemberSrl(), false, position);
//                } else {
//                    followController.Follow(followMembers.get(position).getMemberSrl(), true, position);
//                }
//            }
//        });


    }


    public void deleteMember(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }
}