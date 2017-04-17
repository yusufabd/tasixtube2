package uz.androidclub.tas_ixtube.utils;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by yusufabd on 3/1/2017.
 */

public class StringUtils implements Constants {

    public static void showToast(Context ctx, String msg) {
        Log.d(LOG_TAG, msg);
        Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
    }

    public static void showToast(Context ctx, int resId) {
        Log.d(LOG_TAG, ctx.getString(resId));
        Toast.makeText(ctx, ctx.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void showLog(String msg) {
        Log.d(LOG_TAG, "Error: " + msg);
    }

    public static boolean isNewVersion() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
    }

    private String stringToTime(int timeMs) {
        StringBuilder mFormatBuilder = new StringBuilder();
        java.util.Formatter mFormatter = new java.util.Formatter(mFormatBuilder, Locale.getDefault());

        int totalSeconds = timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d", hours, minutes).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    public static boolean isUrlValid(String link) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(link).openConnection();
            return connection.getResponseCode() == 200;
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
