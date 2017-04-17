package uz.androidclub.tas_ixtube.interfaces;

import uz.androidclub.tas_ixtube.models.Video;

import java.util.ArrayList;

/**
 * Created by yusufabd on 3/13/2017.
 */

public interface FavoriteContract {
    interface View{
        void showList(ArrayList<Video> list);
        void loadMore(ArrayList<Video> list);
        void showError(String msg);
    }

    interface Presenter{
        void loadVideoList();
        void lazyLoad(int page);
        void onLoadResponse(ArrayList<Video> lis, boolean isLazy);
    }
}
