package uz.androidclub.tas_ixtube.presenter;

import android.content.Context;

import uz.androidclub.tas_ixtube.interfaces.TagsContract;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.parsers.TagPageParser;
import uz.androidclub.tas_ixtube.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yusufabd on 3/8/2017.
 */

public class TagsPresenter implements TagsContract.Presenter, Constants {

    private Context mCtx;
    private TagsContract.View mView;
    private TagPageParser mParser;
    private String mUrl;

    public TagsPresenter(Context mCtx, TagsContract.View mView) {
        this.mCtx = mCtx;
        this.mView = mView;
        mParser = new TagPageParser();
    }

    @Override
    public void onRefresh() {
        loadVideoList(mUrl);
    }

    @Override
    public void loadVideoList(String tag) {
        mUrl = TAGS_LINK + tag;
        Observable
                .fromCallable(new Callable<Document>() {
                    @Override
                    public Document call() throws Exception {
                        StringUtils.showLog(mUrl);
                        return Jsoup.connect(mUrl).get();
                    }
              }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        StringUtils.showToast(mCtx, e.getMessage());
                    }

                    @Override
                    public void onNext(Document document) {
                        onLoadResponse(mParser.getList(document), false);
                    }
        });
    }

    @Override
    public void lazyLoad(int page) {

    }

    @Override
    public void onLoadResponse(ArrayList<Video> list, boolean isLazy) {
        if (isLazy)
            mView.loadMore(list);
        else
            mView.showList(list);
    }
}
