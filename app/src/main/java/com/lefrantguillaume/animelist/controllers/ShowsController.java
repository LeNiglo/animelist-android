package com.lefrantguillaume.animelist.controllers;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leniglo on 02/03/16.
 */
public class ShowsController {

    private static ShowsController instance = null;
    private static final String TAG = ShowsController.class.getName();

    private final List<ShowModel> shows;
    private int active;


    public static ShowsController getInstance() {
        if (instance == null) {
            instance = new ShowsController();
        }
        return instance;
    }

    private ShowsController() {
        active = -1;
        shows = new ArrayList<>();
    }

    public void setActive(final int id) {
        this.active = id;
    }

    public int getActive() {
        return this.active;
    }

    public List<ShowModel> getActiveList() {
        final List<ShowModel> activeList = new ArrayList<>();

        for (int i = 0; i < shows.size(); i++) {
            switch (this.active) {
                case R.id.nav_animes_running:
                    if (shows.get(i).getType().equals("anime") && shows.get(i).getStatus().equals("Running"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_animes_waiting:
                    if (shows.get(i).getType().equals("anime") && shows.get(i).getStatus().equals("Waiting"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_animes_tosee:
                    if (shows.get(i).getType().equals("anime") && shows.get(i).getStatus().equals("To See"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_animes_finished:
                    if (shows.get(i).getType().equals("anime") && shows.get(i).getStatus().equals("Finished"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_series_running:
                    if (shows.get(i).getType().equals("serie") && shows.get(i).getStatus().equals("Running"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_series_waiting:
                    if (shows.get(i).getType().equals("serie") && shows.get(i).getStatus().equals("Waiting"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_series_tosee:
                    if (shows.get(i).getType().equals("serie") && shows.get(i).getStatus().equals("To See"))
                        activeList.add(shows.get(i));
                    break;
                case R.id.nav_series_finished:
                    if (shows.get(i).getType().equals("serie") && shows.get(i).getStatus().equals("Finished"))
                        activeList.add(shows.get(i));
                    break;
            }
        }
        return activeList;
    }

    public List<ShowModel> getShows() {
        return this.shows;
    }

    public void addDocument(ShowModel showModel) {
        this.shows.add(showModel);
    }

    public void clear() {
        this.shows.clear();
    }

    public boolean changeItem(ShowModel item) {
        for (int i = 0; i < shows.size(); i++) {
            ShowModel model = shows.get(i);
            if (model.getId().equals(item.getId())) {
                this.shows.set(i, item);
                return true;
            }
        }
        return false;
    }
}
