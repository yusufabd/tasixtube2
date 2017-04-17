package uz.androidclub.tas_ixtube.presenter;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;

import uz.androidclub.tas_ixtube.activities.JCPlayer;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.interfaces.PlayerContract;
import uz.androidclub.tas_ixtube.managers.DatabaseManager;
import uz.androidclub.tas_ixtube.parsers.WatchPageParser;
import uz.androidclub.tas_ixtube.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yusufabd on 3/4/2017.
 */

public class PlayerPresenter implements PlayerContract.Presenter, Constants{

    private Context mCtx;
    private WatchPageParser mParser;
    private PlayerContract.View mView;
    private DatabaseManager mDB;
    private Video mObject;
    private int mQuality = QUALITY_360;

    public PlayerPresenter(Context ctx, PlayerContract.View mView, Video object) {
        mCtx = ctx;
        mParser = new WatchPageParser();
        this.mView = mView;
        mObject = object;
        mDB = new DatabaseManager(mCtx);
    }

    @Override
    public void startVideo() {
        mView.showLoading();
        Observable.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                return Jsoup.connect(mObject.getWatchLink()).get();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Document>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Document document) {
                Video result = mParser.getVideoInfo(document);
                mObject.setDescription(result.getDescription());
                mObject.setTags(result.getTags());
                mObject.setSimilarVideoList(result.getSimilarVideoList());
                mObject.setAuthor(result.getAuthor());
                mView.setObject(mObject);
                mView.showVideo(mObject.getMediumQualityVideo());
                mView.hideLoading();
            }
        });
    }


    public void download() {
        if(ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    (JCPlayer)mCtx,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WES_PERMISSION);
            return;
        }

        String url = mObject.getMediumQualityVideo();
        switch (mQuality){
            case QUALITY_240:
                url = mObject.getSmallQualityVideo();
                break;
            case QUALITY_480:
                if (StringUtils.isUrlValid(mObject.getHighQualityVideo())){
                    url = mObject.getHighQualityVideo();
                }else {
                    StringUtils.showToast(mCtx, R.string.quality_not_available);
                }
                break;
        }

        AlertDialog dialog = new AlertDialog.Builder(mCtx)
                .setTitle(R.string.download)
                .setMessage(mCtx.getString(R.string.folder, Environment.DIRECTORY_DOWNLOADS))
                .setNegativeButton(R.string.start, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DownloadManager.Request r = new DownloadManager.Request(Uri.parse(mObject.getMediumQualityVideo()));
                        r.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "");
                        r.allowScanningByMediaScanner();
                        r.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                        DownloadManager dm = (DownloadManager) mCtx.getSystemService(Context.DOWNLOAD_SERVICE);
                        dm.enqueue(r);
                        StringUtils.showToast(mCtx, R.string.download_started);
                    }
                }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();

    }


    public void share() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = mObject.getWatchLink();
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        mCtx.startActivity(Intent.createChooser(sharingIntent, mCtx.getString(R.string.share)));
    }

    @Override
    public void changeQuality(int position, int second) {
        switch (position){
            case QUALITY_240:
                mView.changeQuality(mObject.getSmallQualityVideo(), second);
                break;
            case QUALITY_360:
                mView.changeQuality(mObject.getMediumQualityVideo(), second);
                break;
            case QUALITY_480:
                mView.changeQuality(mObject.getHighQualityVideo(), second);
                break;

        }
    }

    @Override
    public void onPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE_WES_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    download();
                }else {
                    StringUtils.showToast(mCtx, R.string.no_wes_permission);
                }
                break;
        }
    }

    @Override
    public void onClick(int id) {
        switch (id){
            case R.id.arrow_down_button:
                mView.toggleDescription();
                break;
            case R.id.bottom_download:
                download();
                break;
            case R.id.bottom_tags:
                mView.showTags();
                break;
            case R.id.bottom_quality:
                mView.showQuality();
                break;
            case R.id.card_author:

                break;
            case R.id.bottom_star:
                mDB.addToFavorite(mObject);
                StringUtils.showToast(mCtx, R.string.added_to_fav);
                break;
            case R.id.top_share:
                share();
                break;
        }
    }
}
