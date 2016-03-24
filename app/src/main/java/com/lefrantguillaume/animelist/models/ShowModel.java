package com.lefrantguillaume.animelist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by leniglo on 02/03/16.
 */
public class ShowModel implements Parcelable {
    private final String _id;
    private final String name;
    private final String status;
    private final String type;
    private final int season;
    private final int episode;
    private final String owner;
    private final String pic;
    private final String link;
    private final Date createdAt;
    private final Date updatedAt;

    public ShowModel(String id, String name, String status, String type, int season, int episode, String owner, String pic, String link, Date createdAt, Date updatedAt) {
        this._id = id;
        this.name = name;
        this.status = status;
        this.type = type;
        this.season = season;
        this.episode = episode;
        this.owner = owner;
        this.pic = pic;
        this.link = link;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public String toString() {
        return (this.type + " '" + this.name + "' [" + this.status + "] S" + this.season + " E" + this.episode + ".");
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

    public String getType() {
        return type;
    }

    public int getSeason() {
        return season;
    }

    public int getEpisode() {
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

    protected ShowModel(Parcel in) {
        _id = in.readString();
        name = in.readString();
        status = in.readString();
        type = in.readString();
        season = in.readInt();
        episode = in.readInt();
        owner = in.readString();
        pic = in.readString();
        link = in.readString();
        long tmpCreatedAt = in.readLong();
        createdAt = tmpCreatedAt != -1 ? new Date(tmpCreatedAt) : null;
        long tmpUpdatedAt = in.readLong();
        updatedAt = tmpUpdatedAt != -1 ? new Date(tmpUpdatedAt) : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(name);
        dest.writeString(status);
        dest.writeString(type);
        dest.writeInt(season);
        dest.writeInt(episode);
        dest.writeString(owner);
        dest.writeString(pic);
        dest.writeString(link);
        dest.writeLong(createdAt != null ? createdAt.getTime() : -1L);
        dest.writeLong(updatedAt != null ? updatedAt.getTime() : -1L);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ShowModel> CREATOR = new Parcelable.Creator<ShowModel>() {
        @Override
        public ShowModel createFromParcel(Parcel in) {
            return new ShowModel(in);
        }

        @Override
        public ShowModel[] newArray(int size) {
            return new ShowModel[size];
        }
    };

}