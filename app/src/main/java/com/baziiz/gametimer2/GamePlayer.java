package com.baziiz.gametimer2;

/**
 * Created by samn on 5/3/15.
 */
public class GamePlayer {

    private String name;
    private int color;

    public GamePlayer(String name, int color) {
        this.name = name;
        this.color = color;
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
}
