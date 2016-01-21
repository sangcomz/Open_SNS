package xyz.sangcomz.open_sns.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.drakeet.materialdialog.MaterialDialog;
import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Comment;
import xyz.sangcomz.open_sns.ui.comment.CommentController;
import xyz.sangcomz.open_sns.util.Utils;
import xyz.sangcomz.open_sns.util.custom.RoundedImageView;

/**
 * Created by sangc on 2015-12-28.
 */
public class CommentAdapter
        extends RecyclerView.Adapter<CommentAdapter.ViewHolder> {

    CommentController commentController;
    ArrayList<Comment> comments = new ArrayList<>();
    Context context;
    MaterialDialog materialDialog;

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
        materialDialog = new MaterialDialog(context);
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

                    materialDialog.setTitle(context.getString(R.string.txt_comment_dialog_title))
                            .setMessage(context.getString(R.string.msg_comment_dialog))
                            .setPositiveButton(context.getString(R.string.txt_done), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    commentController.delComment(comments.get(position).getCommentSrl(), position, CommentAdapter.this);
                                    materialDialog.dismiss();
                                }
                            })
                            .setNegativeButton(context.getString(R.string.txt_cancel), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    materialDialog.dismiss();
                                }
                            }).show();
//                    MaterialDialog mMaterialDialog = new MaterialDialog(this)
//                            .setTitle("MaterialDialog")
//                            .setMessage("Hello world!")
//                            .setPositiveButton("OK", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                    ...
//                                }
//                            })
//                            .setNegativeButton("CANCEL", new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    mMaterialDialog.dismiss();
//                                    ...
//                                }
//                            });


                    return false;
                }
            });


    }


    public void delComment(int position) {
        comments.remove(position);
        notifyDataSetChanged();
        if (comments.size() == 0)
            commentController.setNodataVisibility(View.VISIBLE);
    }


    @Override
    public int getItemCount() {
        return comments.size();
    }
}