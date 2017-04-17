package uz.androidclub.tas_ixtube.interfaces;

import android.support.annotation.NonNull;

import uz.androidclub.tas_ixtube.models.Video;

/**
 * Created by yusufabd on 3/4/2017.
 */

public interface PlayerContract {
    interface View{
        void showVideo(String url);
        void setObject(Video object);
        void changeQuality(String url, int second);
        void showTags();
        void showQuality();
        void showLoading();
        void hideLoading();
        void toggleDescription();
    }

    interface Presenter{
        void startVideo();
        void changeQuality(int position, int second);
        void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults);
        void onClick(int id);
    }
}
