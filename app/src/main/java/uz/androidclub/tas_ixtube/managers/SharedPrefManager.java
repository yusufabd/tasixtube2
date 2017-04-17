package uz.androidclub.tas_ixtube.managers;

import android.content.Context;
import android.content.SharedPreferences;

import uz.androidclub.tas_ixtube.utils.Constants;

/**
 * Created by yusufabd on 2/27/2017.
 */

public class SharedPrefManager implements Constants {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    public SharedPrefManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    public int getListItemType(){
        return preferences.getInt(KEY_LIST_TYPE, LIST_REGULAR);
    }

    public void setListItemType(int type){
        editor.putInt(KEY_LIST_TYPE, type);
        editor.apply();
    }

    public int getLang(){
        return preferences.getInt(KEY_LANG, LANG_RU);
    }

    public void setLang(int lang){
        editor.putInt(KEY_LANG, lang);
        editor.apply();
    }

    public boolean isExternalPlayer(){
        return preferences.getBoolean(KEY_EXTERNAL_PLAYER, false);
    }

    public void setExternalPlayer(boolean isExternal){
        editor.putBoolean(KEY_EXTERNAL_PLAYER, isExternal);
        editor.apply();
    }
}
