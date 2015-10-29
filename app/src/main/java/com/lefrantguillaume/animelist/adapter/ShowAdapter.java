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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by leniglo on 29/10/15.
 */
public class ShowAdapter extends BaseAdapter {

    private final Context context;
    private final List<ShowItem> objects;
    private final HashMap<String, ShowItem> itemHashMap;
    private LayoutInflater inflater = null;

    public ShowAdapter(Context context, HashMap<String, ShowItem> objects) {
        this.context = context;
        this.itemHashMap = objects;
        this.objects = new ArrayList<>(objects.values());
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

    @Override
    public long getItemId(int position) {
        return objects.get(position).getCreatedAt().getTime();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = inflater.inflate(R.layout.show_layout, parent, false);

        TextView textView = (TextView) rowView.findViewById(R.id.label);

        textView.setText(this.objects.get(position).toString());

        return rowView;
    }

}
