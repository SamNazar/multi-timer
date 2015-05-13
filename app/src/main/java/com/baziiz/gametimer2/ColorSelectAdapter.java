package com.baziiz.gametimer2;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

/**
 * Created by samn on 5/12/15.
 */
public class ColorSelectAdapter extends BaseAdapter {
    private Context mContext;

    public ColorSelectAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mContext.getResources().getIntArray(R.array.player_colors).length;
    }

    public Object getItem(int position) {
        return mContext.getResources().getIntArray(R.array.player_colors)[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new textView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView colorView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            colorView = new TextView(mContext);
        } else {
            colorView = (TextView) convertView;
        }
        int color = mContext.getResources().getIntArray(R.array.player_colors)[position];
        // set text color based on brightness of background
        if (ColorUtility.getBrightness(color) > 127) {
            colorView.setTextColor(Color.BLACK);
        }
        else {
            colorView.setTextColor(Color.WHITE);
        }
        colorView.setText(mContext.getResources().getStringArray(R.array.player_color_names)[position]);
        colorView.setBackgroundColor(color);
        colorView.setHeight((int) (45 * mContext.getResources().getDisplayMetrics().density + 0.5f));

        return colorView;
    }
}
