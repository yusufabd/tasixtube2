package uz.androidclub.tas_ixtube.activities;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.AppCompatSpinner;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;

import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.utils.StringUtils;
import uz.androidclub.tas_ixtube.interfaces.MainContract;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.fragmnts.VideoListFragment;
import uz.androidclub.tas_ixtube.presenter.MainPresenter;
import uz.androidclub.tas_ixtube.utils.Constants;

import java.util.ArrayList;

public class MainActivity   extends    AppCompatActivity
                            implements NavigationView.OnNavigationItemSelectedListener,
                                       SwipeRefreshLayout.OnRefreshListener,
                                       MainContract.View,
                                       View.OnClickListener{

    private AppCompatSpinner mDropdownCategories, mDropdownSections;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private ImageButton mSettingsButton, mFavButton, mShareButton, mRateButton, mAboutButton, mFAQButton;
    private VideoListFragment mFragment;
    private MainPresenter mPresenter;

    private static final int TIME_DELAY = 2000;
    private static long mBackPressed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setNavigationDrawer(toolbar);
        initUI();
        mPresenter.startApplication();
    }

    @Override
    public void onRefresh() {
        mPresenter.onRefresh();
    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (mBackPressed + TIME_DELAY > System.currentTimeMillis()) {
            finish();
        } else {
            StringUtils.showToast(this, R.string.press_again_to_exit);
        }
        mBackPressed = System.currentTimeMillis();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_search:
                startActivity(new Intent(this, SearchActivity.class));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        mPresenter.onNavigationItemSelected(item);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void showList(ArrayList<Video> list) {
        mDropdownSections.setVisibility(View.VISIBLE);
        mSwipeRefreshLayout.setRefreshing(false);
        mFragment = VideoListFragment.newInstance(list, Constants.MAIN_ACTIVITY);
        replaceFragment();
    }

    @Override
    public void showListWithoutDropdown(ArrayList<Video> list) {
        mDropdownSections.setVisibility(View.GONE);
        mSwipeRefreshLayout.setRefreshing(false);
        mFragment = VideoListFragment.newInstance(list, Constants.MAIN_ACTIVITY);
        replaceFragment();
    }

    @Override
    public void loadMore(ArrayList<Video> list) {
        mFragment.updateList(list);
    }
    @Override
    public void showError(String msg) {
        StringUtils.showToast(this, msg);
    }
    @Override
    public void setTitle(String title) {
        setTitle(title.subSequence(0, title.length()));
    }

    public void onLoadMore(int page){
        mPresenter.lazyLoad(page);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private class SectionSelector implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mPresenter.onSectionSelected(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private class CategorySelector implements android.widget.AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mPresenter.onCategorySelected(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
    private void setNavigationDrawer(Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getHeaderView(0).setOnClickListener(mPresenter.onHeaderClickListener);
    }
    private void initUI() {
        mDropdownCategories = (AppCompatSpinner)findViewById(R.id.spinner_category);
        mDropdownCategories.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.cats, R.layout.spinner_item));
        mDropdownCategories.setOnItemSelectedListener(new CategorySelector());

        mDropdownSections = (AppCompatSpinner)findViewById(R.id.spinner_ratings);
        mDropdownSections.setAdapter(ArrayAdapter.createFromResource(this,
                R.array.ratings, R.layout.spinner_item));
        mDropdownSections.setOnItemSelectedListener(new SectionSelector());

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary, R.color.colorPrimaryDark, R.color.colorAccent);

        mSettingsButton = (ImageButton)findViewById(R.id.nav_button_settings);
        mFavButton = (ImageButton)findViewById(R.id.nav_button_fav);
        mShareButton = (ImageButton)findViewById(R.id.nav_button_share);
        mRateButton = (ImageButton)findViewById(R.id.nav_button_rate);
        mAboutButton = (ImageButton)findViewById(R.id.nav_button_about);
        mFAQButton = (ImageButton)findViewById(R.id.nav_button_faq);

        mSettingsButton.setOnClickListener(this);
        mFavButton.setOnClickListener(this);
        mShareButton.setOnClickListener(this);
        mRateButton.setOnClickListener(this);
        mAboutButton.setOnClickListener(this);
        mFAQButton.setOnClickListener(this);
        findViewById(R.id.mover_banner).setOnClickListener(this);
    }
    private void replaceFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, mFragment);
        transaction.commit();
    }
}