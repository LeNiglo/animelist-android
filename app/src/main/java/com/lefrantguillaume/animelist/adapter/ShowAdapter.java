package com.lefrantguillaume.animelist.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.model.ShowItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leniglo on 29/10/15.
 */
public class ShowAdapter extends BaseAdapter {

    private final Context context;
    private final List<ShowItem> objects;
    private final HashMap<String, ShowItem> mMap;
    private LayoutInflater inflater = null;

    public ShowAdapter(Context context) {
        this(context, new HashMap<String, ShowItem>());
    }

    public ShowAdapter(Context context, HashMap<String, ShowItem> objects) {
        this.context = context;
        this.objects = new ArrayList<>();
        this.mMap = objects;
        this.objects.addAll(objects.values());
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Object getItem(int position) {
        if (position >= 0 && position < objects.size())
            return objects.get(position);
        else
            return null;
    }

    public void addItem(String id, ShowItem item) {
        mMap.put(id, item);
        objects.clear();
        objects.addAll(mMap.values());
        notifyDataSetChanged();
    }

    public void removeItem(String id) {
        mMap.remove(id);
        objects.clear();
        objects.addAll(mMap.values());
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        ItemListViewHolder viewHolder;

        if (convertView == null) {
            v = inflater.inflate(R.layout.show_layout, parent, false);
            viewHolder = new ItemListViewHolder(v);
            v.setTag(viewHolder);
        } else {
            viewHolder = (ItemListViewHolder) v.getTag();
        }
        viewHolder.label.setText(this.objects.get(position).toString());
        Picasso.with(context).load(this.objects.get(position).getPic()).into(viewHolder.image);
        return v;
    }

    class ItemListViewHolder {
        public TextView label;
        public ImageView image;
        public ItemListViewHolder(View base) {
            label = (TextView) base.findViewById(R.id.label);
            image = (ImageView) base.findViewById(R.id.image);
        }
    }
}
