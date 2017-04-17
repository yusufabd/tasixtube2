package uz.androidclub.tas_ixtube.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.TextView;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.fragmnts.VideoListFragment;
import uz.androidclub.tas_ixtube.interfaces.SearchContract;
import uz.androidclub.tas_ixtube.fragmnts.FiltersDialogFragment;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.presenter.SearchPresenter;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.StringUtils;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity implements SearchContract.View, Constants{

    private AutoCompleteTextView mField;
    private ProgressBar mLoading;
    private SearchPresenter presenter;
    private VideoListFragment mFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        presenter = new SearchPresenter(this, this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment dialog = new FiltersDialogFragment();
                dialog.show(getSupportFragmentManager(), "Filters");
            }
        });

        mField = (AutoCompleteTextView)findViewById(R.id.search_field);
        mField.setAdapter(new ArrayAdapter<>(this, R.layout.spinner_item, presenter.getSearchHistory()));
        mField.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH){
                    searchClicked();
                }
                return false;
            }
        });

        mLoading = (ProgressBar)findViewById(R.id.loading_search);
    }

    private void searchClicked() {
        InputMethodManager inputManager =
                (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(
                this.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        showLoading();
        presenter.loadList(mField.getText().toString());
    }

    public void queryUpdate(String filter){
        showLoading();
        presenter.addFilter(filter);
    }

    public void onLoadMore(int page){
        presenter.lazyLoad(page);
    }

    @Override
    public void showList(ArrayList<Video> list) {
        hideLoading();
        mFragment = VideoListFragment.newInstance(list, SEARCH_ACTIVITY);
        replaceFragment();
    }

    @Override
    public void loadMore(ArrayList<Video> list) {
        mFragment.updateList(list);
    }

    @Override
    public void showError(String msg) {
        hideLoading();
        StringUtils.showToast(this, msg);
    }

    private void showLoading(){
        mLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        mLoading.setVisibility(View.GONE);
    }

    private void replaceFragment(){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.search_container, mFragment);
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
