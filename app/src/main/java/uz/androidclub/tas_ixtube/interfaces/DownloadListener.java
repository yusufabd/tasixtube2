package uz.androidclub.tas_ixtube.interfaces;

import uz.androidclub.tas_ixtube.models.Message;

/**
 * Created by yusufabd on 4/22/2017.
 */
public interface DownloadListener {
    /**
     * downloadFile progress value
     *
     * @param value
     */
    void progressUpdate(Message value);
}