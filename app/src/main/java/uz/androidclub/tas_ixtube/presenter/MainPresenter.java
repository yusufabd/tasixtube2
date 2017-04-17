package uz.androidclub.tas_ixtube.presenter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.activities.AboutActivity;
import uz.androidclub.tas_ixtube.activities.FavoriteActivity;
import uz.androidclub.tas_ixtube.activities.InstructionsActivity;
import uz.androidclub.tas_ixtube.activities.SettingsActivity;
import uz.androidclub.tas_ixtube.interfaces.MainContract;
import uz.androidclub.tas_ixtube.models.Category;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.parsers.MainPageParser;
import uz.androidclub.tas_ixtube.parsers.RegularParser;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.Divisions;
import uz.androidclub.tas_ixtube.utils.Sections;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by yusufabd on 13.02.2017.
 */

public class MainPresenter implements MainContract.Presenter, Constants {

    private static final String TAG = LOG_TAG;
    private Context mCtx;
    private MainPageParser mMainPageParser;
    private RegularParser mRegularParser;
    private MainContract.View mView;
    private Category mCategory;
    private int mPage = 1;

    public MainPresenter(Context context, MainContract.View v) {
        mCtx = context;
        mMainPageParser = new MainPageParser();
        mRegularParser = new RegularParser();
        mView = v;
    }

    @Override
    public void startApplication() {
        mCategory = Category.newInstance(mCtx, 0);
        loadVideoList(mCategory);
    }

    @Override
    public void onRefresh() {
        if (mCategory != null){
            loadVideoList(mCategory);
        }
    }

    @Override
    public void loadVideoList(final Category category) {
        mPage = 1;
        Observable.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                Log.d(TAG, "loadVideoList: " + category.getUrl());
                return Jsoup.connect(category.getUrl()).get();
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
                  mView.showError(e.getMessage());
              }

              @Override
              public void onNext(Document document) {
                  if (category.getType() == Divisions.MAIN){
                      onLoadResponse(mMainPageParser.getList(document), false);
                  }else {
                      onLoadResponse(mRegularParser.getList(document), false);
                  }
              }
          });
    }


    @Override
    public void lazyLoad(final int page) {
        if (mCategory.getType() == Divisions.MAIN || mPage >= page)
            return;

        mPage = page;
        Observable.fromCallable(new Callable<Document>() {
            @Override
            public Document call() throws Exception {
                Log.d(TAG, "Lazy load: " + mCategory.getUrl() + mPage);
                Document d = Jsoup.connect(mCategory.getUrl() + page).get();
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
                        Log.d(TAG, "Error msg: " + e.getMessage());
                        mView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(Document document) {
                        onLoadResponse(mRegularParser.getList(document), true);
                    }
                });
    }

    @Override
    public void onLoadResponse(ArrayList<Video> list, boolean isLazy) {
        if (isLazy){
            mView.loadMore(list);
        }else if(mCategory.getType() == Divisions.MAIN ||
                mCategory.getType() == Divisions.LATEST ||
                mCategory.getType() == Divisions.INTERESTING){
            mView.setTitle(mCategory.getTitle());
            mView.showListWithoutDropdown(list);
        }else {
            mView.setTitle("");
            mView.showList(list);
        }
    }

    @Override
    public void onNavigationItemSelected(MenuItem item) {
        int navId = item.getItemId();
    }

    @Override
    public void onSectionSelected(int position) {
        switch (position){
            case 0:
                mCategory.setSection(Sections.TOP_DAY);
                break;
            case 1:
                mCategory.setSection(Sections.TOP_THREE_DAYS);
                break;
            case 2:
                mCategory.setSection(Sections.TOP_WEEK);
                break;
            case 3:
                mCategory.setSection(Sections.TOP_MONTH);
                break;
            case 4:
                mCategory.setSection(Sections.RECOMMENDED);
                break;
            case 5:
                mCategory.setSection(Sections.INTERESTING);
                break;
            case 6:
                mCategory.setSection(Sections.NEW);
                break;
            default:
        }
        loadVideoList(mCategory);
    }

    @Override
    public void onCategorySelected(int position) {
        mCategory = Category.newInstance(mCtx, position);
        loadVideoList(mCategory);
    }

    @Override
    public void onClick(int id) {
        switch (id){
            case R.id.nav_button_settings:
                Intent settingsIntent = new Intent(mCtx, SettingsActivity.class);
                startActivity(settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY));
                break;
            case R.id.nav_button_share:
                shareApp();
                break;
            case R.id.nav_button_fav:
                Intent favIntent = new Intent(mCtx, FavoriteActivity.class);
                startActivity(favIntent);
                break;
            case R.id.nav_button_rate:
                Uri uri = Uri.parse("market://details?id=" + mCtx.getPackageName());
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                    Intent.FLAG_ACTIVITY_NEW_TASK|
                                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + mCtx.getPackageName())));
                }
                break;
            case R.id.nav_button_about:
                startActivity(new Intent(mCtx, AboutActivity.class));
                break;
            case R.id.nav_button_faq:
                startActivity(new Intent(mCtx, InstructionsActivity.class));
                break;
            case R.id.mover_banner:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://mover.uz"));
                startActivity(intent);
                break;
        }
    }

    private void shareApp(){
        String link = "http://play.google.com/store/apps/details?id=" + mCtx.getPackageName();
        String text = mCtx.getString(R.string.recommend_using_app);
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text + "\n" + link);
        mCtx.startActivity(Intent.createChooser(sharingIntent, mCtx.getString(R.string.share)));
    }

    void startActivity(Intent intent){
        mCtx.startActivity(intent);
    }

    public View.OnClickListener onHeaderClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://androidclub.uz"));
            startActivity(intent);
        }
    };
}
