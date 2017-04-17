package uz.androidclub.tas_ixtube.fragmnts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.androidclub.tas_ixtube.adapters.TagsRecyclerAdapter;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.R;

import java.util.Arrays;

/**
 * Created by yusufabd on 2/25/2017.
 */

public class TagsDialogFragment extends DialogFragment{

    private String[] mTags;

    public TagsDialogFragment() {

    }

    public static TagsDialogFragment newInstance(String[] tags){
        TagsDialogFragment f = new TagsDialogFragment();
        f.setTags(tags);
        StringUtils.showLog(Arrays.toString(tags));
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_tags, container, false);
        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.recycler_tags);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(new TagsRecyclerAdapter(getActivity(), mTags));
        return rootView;
    }

    public void setTags(String[] mTags) {
        this.mTags = mTags;
    }
}
