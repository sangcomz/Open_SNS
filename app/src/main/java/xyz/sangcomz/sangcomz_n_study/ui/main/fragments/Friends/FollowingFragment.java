package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.friends;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import xyz.sangcomz.sangcomz_n_study.adapter.FollowAdapter;
import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.bean.Member;
import xyz.sangcomz.sangcomz_n_study.util.ItemDecoration.DividerItemDecoration;
import xyz.sangcomz.sangcomz_n_study.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class FollowingFragment extends Fragment {

    FollowController followController;
    FollowAdapter followAdapter;
    ArrayList<Member> members = new ArrayList<>();
    RecyclerView recyclerView;

    RelativeLayout areaNoData;
    NoDataController noDataController;

    public FollowingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_following, container, false);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        areaNoData = (RelativeLayout) rootView.findViewById(R.id.area_nodata);
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_people_black_24dp, getString(R.string.msg_no_following));

        followController = new FollowController(getActivity(), followAdapter);
        followController.GetFollow(true, 1, this);
        // Inflate the layout for this fragment

        return rootView;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
        if (members.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            followAdapter = new FollowAdapter(getActivity(), members, true);
            recyclerView.setAdapter(followAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

}
