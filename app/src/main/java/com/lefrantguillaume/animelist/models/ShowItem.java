package com.lefrantguillaume.animelist.models;

import java.util.Date;

/**
 * Created by leniglo on 10/11/15.
 */
public class ShowItem {
    private final String _id;
    private final String name;
    private final String status;
    private final Integer season;
    private final Integer episode;
    private final String owner;
    private final String pic;
    private final String link;
    private final Date createdAt;
    private final Date updatedAt;


    public ShowItem(String id, String name, String status, Integer season, Integer episode, String owner, String pic, String link, Date createdAt, Date updatedAt) {
        this._id = id;
        this.name = name;
        this.status = status;
        this.season = season;
        this.episode = episode;
        this.owner = owner;
        this.pic = pic;
        this.link = link;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String toString() {
        return (this.name + " [" + this.status + "] S" + this.season + " E" + this.episode + ".");
    }

    public String getId() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public String getStatus() {
        return status;
    }

    public Integer getSeason() {
        return season;
    }

    public Integer getEpisode() {
        return episode;
    }

    public String getOwner() {
        return owner;
    }

    public String getPic() {
        return pic;
    }

    public String getLink() {
        return link;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }
}
