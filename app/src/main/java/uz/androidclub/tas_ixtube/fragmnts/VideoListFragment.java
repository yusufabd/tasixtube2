package uz.androidclub.tas_ixtube.fragmnts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.activities.MainActivity;
import uz.androidclub.tas_ixtube.activities.SearchActivity;
import uz.androidclub.tas_ixtube.activities.TagsActivity;
import uz.androidclub.tas_ixtube.adapters.FavoriteRecyclerAdapter;
import uz.androidclub.tas_ixtube.adapters.VideoRecyclerAdapter;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.EndlessRecyclerOnScrollListener;

/**
 * Created by yusufabd on 2/28/2017.
 */

public class VideoListFragment extends Fragment implements Constants{

    private View mRoot;
    private RecyclerView mRecyclerList;
    private VideoRecyclerAdapter mRecyclerAdapter;
    private LinearLayoutManager mRecyclerManager;
    private ArrayList<Video> mList;
    private int mActivity;


    public static VideoListFragment newInstance(ArrayList<Video> list, int activity){
        VideoListFragment fragment = new VideoListFragment();
        fragment.setList(list);
        fragment.setActivity(activity);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_video_list, container, false);
        initRecycler();
        return mRoot;
    }

    public void setActivity(int mActivity) {
        this.mActivity = mActivity;
    }

    private void setList(ArrayList<Video> mList) {
        this.mList = mList;
    }

    public void updateList(ArrayList<Video> list){
        for (Video v :
                list) {
            mList.add(v);
        }
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mList == null){

        }
    }

    private void updateRecycler(){
        if (mActivity == FAVORITE_ACTIVITY){
            FavoriteRecyclerAdapter adapter = new FavoriteRecyclerAdapter(mList, getActivity(), getActivity().getSupportFragmentManager());
            mRecyclerList.setAdapter(adapter);
        }else {
            mRecyclerAdapter = new VideoRecyclerAdapter(mList, getActivity(), getActivity().getSupportFragmentManager());
            mRecyclerList.setAdapter(mRecyclerAdapter);
        }
        mRecyclerList.addOnScrollListener(new EndlessRecyclerOnScrollListener(mRecyclerManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d(TAG, "Current page:" + current_page);
                switch (mActivity) {
                    case MAIN_ACTIVITY:
                        MainActivity main = (MainActivity) getActivity();
                        main.onLoadMore(current_page);
                        break;
                    case SEARCH_ACTIVITY:
                        SearchActivity search = (SearchActivity) getActivity();
                        search.onLoadMore(current_page);
                        break;
                    case TAGS_ACTIVITY:
                        TagsActivity tags = (TagsActivity) getActivity();
                        tags.onLoadMore(current_page);
                        break;
                }
            }
        });
    }

    private void initRecycler(){
        mRecyclerList = (RecyclerView)mRoot.findViewById(R.id.recycler_view_video);
        mRecyclerManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRecyclerList.setLayoutManager(mRecyclerManager);
        mRecyclerList.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        updateRecycler();
    }
}
