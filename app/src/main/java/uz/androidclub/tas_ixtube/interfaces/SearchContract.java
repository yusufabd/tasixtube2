package uz.androidclub.tas_ixtube.interfaces;

import uz.androidclub.tas_ixtube.models.Video;

import java.util.ArrayList;

/**
 * Created by yusufabd on 2/28/2017.
 */

public interface SearchContract {
    interface View{
        void showList(ArrayList<Video> list);
        void loadMore(ArrayList<Video> list);
        void showError(String msg);
    }

    interface Presenter{
        void loadList(String query);
        void lazyLoad(int page);
        void addFilter(String filter);
        void onLoadResponse(ArrayList<Video> list, boolean isLazy);
        ArrayList<String> getSearchHistory();
    }
}
