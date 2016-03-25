package com.lefrantguillaume.animelist.controllers;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.util.List;

/**
 * Created by leniglo on 24/03/16.
 */
public class MyShowsAdapter extends RecyclerView.Adapter<MyShowsAdapter.ViewHolder> {
    private List<ShowModel> mDataset;

    public void clear() {
        mDataset = null;
    }

    public void setmDataset(List<ShowModel> myDataset) {
        mDataset = myDataset;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextViewName;
        public TextView mTextViewSeasonEpisode;
        public ImageButton mButtonSeason;
        public ImageButton mButtonEpisode;
        public Spinner mSpinnerStatus;

        public ViewHolder(View v, TextView textView1, TextView textView2, ImageButton imageButton1, ImageButton imageButton2, Spinner spinner) {
            super(v);
            mTextViewName = textView1;
            mTextViewSeasonEpisode = textView2;
            mButtonSeason = imageButton1;
            mButtonEpisode = imageButton2;
            mSpinnerStatus = spinner;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyShowsAdapter(List<ShowModel> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyShowsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_row, parent, false);

        TextView v1 = (TextView) view.findViewById(R.id.textViewName);
        TextView v2 = (TextView) view.findViewById(R.id.textViewSeasonEpisode);
        ImageButton v3 = (ImageButton) view.findViewById(R.id.btnSeason);
        ImageButton v4 = (ImageButton) view.findViewById(R.id.btnEpisode);
        Spinner v5 = (Spinner) view.findViewById(R.id.spinnerStatus);

        RelativeLayout topLayout = (RelativeLayout) view.findViewById(R.id.topLayout);
        topLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RelativeLayout bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomLayout);
                if (bottomLayout.getVisibility() == View.GONE)
                    bottomLayout.setVisibility(View.VISIBLE);
                else
                    bottomLayout.setVisibility(View.GONE);
            }
        });

        return new ViewHolder(view, v1, v2, v3, v4, v5);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ShowModel item = mDataset.get(position);

        holder.mTextViewName.setText(mDataset.get(position).getName());
        holder.mTextViewSeasonEpisode.setText("S" + item.getSeason() + "E" + item.getEpisode());

        switch (item.getStatus()) {
            case "Waiting":
                holder.mSpinnerStatus.setSelection(1);
                break;
            case "To See":
                holder.mSpinnerStatus.setSelection(2);
                break;
            case "Finished":
                holder.mSpinnerStatus.setSelection(3);
                break;
            default:
                holder.mSpinnerStatus.setSelection(0);
                break;
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
