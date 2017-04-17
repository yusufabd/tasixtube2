package uz.androidclub.tas_ixtube.fragmnts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import uz.androidclub.tas_ixtube.activities.SearchActivity;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.presenter.FiltersPresenter;

/**
 * Created by yusufabd on 2/28/2017.
 */

public class FiltersDialogFragment extends DialogFragment{

    View mRootView;
    private AppCompatSpinner mCatFilter, mSortFilter, mDateFilter, mLengthFilter;
    private TextView mSearch;
    private String q = "";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.dialog_fragment_filters, container, false);
        mCatFilter = (AppCompatSpinner)mRootView.findViewById(R.id.spinner_filter_category);
        mCatFilter.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.filter_cats, R.layout.support_simple_spinner_dropdown_item));
        mSortFilter = (AppCompatSpinner)mRootView.findViewById(R.id.spinner_filter_sort);
        mSortFilter.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.sort, R.layout.support_simple_spinner_dropdown_item));
        mDateFilter = (AppCompatSpinner)mRootView.findViewById(R.id.spinner_filter_date);
        mDateFilter.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.date, R.layout.support_simple_spinner_dropdown_item));
        mLengthFilter = (AppCompatSpinner)mRootView.findViewById(R.id.spinner_filter_length);
        mLengthFilter.setAdapter(ArrayAdapter.createFromResource(getActivity(), R.array.length, R.layout.support_simple_spinner_dropdown_item));
        mSearch = (TextView)mRootView.findViewById(R.id.text_search);
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchActivity activity = (SearchActivity)getActivity();
                int categoryPosition = mCatFilter.getSelectedItemPosition();
                int sortPosition = mSortFilter.getSelectedItemPosition();
                int datePosition = mDateFilter.getSelectedItemPosition();
                int lengthPosition = mLengthFilter.getSelectedItemPosition();
                activity.queryUpdate(new FiltersPresenter().getFilteredQuery(categoryPosition, sortPosition, datePosition, lengthPosition));
                dismiss();
            }
        });
        return mRootView;
    }

}
