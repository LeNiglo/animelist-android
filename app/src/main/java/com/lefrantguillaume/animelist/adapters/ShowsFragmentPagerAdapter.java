package com.lefrantguillaume.animelist.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.fragments.ShowFragment;

/**
 * Created by leniglo on 09/11/15.
 */
public class ShowsFragmentPagerAdapter extends FragmentPagerAdapter {

    private final Context context;
    private final String tabTitles[] = new String[]{"Animes", "Series"};
    private ShowFragment animesFragment;
    private ShowFragment seriesFragment;

    public ShowsFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        this.animesFragment = ShowFragment.newInstance(context.getString(R.string.animes));
        this.seriesFragment = ShowFragment.newInstance(context.getString(R.string.series));
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position >= 0 && position < this.getCount())
            return tabTitles[position];
        else
            return "";
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return this.animesFragment;
        else if (position == 1)
            return this.seriesFragment;
        else
            return null;
    }

    @Override
    public int getCount() {
        return this.tabTitles.length;
    }

    public void clear() {
        if (this.animesFragment != null)
            this.animesFragment.clear();
        if (this.seriesFragment != null)
            this.seriesFragment.clear();
    }
}
