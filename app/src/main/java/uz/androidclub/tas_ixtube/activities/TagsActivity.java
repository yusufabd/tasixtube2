package uz.androidclub.tas_ixtube.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import uz.androidclub.tas_ixtube.fragmnts.VideoListFragment;
import uz.androidclub.tas_ixtube.interfaces.TagsContract;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.presenter.TagsPresenter;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.utils.Constants;

import java.util.ArrayList;

public class TagsActivity extends AppCompatActivity implements Constants, TagsContract.View, SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mRefreshLayout;
    private TagsPresenter mPresenter;
    private VideoListFragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        String tag = getIntent().getStringExtra(EXTRA_TAG);
        setTitle("#" + tag);
        mRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        mPresenter = new TagsPresenter(this, this);
        mPresenter.loadVideoList(tag);
    }

    @Override
    public void showList(ArrayList<Video> list) {
        mFragment = VideoListFragment.newInstance(list, TAGS_ACTIVITY);
        replaceFragment();
    }

    @Override
    public void loadMore(ArrayList<Video> list) {
        mFragment.updateList(list);
    }

    @Override
    public void showError(String message) {
        StringUtils.showToast(this, message);
    }

    @Override
    public void setTitle(String title) {
        setTitle(title.subSequence(0, title.length()));
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
        hideLoading();
    }

    public void onLoadMore(int page){
        mPresenter.lazyLoad(page);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void replaceFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.tags_container, mFragment);
        transaction.commit();
    }

    private void showLoading(){
        mRefreshLayout.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        mRefreshLayout.setVisibility(View.GONE);
    }
}
