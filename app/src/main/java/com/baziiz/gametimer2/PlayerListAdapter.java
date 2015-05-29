package com.baziiz.gametimer2;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import java.util.Collections;
import java.util.Map;

/**
 * Created by samn on 4/28/15.
 */
public class PlayerListAdapter extends ArrayAdapter<GamePlayer>
    implements DragNDropAdapter {

    ArrayList<GamePlayer> data;
    Context context;
    private int mHandle;


    public PlayerListAdapter(Context context, ArrayList<GamePlayer> dataItem, int handler) {
        super(context, R.layout.player_list_item, dataItem);
        this.data = dataItem;
        this.context = context;
        mHandle = handler;
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
            viewHolder.buttonNameEdit = (ImageButton) convertView
                    .findViewById(R.id.buttonIconPlayerListItemName);
            viewHolder.buttonDeletePlayer = (ImageButton) convertView
                    .findViewById(R.id.buttonIconPlayerListItemDelete);
            viewHolder.buttonColorSelect = (ImageButton) convertView
                    .findViewById(R.id.buttonIconPlayerListItemColor);
            viewHolder.dragHandle = (ImageView) convertView
                    .findViewById(R.id.dragHandle);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        GamePlayer player = getItem(position);
        // change color of cell contents based on brightness of background / player color
        if (ColorUtility.isDark(player.getColor())) {
            viewHolder.playerName.setTextColor(Color.WHITE);
            viewHolder.buttonColorSelect.setImageResource(R.drawable.ic_action_format_color_fill);
            viewHolder.buttonDeletePlayer.setImageResource(R.drawable.ic_action_remove_circle);
            viewHolder.buttonNameEdit.setImageResource(R.drawable.ic_action_mode_edit);
            viewHolder.dragHandle.setImageResource(R.drawable.ic_action_reorder);
        } else {
            viewHolder.playerName.setTextColor(Color.BLACK);
            viewHolder.buttonColorSelect.setImageResource(R.drawable.ic_action_format_color_fill_bk);
            viewHolder.buttonDeletePlayer.setImageResource(R.drawable.ic_action_remove_circle_bk);
            viewHolder.buttonNameEdit.setImageResource(R.drawable.ic_action_mode_edit_bk);
            viewHolder.dragHandle.setImageResource(R.drawable.ic_action_reorder_bk);
        }
        viewHolder.playerName.setText(player.getName());
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
        viewHolder.buttonNameEdit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ((ListView) parent).performItemClick(v, position, 0);
            }
        });

        return convertView;
    }

    @Override
    public void onItemDrag(DragNDropListView parent, View view, int position, long id) {

    }

    @Override
    public void onItemDrop(DragNDropListView parent, View view, int startPosition, int endPosition, long id) {
        GamePlayer movedPlayer = data.get(startPosition);

        if (startPosition < endPosition) {
            for (int i = startPosition; i < endPosition; ++i)
                Collections.swap(data, i, i + 1);
        }
        else if (endPosition < startPosition) {
            for (int i = startPosition; i > endPosition; --i)
                Collections.swap(data, i, i - 1);
        }
        data.set(endPosition, movedPlayer);
//        Log.i("POO", "Starting at " + String.valueOf(startPosition) + " and going to " + String.valueOf(endPosition));
    }

    public boolean hasPlayerWithName(String playerName) {
        for (GamePlayer pl: data) {
            if (pl.getName().equalsIgnoreCase(playerName))
                return true;
        }
        return false;
    }

    @Override
    public int getDragHandle() {
        return mHandle;
    }

    public class ViewHolder {
        RelativeLayout layoutItem;
        TextView playerName;
        ImageView dragHandle;
        ImageButton buttonDeletePlayer;
        ImageButton buttonColorSelect;
        ImageButton buttonNameEdit;
    }

}
