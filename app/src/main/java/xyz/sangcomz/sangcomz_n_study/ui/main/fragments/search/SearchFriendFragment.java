package xyz.sangcomz.sangcomz_n_study.ui.main.fragments.search;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import xyz.sangcomz.sangcomz_n_study.R;
import xyz.sangcomz.sangcomz_n_study.adapter.FollowAdapter;
import xyz.sangcomz.sangcomz_n_study.bean.Member;
import xyz.sangcomz.sangcomz_n_study.util.NoDataController;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFriendFragment extends Fragment {

    RecyclerView recyclerView;
    FollowAdapter followAdapter;
    ArrayList<Member> members = new ArrayList<>();
    SeachController seachController;
    RelativeLayout areaNoData;
    NoDataController noDataController;

    LinearLayoutManager linearLayoutManager;

    int curPage = 1;
    int totalPage;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    String query="";

    public SearchFriendFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_friend, container, false);
        seachController = new SeachController(getActivity(), this);
        areaNoData = (RelativeLayout) rootView.findViewById(R.id.area_nodata);
        noDataController = new NoDataController(areaNoData, getActivity());
        noDataController.setNodata(R.drawable.ic_search_black_24dp, getString(R.string.msg_no_search));
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);


        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                visibleItemCount = linearLayoutManager.getChildCount();
                totalItemCount = linearLayoutManager.getItemCount();
                pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                    if (curPage <= totalPage) {
                        searchMember(query);
                    }
                }

            }
        });

        return rootView;
    }

    public void setMembers(ArrayList<Member> members) {
        this.members = members;
        if (members.size() > 0) {
            areaNoData.setVisibility(View.GONE);
            followAdapter = new FollowAdapter(getActivity(), members, false);
            recyclerView.setAdapter(followAdapter);
        } else {
            areaNoData.setVisibility(View.VISIBLE);
        }
    }

    public void searchMember(String query) {
        this.query = query;
        seachController.SearchMember(query, curPage++);
    }
}
