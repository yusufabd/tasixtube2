package uz.androidclub.tas_ixtube.interfaces;

import android.view.MenuItem;

import uz.androidclub.tas_ixtube.models.Category;
import uz.androidclub.tas_ixtube.models.Video;

import java.util.ArrayList;

/**
 * Created by yusufabd on 13.02.2017.
 */

public interface MainContract {

    interface View{
        void showList(ArrayList<Video> list);
        void showListWithoutDropdown(ArrayList<Video> list);
        void loadMore(ArrayList<Video> list);
        void showError(String message);
        void setTitle(String title);
    }

    interface Presenter{
        void startApplication();
        void onRefresh();
        void loadVideoList(Category category);
        void lazyLoad(int page);
        void onLoadResponse(ArrayList<Video> list, boolean isLazy);
        void onNavigationItemSelected(MenuItem item);
        void onSectionSelected(int position);
        void onCategorySelected(int position);
        void onClick(int id);
    }
}
