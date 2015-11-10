package com.lefrantguillaume.animelist.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.models.ShowItem;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by leniglo on 10/11/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private LinkedHashMap<String, ShowItem> mDataset;

    private LayoutInflater mInflater;
    private Context context;

    public RecyclerAdapter(Context context, LinkedHashMap<String, ShowItem> data) {
        this.context = context;

        mInflater = LayoutInflater.from(context);
        mDataset = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.row_show, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        List<ShowItem> l = new ArrayList<>(mDataset.values());

        final ShowItem current = l.get(position);
        holder.mTitle.setText(current.getName());
        holder.mShow.setText(context.getString(R.string.season_episode, current.getSeason(), current.getEpisode()));

        Log.i("BindViewHolder", current.toString());
        if (current.getLink() != null && !current.getLink().isEmpty()) {
            holder.mButton.setClickable(true);
            holder.mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.w("OPEN BROWSER", current.getLink());
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(current.getLink())));
                }
            });
        } else {
            holder.mButton.setClickable(true);
            holder.mButton.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        TextView mShow;
        Button mButton;

        public MyViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            mTitle = (TextView) itemView.findViewById(R.id.titleTextView);
            mShow = (TextView) itemView.findViewById(R.id.showTextView);
            mButton = (Button) itemView.findViewById(R.id.showButton);

            itemView.setClickable(true);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("RecyclerAdapter", "Clicked on item : " + v.toString());
        }
    }
}