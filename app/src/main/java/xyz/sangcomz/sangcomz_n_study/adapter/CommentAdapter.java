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
import xyz.sangcomz.sangcomz_n_study.bean.Comment;
import xyz.sangcomz.sangcomz_n_study.bean.Post;
import xyz.sangcomz.sangcomz_n_study.ui.comment.CommentActivity;
import xyz.sangcomz.sangcomz_n_study.ui.comment.CommentController;
import xyz.sangcomz.sangcomz_n_study.ui.main.MainActivity;
import xyz.sangcomz.sangcomz_n_study.util.Utils;
import xyz.sangcomz.sangcomz_n_study.util.custom.RoundedImageView;
import xyz.sangcomz.sangcomz_n_study.util.custom.SquareImageView;

/**
 * Created by sangc on 2015-12-28.
 */
public class CommentAdapter
        extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    CommentController commentController;
    ArrayList<Comment> comments = new ArrayList<>();
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final View mView;
        public final RoundedImageView rivProfile;
        public final TextView txtMemberName;
        public final TextView txtDate;
        public final TextView txtContent;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            rivProfile = (RoundedImageView) view.findViewById(R.id.riv_profile);
            txtMemberName = (TextView) view.findViewById(R.id.txt_member_name);
            txtDate = (TextView) view.findViewById(R.id.txt_date);
            txtContent = (TextView) view.findViewById(R.id.txt_content);
        }
    }

    public CommentAdapter(Context context, ArrayList<Comment> comments, CommentController commentController) {
        this.context = context;
        this.comments = comments;
        this.commentController = commentController;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(comments.get(position).getMemberProfile()).centerCrop().into(holder.rivProfile);
//        Glide.with(context).load(posts.get(position).getPostImage()).centerCrop().into(holder.sivPostImage);
        holder.txtMemberName.setText(comments.get(position).getMemberName());
        holder.txtContent.setText(comments.get(position).getCommentContent());
//        holder.txtCommentCount.setText(posts.get(position).getPostCommentCount());
        holder.txtDate.setText(Utils.getDateString(holder.txtDate.getContext(),
                "yyyy.MM.dd", Integer.parseInt(comments.get(position).getCommentDate())));

        if (comments.get(position).getIsMyComment().equals("Y"))
            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    commentController.delComment(comments.get(position).getCommentSrl(), position, CommentAdapter.this);
                    return false;
                }
            });


    }


    public void delComment(int position) {
        comments.remove(position);
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }
}