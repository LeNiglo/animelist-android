package com.lefrantguillaume.animelist.controllers;


import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.koushikdutta.async.future.FutureCallback;
import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.activities.DetailsActivity;
import com.lefrantguillaume.animelist.activities.MainActivity;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.util.List;

/**
 * Created by leniglo on 24/03/16.
 */
public class ShowsAdapter extends RecyclerView.Adapter<ShowsAdapter.ViewHolder> {
    private List<ShowModel> mDataset;
    private final Activity activity;

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
        public RelativeLayout mTopLayout;
        public RelativeLayout mBottomLayout;

        public ViewHolder(View v, TextView textView1, TextView textView2, ImageButton imageButton1, ImageButton imageButton2, Spinner spinner, RelativeLayout topLayout, RelativeLayout bottomLayout) {
            super(v);
            mTextViewName = textView1;
            mTextViewSeasonEpisode = textView2;
            mButtonSeason = imageButton1;
            mButtonEpisode = imageButton2;
            mSpinnerStatus = spinner;
            mTopLayout = topLayout;
            mBottomLayout = bottomLayout;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ShowsAdapter(Activity activity, List<ShowModel> myDataset) {
        this.mDataset = myDataset;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ShowsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.show_row, parent, false);

        TextView v1 = (TextView) view.findViewById(R.id.textViewName);
        TextView v2 = (TextView) view.findViewById(R.id.textViewSeasonEpisode);
        ImageButton v3 = (ImageButton) view.findViewById(R.id.btnSeason);
        ImageButton v4 = (ImageButton) view.findViewById(R.id.btnEpisode);
        Spinner v5 = (Spinner) view.findViewById(R.id.spinnerStatus);

        RelativeLayout topLayout = (RelativeLayout) view.findViewById(R.id.topLayout);
        RelativeLayout bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomLayout);

        return new ViewHolder(view, v1, v2, v3, v4, v5, topLayout, bottomLayout);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final ShowModel item = mDataset.get(position);

        final RelativeLayout bottom = holder.mBottomLayout;
        final RelativeLayout top = holder.mTopLayout;

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

        holder.mTextViewName.setText(mDataset.get(position).getName());
        holder.mTextViewSeasonEpisode.setText(String.format("S%dE%d", item.getSeason(), item.getEpisode()));
        holder.mBottomLayout.setVisibility(View.GONE);

        top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottom.getVisibility() == View.GONE)
                    bottom.setVisibility(View.VISIBLE);
                else
                    bottom.setVisibility(View.GONE);
            }
        });

        top.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(activity, DetailsActivity.class);
                intent.putExtra("ShowModel", item);
                activity.startActivity(intent);
                return true;
            }
        });

        holder.mButtonSeason.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetController.sendChangement(activity, item, "season", item.getSeason() + 1, new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            try {
                                if (Integer.parseInt(result) > 0) {
                                    item.setParameter("season", String.valueOf(item.getSeason() + 1));
                                    ShowsController.getInstance().changeItem(item);
                                    ((MainActivity) activity).updateAdapter();
                                } else {
                                    Snackbar.make(activity.findViewById(R.id.shows_list), "Error while updating item.", Snackbar.LENGTH_LONG).show();
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        holder.mButtonEpisode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NetController.sendChangement(activity, item, "episode", item.getEpisode() + 1, new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            e.printStackTrace();
                        } else {
                            try {
                                if (Integer.parseInt(result) > 0) {
                                    item.setParameter("episode", String.valueOf(item.getEpisode() + 1));
                                    ShowsController.getInstance().changeItem(item);
                                    ((MainActivity) activity).updateAdapter();
                                } else {
                                    Snackbar.make(activity.findViewById(R.id.shows_list), "Error while updating item.", Snackbar.LENGTH_LONG).show();
                                }
                            } catch (NumberFormatException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                });
            }
        });

        holder.mSpinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, final int position, long id) {
                final String newStatus = (String) parent.getItemAtPosition(position);
                if (!newStatus.equals(item.getStatus())) {
                    NetController.sendChangement(activity, item, "status", newStatus, new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (e != null) {
                                e.printStackTrace();
                            } else {
                                try {
                                    if (Integer.parseInt(result) > 0) {
                                        item.setParameter("status", newStatus);
                                        ShowsController.getInstance().changeItem(item);
                                        ((MainActivity) activity).updateAdapter();
                                    } else {
                                        Snackbar.make(activity.findViewById(R.id.shows_list), "Error while updating item.", Snackbar.LENGTH_LONG).show();
                                    }
                                } catch (NumberFormatException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

}
