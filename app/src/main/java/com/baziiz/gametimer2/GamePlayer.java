package com.baziiz.gametimer2;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by samn on 5/3/15.
 */
public class GamePlayer
    implements Parcelable {

    private String name;
    private int color;

    public GamePlayer(String name, int color) {
        this.name = name;
        this.color = color;
    }

    public GamePlayer(Parcel in) {
        name = in.readString();
        color = in.readInt();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setColor(int red, int blue, int green) {
        // this will only work properly for values of red, green, and blue between 0 and 255
        // sets alpha to 255 (fully opaque)
        this.color = 0xFF000000 + ((red & 0xff) << 16) + ((blue & 0xFF) << 8) + (green & 0xFF);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeInt(color);
    }

    public static final Parcelable.Creator<GamePlayer> CREATOR
            = new Parcelable.Creator<GamePlayer>() {
        public GamePlayer createFromParcel(Parcel in) {
            return new GamePlayer(in);
        }

        public GamePlayer[] newArray(int size) {
            return new GamePlayer[size];
        }
    };
}
