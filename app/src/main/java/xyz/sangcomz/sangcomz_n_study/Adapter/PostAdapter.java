package xyz.sangcomz.sangcomz_n_study.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.bean.Post;
import xyz.sangcomz.sangcomz_n_study.ui.comment.CommentActivity;
import xyz.sangcomz.sangcomz_n_study.ui.main.MainActivity;
import xyz.sangcomz.sangcomz_n_study.util.Utils;
import xyz.sangcomz.sangcomz_n_study.util.custom.RoundedImageView;
import xyz.sangcomz.sangcomz_n_study.util.custom.SquareImageView;

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


        public ViewHolder(View view) {
            super(view);
            rivProfile = (RoundedImageView) view.findViewById(R.id.riv_profile);
            txtMemberName = (TextView) view.findViewById(R.id.txt_member_name);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            sivPostImage = (SquareImageView) view.findViewById(R.id.siv_post_image);
            txtContent = (TextView) view.findViewById(R.id.txt_content);
            txtCommentCount = (TextView) view.findViewById(R.id.txt_comment_count);
            areaComment = (LinearLayout) view.findViewById(R.id.area_comment);
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
                MainActivity mainActivity = (MainActivity) context;
                Intent intent = new Intent(holder.areaComment.getContext(), CommentActivity.class);
                mainActivity.startActivity(intent);
                mainActivity.overridePendingTransition(R.anim.slide_top_to_bottom, R.anim.slide_top_to_bottom);

            }
        });

//        setBtnColor(members.get(position).getFollowYN(), holder.btnFollow);
//        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (members.get(position).getFollowYN().equals("Y")) {
//                    followController.Follow(members.get(position).getMemberSrl(), false, position);
//                } else {
//                    followController.Follow(members.get(position).getMemberSrl(), true, position);
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