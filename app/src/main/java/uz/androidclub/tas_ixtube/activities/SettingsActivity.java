package uz.androidclub.tas_ixtube.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import uz.androidclub.tas_ixtube.interfaces.SettingsContract;
import uz.androidclub.tas_ixtube.presenter.SettingsPresenter;
import uz.androidclub.tas_ixtube.R;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, SettingsContract.View {

    private AppCompatSpinner mSpinnerLang;
    private RelativeLayout mTogglePlayerContainer;
    private Switch mTogglePlayer;
    private TextView mClearSearchHistory, mClearFavorite, mInstructions, mAbout;
    private SettingsPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mPresenter = new SettingsPresenter(this, this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle(getString(R.string.settings));
        initUI();
    }

    private void initUI() {
        mSpinnerLang = (AppCompatSpinner)findViewById(R.id.spinner_lang);
        mSpinnerLang.setSelection(mPresenter.getLang());
        mSpinnerLang.setOnItemSelectedListener(mLangListener);
        mTogglePlayerContainer = (RelativeLayout)findViewById(R.id.toggle_player_container);
        mTogglePlayer = (Switch)findViewById(R.id.toggle_player);
        mTogglePlayer.setChecked(mPresenter.isExternalPlayer());
        mClearSearchHistory = (TextView)findViewById(R.id.text_clear_search);
        mClearFavorite = (TextView)findViewById(R.id.text_clear_fav);
        mInstructions = (TextView)findViewById(R.id.text_instructions);
        mAbout = (TextView)findViewById(R.id.text_about);

        mTogglePlayerContainer.setOnClickListener(this);
        mClearSearchHistory.setOnClickListener(this);
        mClearFavorite.setOnClickListener(this);
        mInstructions.setOnClickListener(this);
        mAbout.setOnClickListener(this);

        mTogglePlayer.setOnCheckedChangeListener(mSwitchListener);
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

    @Override
    public void onBackPressed() {
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(startIntent);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
    }

    @Override
    public void switchPlayer() {
        mTogglePlayer.toggle();
    }

    private CompoundButton.OnCheckedChangeListener mSwitchListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            mPresenter.onSwitched(isChecked);
        }
    };

    private AdapterView.OnItemSelectedListener mLangListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            mPresenter.onSpinnerItemSelected(position);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };
}
