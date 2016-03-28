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

    private static ShowsController instance = null;
    private static final String TAG = ShowsController.class.getName();

    private final List<ShowModel> animesRunning;
    private final List<ShowModel> animesWaiting;
    private final List<ShowModel> animesTosee;
    private final List<ShowModel> animesFinished;
    private final List<ShowModel> seriesRunning;
    private final List<ShowModel> seriesWaiting;
    private final List<ShowModel> seriesTosee;
    private final List<ShowModel> seriesFinished;
    private int active;


    public static ShowsController getInstance() {
        if (instance == null) {
            instance = new ShowsController();
        }
        return instance;
    }

    private ShowsController() {
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
            switch (showModel.getStatus()) {
                case "Running":
                    this.animesRunning.add(showModel);
                    break;
                case "Waiting":
                    this.animesWaiting.add(showModel);
                    break;
                case "To See":
                    this.animesTosee.add(showModel);
                    break;
                case "Finished":
                    this.animesFinished.add(showModel);
                    break;
            }
        } else if (showModel.getType().equals("serie")) {
            switch (showModel.getStatus()) {
                case "Running":
                    this.seriesRunning.add(showModel);
                    break;
                case "Waiting":
                    this.seriesWaiting.add(showModel);
                    break;
                case "To See":
                    this.seriesTosee.add(showModel);
                    break;
                case "Finished":
                    this.seriesFinished.add(showModel);
                    break;
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

    public boolean changeItem(ShowModel item) {
        if (item.getType().equals("anime")) {
            for (int i1 = 0; i1 < animesRunning.size(); i1++) {
                ShowModel i = animesRunning.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(0, i1, item);
                }
            }
            for (int i1 = 0; i1 < animesWaiting.size(); i1++) {
                ShowModel i = animesWaiting.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(1, i1, item);
                }
            }
            for (int i1 = 0; i1 < animesTosee.size(); i1++) {
                ShowModel i = animesTosee.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(2, i1, item);
                }
            }
            for (int i1 = 0; i1 < animesFinished.size(); i1++) {
                ShowModel i = animesFinished.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(3, i1, item);
                }
            }
        } else if (item.getType().equals("serie")) {
            for (int i1 = 0; i1 < seriesRunning.size(); i1++) {
                ShowModel i = seriesRunning.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(4, i1, item);
                }
            }
            for (int i1 = 0; i1 < seriesWaiting.size(); i1++) {
                ShowModel i = seriesWaiting.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(5, i1, item);
                }
            }
            for (int i1 = 0; i1 < seriesTosee.size(); i1++) {
                ShowModel i = seriesTosee.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(6, i1, item);
                }
            }
            for (int i1 = 0; i1 < seriesFinished.size(); i1++) {
                ShowModel i = seriesFinished.get(i1);
                if (i.getId().equals(item.getId())) {
                    return compareAndUpdate(7, i1, item);
                }
            }
        }
        return false;
    }

    private boolean compareAndUpdate(int list, int position, ShowModel item) {
        switch (list) {
            case 0:
                if (item.getStatus().equals("Running")) {
                    this.animesRunning.set(position, item);
                } else {
                    this.animesRunning.remove(position);
                    switch (item.getStatus()) {
                        case "Waiting":
                            this.animesWaiting.add(item);
                            break;
                        case "To See":
                            this.animesTosee.add(item);
                            break;
                        case "Finished":
                            this.animesFinished.add(item);
                            break;
                    }
                }
                break;
            case 1:
                if (item.getStatus().equals("Waiting")) {
                    this.animesWaiting.set(position, item);
                } else {
                    this.animesWaiting.remove(position);
                    switch (item.getStatus()) {
                        case "Running":
                            this.animesRunning.add(item);
                            break;
                        case "To See":
                            this.animesTosee.add(item);
                            break;
                        case "Finished":
                            this.animesFinished.add(item);
                            break;
                    }
                }
                break;
            case 2:
                if (item.getStatus().equals("To See")) {
                    this.animesTosee.set(position, item);
                } else {
                    this.animesTosee.remove(position);
                    switch (item.getStatus()) {
                        case "Running":
                            this.animesRunning.add(item);
                            break;
                        case "Waiting":
                            this.animesWaiting.add(item);
                            break;
                        case "Finished":
                            this.animesFinished.add(item);
                            break;
                    }
                }
                break;
            case 3:
                if (item.getStatus().equals("Finished")) {
                    this.animesFinished.set(position, item);
                } else {
                    this.animesFinished.remove(position);
                    switch (item.getStatus()) {
                        case "Running":
                            this.animesRunning.add(item);
                            break;
                        case "Waiting":
                            this.animesWaiting.add(item);
                            break;
                        case "To See":
                            this.animesTosee.add(item);
                            break;
                    }
                }
                break;
            case 4:
                if (item.getStatus().equals("Running")) {
                    this.seriesRunning.set(position, item);
                } else {
                    this.seriesRunning.remove(position);
                    switch (item.getStatus()) {
                        case "Waiting":
                            this.seriesWaiting.add(item);
                            break;
                        case "To See":
                            this.seriesTosee.add(item);
                            break;
                        case "Finished":
                            this.seriesFinished.add(item);
                            break;
                    }
                }
                break;
            case 5:
                if (item.getStatus().equals("Waiting")) {
                    this.seriesWaiting.set(position, item);
                } else {
                    this.seriesWaiting.remove(position);
                    switch (item.getStatus()) {
                        case "Running":
                            this.seriesRunning.add(item);
                            break;
                        case "To See":
                            this.seriesTosee.add(item);
                            break;
                        case "Finished":
                            this.seriesFinished.add(item);
                            break;
                    }
                }
                break;
            case 6:
                if (item.getStatus().equals("To See")) {
                    this.seriesTosee.set(position, item);
                } else {
                    this.seriesTosee.remove(position);
                    switch (item.getStatus()) {
                        case "Running":
                            this.seriesRunning.add(item);
                            break;
                        case "Waiting":
                            this.seriesWaiting.add(item);
                            break;
                        case "Finished":
                            this.seriesFinished.add(item);
                            break;
                    }
                }
                break;
            case 7:
                if (item.getStatus().equals("Finished")) {
                    this.seriesFinished.set(position, item);
                } else {
                    this.seriesFinished.remove(position);
                    switch (item.getStatus()) {
                        case "Running":
                            this.seriesRunning.add(item);
                            break;
                        case "Waiting":
                            this.seriesWaiting.add(item);
                            break;
                        case "To See":
                            this.seriesTosee.add(item);
                            break;
                    }
                }
                break;
            default:
                return false;
        }
        return true;
    }

}
