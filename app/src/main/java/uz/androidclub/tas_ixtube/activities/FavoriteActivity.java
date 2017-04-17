package uz.androidclub.tas_ixtube.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import java.util.ArrayList;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.fragmnts.VideoListFragment;
import uz.androidclub.tas_ixtube.interfaces.FavoriteContract;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.presenter.FavoritePresenter;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.StringUtils;

public class FavoriteActivity extends AppCompatActivity implements FavoriteContract.View, Constants{

    private VideoListFragment mFragment;
    private FavoritePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(R.string.title_activity_favorite);
        mPresenter = new FavoritePresenter(this, this);
        mPresenter.loadVideoList();
    }

    @Override
    public void showList(ArrayList<Video> list) {
        mFragment = VideoListFragment.newInstance(list, FAVORITE_ACTIVITY);
        replaceFragment();
    }

    @Override
    public void loadMore(ArrayList<Video> list) {

    }

    @Override
    public void showError(String msg) {
        StringUtils.showToast(this, msg);
    }

    private void replaceFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fav_container, mFragment);
        transaction.commit();
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
}
