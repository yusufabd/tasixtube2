package uz.androidclub.tas_ixtube.presenter;

import android.content.Context;
import android.util.Log;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.interfaces.SearchContract;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.managers.DatabaseManager;
import uz.androidclub.tas_ixtube.parsers.RegularParser;
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
 * Created by yusufabd on 2/22/2017.
 */

public class SearchPresenter implements SearchContract.Presenter, Constants{
    private static final String TAG = SearchPresenter.class.getSimpleName();
    private SearchContract.View mView;
    private Context mCtx;
    private RegularParser mParser;
    private DatabaseManager mDB;
    private String mFilter = "", mQuery = "", mUrl = "";
    private int mPage = 1;

    public SearchPresenter(Context ctx, SearchContract.View view) {
        mCtx = ctx;
        mParser = new RegularParser();
        mView = view;
        mDB = new DatabaseManager(mCtx);
    }

    @Override
    public void loadList(String query) {
        mPage = 1;
        mUrl = SEARCH_LINK;
        if (query.length() < 3){
            mView.showError("Enter at least 3 characters");
            return;
        }
        mDB.addToSearchHistory(query);
        if (query.contains(" ")){
            query = query.replace(" ", "+");
        }
        mQuery = query;
        mUrl = mUrl.concat(query).concat(mFilter);
        Observable.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                Log.d(TAG, "loadVideoList: " + mUrl);
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
                        Log.d(TAG, "Error msg: " + e.getMessage());
                        mView.showError(mCtx.getString(R.string.nothing_found));
                    }

                    @Override
                    public void onNext(Document document) {
                        ArrayList<Video> result = mParser.getList(document);
                        if(result.size() <= 0){
                            mView.showError(mCtx.getString(R.string.nothing_found));
                        }else {
                            onLoadResponse(result, false);
                        }
                    }
                });
    }



    @Override
    public void lazyLoad(int page) {
        if (mPage >= page)
            return;

        mPage = page;
        mUrl = mUrl.concat("&page=").concat(String.valueOf(page));
        Observable.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                Log.d(TAG, "Lazy load: " + mUrl);
                Document d = Jsoup.connect(mUrl).get();
                Log.d(TAG, d.toString());
                return d;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Document>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Document document) {
                        onLoadResponse(mParser.getList(document), true);
                    }
                });
    }

    @Override
    public void addFilter(String filter) {
        mFilter = filter;
        loadList(mQuery);
    }

    @Override
    public void onLoadResponse(ArrayList<Video> list, boolean isLazy) {
        if (isLazy){
            mView.loadMore(list);
        }else {
            mView.showList(list);
        }
    }

    @Override
    public ArrayList<String> getSearchHistory() {
        return mDB.getSearchHistory();
    }
}
