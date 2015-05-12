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
    private int mHandler;
    private int mPosition[];


    public PlayerListAdapter(Context context, ArrayList<GamePlayer> dataItem, int handler) {
        super(context, R.layout.player_list_item, dataItem);
        this.data = dataItem;
        this.context = context;
        mHandler = handler;
        setup(data.size());
    }

    private void setup(int size) {
        mPosition = new int[size];

        for (int i = 0; i < size; ++i)
            mPosition[i] = i;
    }

    @Override
    public void notifyDataSetChanged() {
        setup(data.size());
        super.notifyDataSetChanged();
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

    @Override
    public void onItemDrag(DragNDropListView parent, View view, int position, long id) {

    }

    @Override
    public void onItemDrop(DragNDropListView parent, View view, int startPosition, int endPosition, long id) {
        GamePlayer movedPlayer = data.get(startPosition);

        if (startPosition < endPosition) {
            Log.i("POO", "DRAGGED DOWN ITEM " + String.valueOf(startPosition));
            for (int i = startPosition; i < endPosition; ++i)
                Collections.swap(data, i, i + 1);
        }
        else if (endPosition < startPosition) {
            Log.i("POO", "DRAGGED UP ITEM " + String.valueOf(startPosition));
            for (int i = startPosition; i > endPosition; --i)
                Collections.swap(data, i, i - 1);
        }
        //mPosition[endPosition] = position;
        data.set(endPosition, movedPlayer);
        Log.i("POO", "Starting at " + String.valueOf(startPosition) + " and going to " + String.valueOf(endPosition));
        super.notifyDataSetChanged();
    }

    @Override
    public int getDragHandler() {
        return mHandler;
    }
    public class ViewHolder {
        RelativeLayout layoutItem;
        TextView playerName;
        ImageButton buttonDeletePlayer;
        ImageButton buttonColorSelect;
    }

}
