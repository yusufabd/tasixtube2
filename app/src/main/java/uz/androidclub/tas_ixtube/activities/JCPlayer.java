package uz.androidclub.tas_ixtube.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import uz.androidclub.tas_ixtube.adapters.PlayerRecyclerAdapter;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.adapters.VideoRecyclerAdapter;
import uz.androidclub.tas_ixtube.fragmnts.QualityDialogFragment;
import uz.androidclub.tas_ixtube.fragmnts.TagsDialogFragment;
import uz.androidclub.tas_ixtube.interfaces.PlayerContract;
import uz.androidclub.tas_ixtube.presenter.PlayerPresenter;
import uz.androidclub.tas_ixtube.utils.Constants;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

public class JCPlayer extends AppCompatActivity implements Constants, View.OnClickListener, PlayerContract.View{

    private Video mObject;

    private JCVideoPlayerStandard mPlayer;
    private AVLoadingIndicatorView mLoading;
    private RecyclerView mSimilarRecycler;
    private LinearLayout mOwnerCard, mDescriptionCard;
    private TextView mDownloadButton, mShareButton, mQualityButton, mTagsButton, mFavoriteButton;
            private ImageButton mArrowButton;
    private TextView mTextTitle, mTextViews, mTextOwner, mTextDescription;

    private PlayerPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jcplayer);
        if (savedInstanceState != null){
            mObject = (Video) savedInstanceState.getSerializable(SERIALIZABLE_VIDEO);
        }
        mObject = (Video) getIntent().getSerializableExtra(EXTRA_OBJECT);
        findViewById();
        mPresenter = new PlayerPresenter(this, this, mObject);
        mPresenter.startVideo();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(SERIALIZABLE_VIDEO, mObject);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    void findViewById(){
        mPlayer = (JCVideoPlayerStandard)findViewById(R.id.jc_player);
        mLoading = (AVLoadingIndicatorView)findViewById(R.id.loading_indicator);
        mSimilarRecycler = (RecyclerView)findViewById(R.id.video_similar);
        mSimilarRecycler.setLayoutManager(new LinearLayoutManager(this));
//        mDownloadButton = (TextView)findViewById(R.id.bottom_download);
//        mShareButton = (TextView)findViewById(R.id.top_share);
//        mQualityButton = (TextView)findViewById(R.id.bottom_quality);
//        mTagsButton = (TextView)findViewById(R.id.bottom_tags);
//        mFavoriteButton = (TextView)findViewById(R.id.bottom_star);
//        mArrowButton = (ImageButton)findViewById(R.id.arrow_down_button);
//        mTextTitle = (TextView)findViewById(R.id.video_title);
//        mTextViews = (TextView)findViewById(R.id.video_total_views);
//        mTextOwner = (TextView)findViewById(R.id.video_author);
//        mTextDescription = (TextView)findViewById(R.id.video_desc);
//        mOwnerCard = (LinearLayout)findViewById(R.id.card_author);
//        mDescriptionCard = (LinearLayout)findViewById(R.id.desc_container);
//        mOwnerCard.setOnClickListener(this);
//        mDownloadButton.setOnClickListener(this);
//        mShareButton.setOnClickListener(this);
//        mQualityButton.setOnClickListener(this);
//        mTagsButton.setOnClickListener(this);
//        mFavoriteButton.setOnClickListener(this);
//        mArrowButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mPresenter.onClick(v.getId());
    }

    @Override
    public void showVideo(String url) {
        setData();
        mSimilarRecycler.setAdapter(new PlayerRecyclerAdapter(mObject, this, getSupportFragmentManager()));
        mPlayer.setUp(mObject.getMediumQualityVideo(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
    }

    @Override
    public void setObject(Video object) {
        mObject = object;
    }

    @Override
    public void changeQuality(String url, int second) {
        mPlayer.setUp(mObject.getMediumQualityVideo(), JCVideoPlayer.SCREEN_LAYOUT_NORMAL, "");
    }

    @Override
    public void showTags() {
        TagsDialogFragment f = TagsDialogFragment.newInstance(mObject.getTags());
        f.show(getSupportFragmentManager(), "tags");
    }

    @Override
    public void showQuality() {
        QualityDialogFragment dialogFragment = QualityDialogFragment.newInstance(mPresenter, 0);
        dialogFragment.show(getSupportFragmentManager(), "quality");
    }

    @Override
    public void showLoading() {
        mLoading.show();
    }

    @Override
    public void hideLoading() {
        mLoading.hide();
    }

    @Override
    public void toggleDescription() {
        if (mDescriptionCard.isShown()){
            mArrowButton.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
            mDescriptionCard.setVisibility(View.GONE);
        }else {
            mArrowButton.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
            mDescriptionCard.setVisibility(View.VISIBLE);
        }
    }

    void setData(){
        Picasso.with(this).load(mObject.getMediumImageLink()).into(mPlayer.thumbImageView);
    }
}
