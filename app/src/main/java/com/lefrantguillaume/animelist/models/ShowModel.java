package com.lefrantguillaume.animelist.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by leniglo on 02/03/16.
 */
public class ShowModel implements Parcelable {
    private final String _id;
    private String name;
    private String commentary;
    private String status;
    private final String type;
    private Integer season;
    private Integer episode;
    private final String owner;
    private String pic;
    private String link;
    private final Date createdAt;
    private final Date updatedAt;

    public ShowModel(String id, String name, String commentary, String status, String type, int season, int episode, String owner, String pic, String link, Date createdAt, Date updatedAt) {
        this._id = id;
        this.name = name;
        this.commentary = commentary;
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

    public String getCommentary() {
        return commentary;
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
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

    protected ShowModel(Parcel in) {
        _id = in.readString();
        name = in.readString();
        commentary = in.readString();
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
        dest.writeString(commentary);
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

    public String getParameter(String parameter) {
        switch (parameter) {
            case "_id":
                return this._id;
            case "name":
                return this.name;
            case "commentary":
                return this.commentary;
            case "status":
                return this.status;
            case "type":
                return this.type;
            case "season":
                return this.season.toString();
            case "episode":
                return this.episode.toString();
            case "owner":
                return this.owner;
            case "pic":
                return this.pic;
            case "link":
                return this.link;
            case "createdAt":
                return this.createdAt.toString();
            case "updatedAt":
                return this.updatedAt.toString();
            default:
                return null;
        }
    }

    public void setParameter(String parameter, Object value) {
        switch (parameter) {
            case "name":
                this.name = (String) value;
                break;
            case "commentary":
                this.commentary = (String) value;
                break;
            case "status":
                this.status = (String) value;
                break;
            case "season":
                this.season = Integer.parseInt((String) value);
                break;
            case "episode":
                this.episode = Integer.parseInt((String) value);
                break;
            case "pic":
                this.pic = (String) value;
                break;
            case "link":
                this.link = (String) value;
                break;
        }
    }
}