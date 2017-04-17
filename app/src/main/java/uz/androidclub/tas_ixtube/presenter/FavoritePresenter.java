package uz.androidclub.tas_ixtube.presenter;

import android.content.Context;

import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.interfaces.FavoriteContract;
import uz.androidclub.tas_ixtube.managers.DatabaseManager;

import java.util.ArrayList;

/**
 * Created by yusufabd on 3/13/2017.
 */

public class FavoritePresenter implements FavoriteContract.Presenter {

    private Context mCtx;
    private DatabaseManager mDB;
    private FavoriteContract.View mView;

    public FavoritePresenter(Context mCtx, FavoriteContract.View view) {
        this.mCtx = mCtx;
        mView = view;
        mDB = new DatabaseManager(mCtx);
    }

    @Override
    public void loadVideoList() {
        onLoadResponse(mDB.getFavoriteList(), false);
    }

    @Override
    public void lazyLoad(int page) {

    }

    @Override
    public void onLoadResponse(ArrayList<Video> lis, boolean isLazy) {
        mView.showList(lis);
    }
}
