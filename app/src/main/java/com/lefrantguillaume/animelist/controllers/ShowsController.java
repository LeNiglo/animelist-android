package com.lefrantguillaume.animelist.controllers;

import android.util.Log;

import com.lefrantguillaume.animelist.R;
import com.lefrantguillaume.animelist.models.ShowModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by leniglo on 02/03/16.
 */
public class ShowsController {

    private List<ShowModel> animesRunning;
    private List<ShowModel> animesWaiting;
    private List<ShowModel> animesTosee;
    private List<ShowModel> animesFinished;
    private List<ShowModel> seriesRunning;
    private List<ShowModel> seriesWaiting;
    private List<ShowModel> seriesTosee;
    private List<ShowModel> seriesFinished;
    private int active;

    public ShowsController() {
        active = -1;
        animesRunning = new ArrayList<>();
        animesWaiting = new ArrayList<>();
        animesTosee = new ArrayList<>();
        animesFinished = new ArrayList<>();
        seriesRunning = new ArrayList<>();
        seriesWaiting = new ArrayList<>();
        seriesTosee = new ArrayList<>();
        seriesFinished = new ArrayList<>();
    }

    public void setActive(final int id) {
        this.active = id;
    }

    public int getActive() {
        return this.active;
    }

    public List<ShowModel> getActiveList() {
        if (this.active == R.id.nav_animes_running) {
            return this.animesRunning;
        } else if (this.active == R.id.nav_animes_waiting) {
            return this.animesWaiting;
        } else if (this.active == R.id.nav_animes_tosee) {
            return this.animesTosee;
        } else if (this.active == R.id.nav_animes_finished) {
            return this.animesFinished;
        } else if (this.active == R.id.nav_series_running) {
            return this.seriesRunning;
        } else if (this.active == R.id.nav_series_waiting) {
            return this.seriesWaiting;
        } else if (this.active == R.id.nav_series_tosee) {
            return this.seriesTosee;
        } else if (this.active == R.id.nav_series_finished) {
            return this.seriesFinished;
        } else {
            return new ArrayList<>();
        }
    }

    public List<ShowModel> getAll() {
        List<ShowModel> newList = new ArrayList<>();
        newList.addAll(this.animesRunning);
        newList.addAll(this.animesWaiting);
        newList.addAll(this.animesTosee);
        newList.addAll(this.animesFinished);
        newList.addAll(this.seriesRunning);
        newList.addAll(this.seriesWaiting);
        newList.addAll(this.seriesTosee);
        newList.addAll(this.seriesFinished);
        return newList;
    }

    public void addDocument(ShowModel showModel) {
        if (showModel.getType().equals("anime")) {
            if (showModel.getStatus().equals("Running")) {
                this.animesRunning.add(showModel);
            } else if (showModel.getStatus().equals("Waiting")) {
                this.animesWaiting.add(showModel);
            } else if (showModel.getStatus().equals("To See")) {
                this.animesTosee.add(showModel);
            } else if (showModel.getStatus().equals("Finished")) {
                this.animesFinished.add(showModel);
            }
        } else if (showModel.getType().equals("serie")) {
            if (showModel.getStatus().equals("Running")) {
                this.seriesRunning.add(showModel);
            } else if (showModel.getStatus().equals("Waiting")) {
                this.seriesWaiting.add(showModel);
            } else if (showModel.getStatus().equals("To See")) {
                this.seriesTosee.add(showModel);
            } else if (showModel.getStatus().equals("Finished")) {
                this.seriesFinished.add(showModel);
            }
        }
    }

    public void clear() {
        animesRunning.clear();
        animesWaiting.clear();
        animesTosee.clear();
        animesFinished.clear();
        seriesRunning.clear();
        seriesWaiting.clear();
        seriesTosee.clear();
        seriesFinished.clear();
    }
}
