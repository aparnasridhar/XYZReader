package com.example.xyzreader.adapter;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.xyzreader.R;
import com.example.xyzreader.data.ArticleLoader;
import com.example.xyzreader.data.ItemsContract;
import com.squareup.picasso.Picasso;

/**
 * Created by saparna on 9/19/15.
 */
public class ArticleListAdapter extends RecyclerView.Adapter<ArticleListAdapter.ViewHolder> {
    private Cursor mCursor;
    private Context mContext;

    public ArticleListAdapter(Cursor cursor,Context context) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public long getItemId(int position) {
        mCursor.moveToPosition(position);
        return mCursor.getLong(ArticleLoader.Query._ID);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_article, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        ItemsContract.Items.buildItemUri(getItemId(vh.getAdapterPosition())));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.titleView.setText(mCursor.getString(ArticleLoader.Query.TITLE));
        holder.subtitleView.setText(
                DateUtils.getRelativeTimeSpanString(
                        mCursor.getLong(ArticleLoader.Query.PUBLISHED_DATE),
                        System.currentTimeMillis(), DateUtils.HOUR_IN_MILLIS,
                        DateUtils.FORMAT_ABBREV_ALL).toString()
                        + " by "
                        + mCursor.getString(ArticleLoader.Query.AUTHOR));
        String url = mCursor.getString(ArticleLoader.Query.PHOTO_URL);
        if (url != null) {
            Picasso.with(mContext).load(url).into(holder.thumbnailView);
        }
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView thumbnailView;
        public TextView titleView;
        public TextView subtitleView;

        public ViewHolder(View view) {
            super(view);
            thumbnailView = (ImageView) view.findViewById(R.id.article_image);
            titleView = (TextView) view.findViewById(R.id.article_title);
            subtitleView = (TextView) view.findViewById(R.id.article_subtitle);
        }
    }
}
