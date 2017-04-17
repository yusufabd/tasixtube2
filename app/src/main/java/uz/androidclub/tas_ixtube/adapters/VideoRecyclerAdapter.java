package uz.androidclub.tas_ixtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.activities.JCPlayer;
import uz.androidclub.tas_ixtube.activities.MainActivity;
import uz.androidclub.tas_ixtube.fragmnts.MoreDialogFragment;
import uz.androidclub.tas_ixtube.managers.SharedPrefManager;
import uz.androidclub.tas_ixtube.models.Video;
import uz.androidclub.tas_ixtube.utils.Constants;
import uz.androidclub.tas_ixtube.utils.StringUtils;

/**
 * Created by yusufabd on 2/18/2017.
 */

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.VideoViewHolder> implements Constants{

    private ArrayList<Video> mList;
    private Context mContext;
    private final int VIEW_TYPE_ITEM = 0, VIEW_TYPE_LOADING = 1;
    private SharedPrefManager mPref;
//    private OnLoadMoreListener mLoadListener;
    private FragmentManager mFragmentManager;

    public VideoRecyclerAdapter(ArrayList<Video> mList, Context mContext, FragmentManager  fm) {
        this.mList = mList;
        this.mContext = mContext;
        mPref = new SharedPrefManager(mContext);
        mFragmentManager = fm;
    }

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        final Video item = mList.get(position);
        holder.mTitle.setText(item.getTitle());

        if (item.getAuthor() != null && !item.getAuthor().isEmpty()){
            holder.mAuthor.setText(item.getAuthor());
            holder.mAuthor.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_author),
                    null, null, null);
        }

        if (item.getViews() != null && !item.getViews().isEmpty()){
            holder.mViews.setText(item.getViews());
            holder.mViews.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(R.drawable.ic_views),
                    null, null, null);
        }

        holder.mLength.setText(item.getLength());
        Picasso.with(mContext).load(item.getSmallImageLink()).fit().into(holder.mPreview);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPref.isExternalPlayer()){
                    Intent chooserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getMediumQualityVideo()));
                    mContext.startActivity(Intent.createChooser(chooserIntent, "View"));
                }else {
                    Intent activityIntent = new Intent(mContext, JCPlayer.class);
                    activityIntent.putExtra(EXTRA_OBJECT, item);
                    mContext.startActivity(activityIntent);
                }
            }
        });
        holder.mMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreDialogFragment dialogFragment = MoreDialogFragment.newInstance(mContext, item, Constants.LIST_TYPE_REGULAR);
                dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.CustomDialog);
                dialogFragment.show(mFragmentManager, "more");
            }
        });
    }

    @Override
    public int getItemCount() {
        try{
            return mList.size();
        }catch (NullPointerException e){
            StringUtils.showToast(mContext, R.string.unknown_error);
            Intent startIntent = new Intent(mContext, MainActivity.class);
            startIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
            mContext.startActivity(startIntent);
            return 0;
        }
    }

    class VideoViewHolder extends RecyclerView.ViewHolder{
        ImageView mPreview;
        ImageButton mMore;
        TextView mTitle, mAuthor, mViews, mLength;
        public VideoViewHolder(View itemView) {
            super(itemView);
            mPreview = (ImageView)itemView.findViewById(R.id.video_item_preview);
            mMore = (ImageButton) itemView.findViewById(R.id.button_more);
            mTitle = (TextView)itemView.findViewById(R.id.video_item_title);
            mAuthor = (TextView)itemView.findViewById(R.id.video_item_author);
            mViews = (TextView)itemView.findViewById(R.id.video_item_views);
            mLength = (TextView)itemView.findViewById(R.id.video_item_length);
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