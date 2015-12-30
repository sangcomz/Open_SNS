package xyz.sangcomz.sangcomz_n_study.adapter;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.bean.Member;
import xyz.sangcomz.sangcomz_n_study.ui.main.fragments.friends.FollowController;
import xyz.sangcomz.sangcomz_n_study.util.custom.RoundedImageView;

/**
 * Created by sangc on 2015-12-28.
 */
public class FollowAdapter
        extends RecyclerView.Adapter<FollowAdapter.ViewHolder> {

    ArrayList<Member> members = new ArrayList<>();
    Context context;
    FollowController followController;
    private boolean isRemove;

    public class ViewHolder extends RecyclerView.ViewHolder {

        public final RoundedImageView rivProfile;
        public final TextView txtMemberName;
        public final Button btnFollow;


        public ViewHolder(View view) {
            super(view);
            rivProfile = (RoundedImageView) view.findViewById(R.id.riv_profile);
            txtMemberName = (TextView) view.findViewById(R.id.txt_member_name);
            btnFollow = (Button) view.findViewById(R.id.btn_follow);
        }
    }

    public FollowAdapter(Context context, ArrayList<Member> members, boolean isRemove) {
        this.context = context;
        this.members = members;
        this.isRemove = isRemove;
        followController = new FollowController(context, this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_follow_member, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Glide.with(context).load(members.get(position).getProfilePath()).centerCrop().into(holder.rivProfile);
        holder.txtMemberName.setText(members.get(position).getMemberName());
        setBtnColor(members.get(position).getFollowYN(), holder.btnFollow);
        holder.btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (members.get(position).getFollowYN().equals("Y")) {
                    followController.Follow(members.get(position).getMemberSrl(), false, position);
                } else {
                    followController.Follow(members.get(position).getMemberSrl(), true, position);
                }
            }
        });

    }

    private void setBtnColor(String followYn, Button btnFollow) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (followYn.equals("Y")) {
                btnFollow.setBackgroundColor(context.getResources().getColor(R.color.colorFollowing, null));
                btnFollow.setText(context.getString(R.string.txt_del_follow));
            } else {
                btnFollow.setBackgroundColor(context.getResources().getColor(R.color.colorFollow, null));
                btnFollow.setText(context.getString(R.string.txt_add_follow));
            }
        } else {
            if (followYn.equals("Y")) {
                btnFollow.setBackgroundColor(context.getResources().getColor(R.color.colorFollowing));
                btnFollow.setText(context.getString(R.string.txt_del_follow));
            } else {
                btnFollow.setBackgroundColor(context.getResources().getColor(R.color.colorFollow));
                btnFollow.setText(context.getString(R.string.txt_add_follow));
            }
        }
    }
//    private void setBtnColor(String followYn, Button btnFollow){
//
//    }

    public void refreshFollowYn(int position, String followYN) {
        members.get(position).setFollowYN(followYN);
        notifyItemChanged(position);
        if (isRemove)
            deleteMember(position);
    }

    public void deleteMember(int position) {
        members.remove(position);
        notifyItemRemoved(position);
//        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return members.size();
    }
}