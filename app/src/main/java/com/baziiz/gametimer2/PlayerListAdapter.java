package com.baziiz.gametimer2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by samn on 4/28/15.
 */
public class PlayerListAdapter extends ArrayAdapter<GamePlayer> {

    ArrayList<GamePlayer> data;
    Context context;

    public PlayerListAdapter(Context context, ArrayList<GamePlayer> dataItem) {
        super(context, R.layout.player_list_item, dataItem);
        this.data = dataItem;
        this.context = context;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.player_list_item, null);
            viewHolder = new ViewHolder();
            viewHolder.layoutItem = (RelativeLayout) convertView
                    .findViewById(R.id.relLayoutPlayerListItem);
            viewHolder.playerName = (TextView) convertView
                    .findViewById(R.id.textViewPlayerListItemName);
            viewHolder.buttonDeletePlayer = (ImageButton) convertView
                    .findViewById(R.id.buttonIconPlayerListItemDelete);
            viewHolder.buttonColorSelect = (ImageButton) convertView
                    .findViewById(R.id.buttonIconPlayerListItemColor);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GamePlayer player = getItem(position);
        final String temp = player.getName();
        int brightness = ColorUtility.getBrightness(player.getColor());
        if (brightness < 128) {
            viewHolder.playerName.setTextColor(Color.WHITE);
        } else {
            viewHolder.playerName.setTextColor(Color.BLACK);
        }
        viewHolder.playerName.setText(temp + " - " + String.valueOf(brightness));
        viewHolder.layoutItem.setBackgroundColor(getItem(position).getColor());

        viewHolder.buttonDeletePlayer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });
        viewHolder.buttonColorSelect.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        return convertView;
    }

    public class ViewHolder {
        RelativeLayout layoutItem;
        TextView playerName;
        ImageButton buttonDeletePlayer;
        ImageButton buttonColorSelect;
    }

}
