package uz.androidclub.tas_ixtube.fragmnts;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import uz.androidclub.tas_ixtube.presenter.PlayerPresenter;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.utils.Constants;

/**
 * Created by yusufabd on 3/5/2017.
 */

public class QualityDialogFragment extends DialogFragment implements Constants, View.OnClickListener{

    private View m240, m360, m480;
    private int mSec;
    private PlayerPresenter mPresenter;

    public static QualityDialogFragment newInstance(PlayerPresenter playerPresenter, int sec){
        QualityDialogFragment fragment = new QualityDialogFragment();
        fragment.setPresenter(playerPresenter);
        fragment.setSec(sec);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.dialog_fragment_quality, container, false);
        m240 = rootView.findViewById(R.id.view_240);
        m360 = rootView.findViewById(R.id.view_360);
        m480 = rootView.findViewById(R.id.view_480);
        m240.setOnClickListener(this);
        m360.setOnClickListener(this);
        m480.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.view_240:
                mPresenter.changeQuality(QUALITY_240, mSec);
                break;
            case R.id.view_360:
                mPresenter.changeQuality(QUALITY_360, mSec);
                break;
            case R.id.view_480:
                mPresenter.changeQuality(QUALITY_480, mSec);
                break;
        }
        dismiss();
    }

    public void setSec(int mSec) {
        this.mSec = mSec;
    }

    public void setPresenter(PlayerPresenter mPresenter) {
        this.mPresenter = mPresenter;
    }
}
