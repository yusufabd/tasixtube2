package uz.androidclub.tas_ixtube.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.widget.RemoteViews;

import java.io.File;
import java.io.IOException;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.interfaces.DownloadListener;
import uz.androidclub.tas_ixtube.managers.SharedPrefManager;
import uz.androidclub.tas_ixtube.models.Message;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.ResumableDownloader;
import uz.androidclub.tas_ixtube.utils.StringUtils;


public class DownloadService extends IntentService implements Constants{

    private ResumableDownloader mDownloader;
    private SharedPrefManager mPref;
    private File mFileDir;


    public DownloadService() {
        super("DownloadService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        mPref = new SharedPrefManager(getApplicationContext());
        mFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Tas Ix Tube");
        switch (intent.getAction()){
            case ACTION_DOWNLOAD:
                download(intent);
                break;
            case ACTION_CANCEL:
                mDownloader.setStatus(ResumableDownloader.COMPLETE);
                mDownloader.cancelDownload();
                stopForeground(true);
                break;
        }
    }

    void download(Intent intent){
        mDownloader = new ResumableDownloader(mPref.getLastModified(), 0);
        String link = intent.getStringExtra(EXTRA_KEY_LINK);
        String title = intent.getStringExtra(EXTRA_KEY_TITLE);
        buildNotification(title);
        if (!mFileDir.exists())
            mFileDir.mkdirs();
        try {
            mDownloader.downloadFile(link, (new File(mFileDir.getAbsolutePath(), title + ".mp4")).getAbsolutePath(),
                    new DownloadListener() {
                        @Override
                        public void progressUpdate(Message value) {
                            StringUtils.showLog("Progress: " + value.getProgress());
                            re.setTextViewText(R.id.remote_progress, String.valueOf(value.getProgress() + "%"));
                            notificationManager.notify(1204, playerNotification);
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RemoteViews re;
    private NotificationManager notificationManager;
    Notification playerNotification;
    void buildNotification(String title){
        notificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        re = new RemoteViews(getPackageName(), R.layout.remote_download);
        re.setTextViewText(R.id.remote_title, title);

//        Intent pauseIntent = new Intent(this, DownloadService.class);
//        pauseIntent.setAction(ACTION_PAUSE);
//        PendingIntent pausePending = PendingIntent.getService(this, REQUEST_CODE_PAUSE, pauseIntent, 0);
//
//        Intent resumeIntent = new Intent(this, DownloadService.class);
//        pauseIntent.setAction(ACTION_RESUME);
//        PendingIntent resumePending = PendingIntent.getService(this, REQUEST_CODE_RESUME, resumeIntent, 0);

        Intent cancelIntent = new Intent(getApplicationContext(), DownloadService.class);
        cancelIntent.setAction(ACTION_CANCEL);
        PendingIntent cancelPending = PendingIntent.getService(getApplicationContext(), REQUEST_CODE_CANCEL, cancelIntent, 0);

        re.setOnClickPendingIntent(R.id.remote_button_stop, cancelPending);

        if (Build.VERSION.SDK_INT >= 24){
            playerNotification = new Notification.Builder(this)
                    .setTicker(getString(R.string.download_started))
                    .setCustomContentView(re)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
        }else {
            playerNotification = new Notification.Builder(this)
                    .setTicker(getString(R.string.download_started))
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .build();
            playerNotification.contentView = re;
        }

        startForeground(1204, playerNotification);
    }
}
