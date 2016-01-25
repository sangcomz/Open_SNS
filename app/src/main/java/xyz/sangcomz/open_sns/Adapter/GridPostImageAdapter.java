package xyz.sangcomz.open_sns.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.sangcomz.open_sns.R;
import xyz.sangcomz.open_sns.bean.Post;
import xyz.sangcomz.open_sns.util.custom.SquareImageView;

/**
 * Created by sangc on 2015-12-28.
 */
public class GridPostImageAdapter
        extends RecyclerView.Adapter<GridPostImageAdapter.ViewHolder> {

    ArrayList<Post> posts = new ArrayList<>();
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {


        public final SquareImageView sivPostImage;



        public ViewHolder(View view) {
            super(view);

            sivPostImage = (SquareImageView) view.findViewById(R.id.siv_post_image);

        }
    }

    public GridPostImageAdapter(Context context, ArrayList<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_only_image, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        Glide.with(context).load(posts.get(position).getPostImage()).centerCrop().into(holder.sivPostImage);



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