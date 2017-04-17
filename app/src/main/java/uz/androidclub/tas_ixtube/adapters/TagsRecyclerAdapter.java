package uz.androidclub.tas_ixtube.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import uz.androidclub.tas_ixtube.R;
import uz.androidclub.tas_ixtube.activities.TagsActivity;
import uz.androidclub.tas_ixtube.utils.Constants;

/**
 * Created by yusufabd on 2/25/2017.
 */

public class TagsRecyclerAdapter extends RecyclerView.Adapter<TagsRecyclerAdapter.ViewHolder> implements Constants{
    private Context mCtx;
    private String[] tags;

    public TagsRecyclerAdapter(Context ctx, String[] tags) {
        mCtx = ctx;
        this.tags = tags;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tag_item, parent, false);
        return new TagsRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String tag = "#" + tags[position];
        holder.text.setText(tag);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activityIntent = new Intent(mCtx, TagsActivity.class);
                activityIntent.putExtra(EXTRA_TAG, tags[position]);
                mCtx.startActivity(activityIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return tags.length;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView text;
        ViewHolder(View itemView) {
            super(itemView);
            text = (TextView)itemView.findViewById(R.id.text_tag);
        }
    }
}
