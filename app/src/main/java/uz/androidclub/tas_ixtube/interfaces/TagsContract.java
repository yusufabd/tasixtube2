package uz.androidclub.tas_ixtube.interfaces;

import uz.androidclub.tas_ixtube.models.Video;

import java.util.ArrayList;

/**
 * Created by yusufabd on 3/8/2017.
 */

public interface TagsContract {
    interface View{
        void showList(ArrayList<Video> list);
        void loadMore(ArrayList<Video> list);
        void showError(String message);
        void setTitle(String title);
    }

    interface Presenter{
        void onRefresh();
        void loadVideoList(String tag);
        void lazyLoad(int page);
        void onLoadResponse(ArrayList<Video> list, boolean isLazy);
    }
}
