package uz.androidclub.tas_ixtube.models;

import uz.androidclub.tas_ixtube.utils.Constants;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by yusufabd on 13.02.2017.
 */

public class Video implements Constants, Serializable{
    private String mId, mTitle, mAuthor, mViews, mLength, mDescription;
    private String[] mTags;
    private ArrayList<Video> mSimilarVideoList;
    public Video() {
    }

    public Video(String mTitle, String mAuthor, String mViews, String mLength, String mId) {
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mViews = mViews.split(":")[1];
        this.mLength = mLength;
        this.mId = mId;
    }

    public String getId() {
        return mId;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String mAuthor) {
        this.mAuthor = mAuthor;
    }

    public String getViews() {
        return mViews;
    }

    public void setViews(String mViews) {
        this.mViews = mViews;
    }

    public String getLength() {
        return mLength;
    }

    public void setLength(String mLength) {
        this.mLength = mLength;
    }

    public String getSmallQualityVideo() {
        return VIDEO_LINK + mId + "_s.mp4";
    }

    public String getMediumQualityVideo() {
        return VIDEO_LINK + mId + "_m.mp4";
    }

    public String getHighQualityVideo() {
        return VIDEO_LINK + mId + "_b.mp4";
    }

    public String getThumbnailLink() {
        return IMAGE_LINK + mId + "_t2.jpg";
    }

    public String getSmallImageLink() {
        return IMAGE_LINK + mId + "_s2.jpg";
    }

    public String getMediumImageLink() {
        return IMAGE_LINK + mId + "_m2.jpg";
    }

    public String getBigImageLink() {
        return IMAGE_LINK + mId + "_b2.jpg";
    }

    public String getWatchLink(){
        return MAIN_PAGE + "watch/" + mId;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String[] getTags() {
        return mTags;
    }

    public void setTags(String[] mTags) {
        this.mTags = mTags;
    }

    public ArrayList<Video> getSimilarVideoList() {
        return mSimilarVideoList;
    }

    public void setSimilarVideoList(ArrayList<Video> mSimilarVideoList) {
        this.mSimilarVideoList = mSimilarVideoList;
    }
}
