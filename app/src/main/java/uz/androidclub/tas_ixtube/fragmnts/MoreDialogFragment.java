package uz.androidclub.tas_ixtube.fragmnts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.presenter.PlayerPresenter;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.managers.DatabaseManager;
import uz.androidclub.tas_ixtube.utils.Constants;

/**
 * Created by yusufabd on 3/8/2017.
 */

public class MoreDialogFragment extends DialogFragment implements View.OnClickListener, Constants{

    private Context mCtx;
    private TextView mFav, mShare, mAuthor;
    private Video mObj;
    private DatabaseManager mDB;
    private int mListType = Constants.LIST_TYPE_REGULAR;

    public static MoreDialogFragment newInstance(Context ctx, Video obj, int listType){
        MoreDialogFragment fragment = new MoreDialogFragment();
        fragment.setObj(obj);
        fragment.setCtx(ctx);
        fragment.setDB();
        fragment.setListType(listType);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog d = super.onCreateDialog(savedInstanceState);
        d.setTitle(mObj.getTitle());
        return d;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setTitle(mObj.getTitle());
        View rootView = inflater.inflate(R.layout.dialog_fragment_more, container, false);
        mFav = (TextView)rootView.findViewById(R.id.text_add_favorite);
        if (mListType == LIST_TYPE_REGULAR){
            mFav.setText(R.string.add_to_fav);
        }else {
            mFav.setText(R.string.remove_from_fav);
        }
        mShare = (TextView)rootView.findViewById(R.id.text_share);
        mAuthor = (TextView)rootView.findViewById(R.id.text_author);
        mFav.setOnClickListener(this);
        mShare.setOnClickListener(this);
        mAuthor.setOnClickListener(this);
        return rootView;
    }

    public void setObj(Video mObj) {
        this.mObj = mObj;
    }

    public void setDB() {
        mDB = new DatabaseManager(mCtx);
    }

    public void setCtx(Context ctx){
        mCtx = ctx;
    }

    public void setListType(int mListType) {
        this.mListType = mListType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.text_add_favorite:
                if (mListType == LIST_TYPE_REGULAR) {
                    mDB.addToFavorite(mObj);
                    StringUtils.showToast(getActivity(), R.string.added_to_fav);
                }else {
                    mDB.removeFromFavorite(mObj);
                    StringUtils.showToast(getActivity(), R.string.removed_from_fav);
                }
                break;
            case R.id.text_share:
                new PlayerPresenter(getActivity(), null, mObj).share();
                break;
            case R.id.text_author:

                break;
        }
        dismiss();
    }
}
