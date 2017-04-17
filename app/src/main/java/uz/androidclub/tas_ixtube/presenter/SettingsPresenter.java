package uz.androidclub.tas_ixtube.presenter;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;

import uz.androidclub.tas_ixtube.activities.InstructionsActivity;
import uz.androidclub.tas_ixtube.interfaces.SettingsContract;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.activities.AboutActivity;
import uz.androidclub.tas_ixtube.managers.DatabaseManager;
import uz.androidclub.tas_ixtube.managers.SharedPrefManager;
import uz.androidclub.tas_ixtube.utils.Constants;

import java.util.Locale;

/**
 * Created by yusufabd on 2/28/2017.
 */

public class SettingsPresenter implements SettingsContract.Presenter, Constants{

    private Context mCtx;
    private SettingsContract.View mView;
    private SharedPrefManager mPref;
    private DatabaseManager mDB;

    public SettingsPresenter(Context context, SettingsContract.View view) {
        mCtx = context;
        mView = view;
        mPref = new SharedPrefManager(context);
        mDB = new DatabaseManager(mCtx);
    }

    @Override
    public void onClick(int id) {
        switch (id){
            case R.id.toggle_player_container:
                mView.switchPlayer();
                break;
            case R.id.text_clear_fav:
                mDB.clearFavoriteList();
                StringUtils.showToast(mCtx, R.string.fav_list_cleared);
                break;
            case R.id.text_clear_search:
                mDB.clearSearchHistory();
                StringUtils.showToast(mCtx, R.string.search_history_cleared);
                break;
            case R.id.text_instructions:
                mCtx.startActivity(new Intent(mCtx, InstructionsActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.text_about:
                mCtx.startActivity(new Intent(mCtx, AboutActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
    }

    @Override
    public void onSpinnerItemSelected(int position) {
        int lang = LANG_RU;
        String locale = "ru";
        switch (position){
            case 0:
                lang = LANG_EN;
                locale = "en";
                break;
            case 1:
                lang = LANG_RU;
                locale = "ru";
                break;
            case 2:
                lang = LANG_UZ;
                locale = "uz";
                break;
        }
        mPref.setLang(lang);
        updateLocale(locale);
    }

    @Override
    public boolean isExternalPlayer() {
        return mPref.isExternalPlayer();
    }

    @Override
    public int getLang() {
        int pos = 1;
        switch (mPref.getLang()){
            case LANG_EN:
                pos = 0;
                break;
            case LANG_RU:
                pos = 1;
                break;
            case LANG_UZ:
                pos = 2;
                break;
        }
        return pos;
    }

    @Override
    public void onSwitched(boolean isChecked) {
        mPref.setExternalPlayer(isChecked);
    }

    private void updateLocale(String locale){
        Resources res = mCtx.getResources();
        Configuration configuration = res.getConfiguration();
        if (StringUtils.isNewVersion()) {
            configuration.setLocale(new Locale(locale));
        }else {
            configuration.locale = new Locale(locale);
        }
        res.updateConfiguration(configuration, null);
    }
}
