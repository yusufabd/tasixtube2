package uz.androidclub.tas_ixtube.service;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
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

    private static final String ACTION_DOWNLOAD = "action_download";
    private static final String ACTION_PAUSE = "action_pause";
    private static final String ACTION_CANCEL = "action_cancel";
    private static final String ACTION_RESUME = "action_resume";
    private static final int REQUEST_CODE_PAUSE = 369;
    private static final int REQUEST_CODE_CANCEL = 963;
    private static final int REQUEST_CODE_RESUME = 693;
    private ResumableDownloader mDownloader;
    private SharedPrefManager mPref;
    private File mFileDir;
    public DownloadService() {
        super("DownloadService");
        mPref = new SharedPrefManager(this);
        mFileDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Tas Ix Tube");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        switch (intent.getAction()){
            case ACTION_DOWNLOAD:
                download(intent);
                break;
            case ACTION_PAUSE:
                mDownloader.setStatus(ResumableDownloader.PAUSE);

                break;
            case ACTION_RESUME:

                break;
            case ACTION_CANCEL:
                mDownloader.setStatus(ResumableDownloader.COMPLETE);
                mDownloader.cancelDownload();
                break;
        }

    }

    void download(Intent intent){
        mDownloader = new ResumableDownloader(mPref.getLastModified(), 0);
        String link = intent.getStringExtra(EXTRA_KEY_LINK);
        String title = intent.getStringExtra(EXTRA_KEY_TITLE);
        if (!mFileDir.exists())
            mFileDir.mkdirs();
        try {
            mDownloader.downloadFile(link, (new File(mFileDir.getAbsolutePath(), title + ".mp4")).getAbsolutePath(),
                    new DownloadListener() {
                        @Override
                        public void progressUpdate(Message value) {
                            StringUtils.showLog("Progress: " + value.getProgress());
                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private RemoteViews re;
    void buildNotification(String title){
        re = new RemoteViews(getPackageName(), R.layout.remote_download);
        re.setTextViewText(R.id.remote_title, title);

        Intent pauseIntent = new Intent(this, DownloadService.class);
        pauseIntent.setAction(ACTION_PAUSE);
        PendingIntent pausePending = PendingIntent.getService(this, REQUEST_CODE_PAUSE, pauseIntent, 0);

        Intent cancelIntent = new Intent(this, DownloadService.class);
        pauseIntent.setAction(ACTION_CANCEL);
        PendingIntent cancelPending = PendingIntent.getService(this, REQUEST_CODE_CANCEL, cancelIntent, 0);

        Intent resumeIntent = new Intent(this, DownloadService.class);
        pauseIntent.setAction(ACTION_RESUME);
        PendingIntent resumePending = PendingIntent.getService(this, REQUEST_CODE_RESUME, resumeIntent, 0);


    }
}
