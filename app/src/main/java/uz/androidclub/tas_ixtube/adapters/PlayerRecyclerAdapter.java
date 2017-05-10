package uz.androidclub.tas_ixtube.adapters;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.activities.JCPlayer;
import uz.androidclub.tas_ixtube.activities.MainActivity;
import uz.androidclub.tas_ixtube.fragmnts.MoreDialogFragment;
import uz.androidclub.tas_ixtube.fragmnts.QualityDialogFragment;
import uz.androidclub.tas_ixtube.fragmnts.TagsDialogFragment;
import uz.androidclub.tas_ixtube.managers.DatabaseManager;
import uz.androidclub.tas_ixtube.managers.SharedPrefManager;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.presenter.PlayerPresenter;
import uz.androidclub.tas_ixtube.service.DownloadService;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.StringUtils;

/**
 * Created by yusufabd on 2/18/2017.
 */

public class PlayerRecyclerAdapter extends RecyclerView.Adapter<PlayerRecyclerAdapter.VideoViewHolder> implements Constants{

    private ArrayList<Video> mList;
    private Video mObject;
    private Context mCtx;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_HEADER = 1;
    private SharedPrefManager mPref;
    private FragmentManager mFragmentManager;
    private DatabaseManager mDB;
    private Intent mDownloadIntent;

    public PlayerRecyclerAdapter(Video obj, Context mCtx, FragmentManager  fm) {
        this.mCtx = mCtx;
        mObject = obj;
        this.mList = mObject.getSimilarVideoList();
        mPref = new SharedPrefManager(mCtx);
        mFragmentManager = fm;
        mDB = new DatabaseManager(mCtx);
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if (viewType == VIEW_TYPE_HEADER){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_item, parent, false);
        }else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        }
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final VideoViewHolder holder, int position) {
        if (position == 0){
            holder.mTextTitle.setText(mObject.getTitle());
            holder.mTextViews.setText(mCtx.getString(R.string.label_views, mObject.getViews()));
            holder.mTextOwner.setText(mObject.getAuthor());
            holder.mTextDescription.setText(mObject.getDescription());
            holder.mDownloadButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    download();
                }
            });
            holder.mShareButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                    sharingIntent.setType("text/plain");
                    String shareBody = mObject.getWatchLink();
                    sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                    mCtx.startActivity(Intent.createChooser(sharingIntent, mCtx.getString(R.string.share)));
                }
            });
            holder.mQualityButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    QualityDialogFragment dialogFragment = QualityDialogFragment.newInstance(new PlayerPresenter(mCtx, null, mObject), 0);
                    dialogFragment.show(mFragmentManager, "quality");
                }
            });
            holder.mTagsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TagsDialogFragment f = TagsDialogFragment.newInstance(mObject.getTags());
                    f.show(mFragmentManager, "tags");
                }
            });
            holder.mFavoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDB.addToFavorite(mObject);
                    StringUtils.showToast(mCtx, R.string.added_to_fav);
                }
            });
            holder.mArrowButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.mDescriptionCard.isShown()){
                        holder.mArrowButton.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
                        holder.mDescriptionCard.setVisibility(View.GONE);
                    }else {
                        holder.mArrowButton.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);
                        holder.mDescriptionCard.setVisibility(View.VISIBLE);
                    }
                }
            });
        }else {
            final Video item = mList.get(position);
            holder.mTitle.setText(item.getTitle());

            if (item.getAuthor() != null && !item.getAuthor().isEmpty()){
                holder.mAuthor.setText(item.getAuthor());
                holder.mAuthor.setCompoundDrawablesWithIntrinsicBounds(mCtx.getResources().getDrawable(R.drawable.ic_author),
                        null, null, null);
            }

            if (item.getViews() != null && !item.getViews().isEmpty()){
                holder.mViews.setText(item.getViews());
                holder.mViews.setCompoundDrawablesWithIntrinsicBounds(mCtx.getResources().getDrawable(R.drawable.ic_views),
                        null, null, null);
            }

            holder.mLength.setText(item.getLength());
            Picasso.with(mCtx).load(item.getSmallImageLink()).fit().into(holder.mPreview);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mPref.isExternalPlayer()){
                        Intent chooserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getMediumQualityVideo()));
                        mCtx.startActivity(Intent.createChooser(chooserIntent, "View"));
                    }else {
                        Intent activityIntent = new Intent(mCtx, JCPlayer.class);
                        activityIntent.putExtra(EXTRA_OBJECT, item);
                        mCtx.startActivity(activityIntent);
                    }
                }
            });
            holder.mMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MoreDialogFragment dialogFragment = MoreDialogFragment.newInstance(mCtx, item, Constants.LIST_TYPE_REGULAR);
                    dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                    dialogFragment.show(mFragmentManager, "more");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        try{
            return mList.size();
        }catch (NullPointerException e){
            StringUtils.showToast(mCtx, R.string.unknown_error);
            Intent startIntent = new Intent(mCtx, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mCtx.startActivity(startIntent);
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)
            return VIEW_TYPE_HEADER;
        return VIEW_TYPE_ITEM;
    }

    public void download() {
        if(ActivityCompat.checkSelfPermission(mCtx, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    (JCPlayer) mCtx,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_WES_PERMISSION);
            return;
        }

        AlertDialog dialog = new AlertDialog.Builder(mCtx)
                .setTitle(R.string.download)
                .setMessage(mCtx.getString(R.string.folder, Environment.DIRECTORY_DOWNLOADS))
                .setNegativeButton(R.string.start, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startDownload();
                    }
                }).setPositiveButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();

        dialog.show();
    }

    void startDownload(){
        mDownloadIntent = new Intent(mCtx, DownloadService.class);
        mDownloadIntent.setAction(ACTION_DOWNLOAD);
        mDownloadIntent.putExtra(EXTRA_KEY_LINK, mObject.getMediumQualityVideo());
        mDownloadIntent.putExtra(EXTRA_KEY_TITLE, mObject.getTitle());
        mCtx.startService(mDownloadIntent);
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView mPreview;
        ImageButton mMore;
        TextView mTitle, mAuthor, mViews, mLength;

        LinearLayout mOwnerCard, mDescriptionCard;
        TextView mDownloadButton, mShareButton, mQualityButton, mTagsButton, mFavoriteButton;
        ImageButton mArrowButton;
        TextView mTextTitle, mTextViews, mTextOwner, mTextDescription;
        public VideoViewHolder(View itemView) {
            super(itemView);
            mPreview = (ImageView)itemView.findViewById(R.id.video_item_preview);
            mMore = (ImageButton) itemView.findViewById(R.id.button_more);
            mTitle = (TextView)itemView.findViewById(R.id.video_item_title);
            mAuthor = (TextView)itemView.findViewById(R.id.video_item_author);
            mViews = (TextView)itemView.findViewById(R.id.video_item_views);
            mLength = (TextView)itemView.findViewById(R.id.video_item_length);

            mDownloadButton = (TextView)itemView.findViewById(R.id.bottom_download);
            mShareButton = (TextView)itemView.findViewById(R.id.top_share);
            mQualityButton = (TextView)itemView.findViewById(R.id.bottom_quality);
            mTagsButton = (TextView)itemView.findViewById(R.id.bottom_tags);
            mFavoriteButton = (TextView)itemView.findViewById(R.id.bottom_star);
            mArrowButton = (ImageButton)itemView.findViewById(R.id.arrow_down_button);
            mTextTitle = (TextView)itemView.findViewById(R.id.video_title);
            mTextViews = (TextView)itemView.findViewById(R.id.video_total_views);
            mTextOwner = (TextView)itemView.findViewById(R.id.video_author);
            mTextDescription = (TextView)itemView.findViewById(R.id.video_desc);
            mOwnerCard = (LinearLayout)itemView.findViewById(R.id.card_author);
            mDescriptionCard = (LinearLayout)itemView.findViewById(R.id.desc_container);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder{
        ProgressBar mProgressBar;
        public LoadingViewHolder(View itemView) {
            super(itemView);
            mProgressBar = (ProgressBar)itemView.findViewById(R.id.progress_lazy_load);
        }
    }
}