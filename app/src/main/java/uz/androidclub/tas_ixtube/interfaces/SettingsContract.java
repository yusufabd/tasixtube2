package uz.androidclub.tas_ixtube.interfaces;

/**
 * Created by yusufabd on 3/13/2017.
 */

public interface SettingsContract {
    interface View{
        void switchPlayer();
    }

    interface Presenter{
        void onClick(int id);
        void onSpinnerItemSelected(int position);
        void onSwitched(boolean isChecked);
        boolean isExternalPlayer();
        int getLang();
    }
}
