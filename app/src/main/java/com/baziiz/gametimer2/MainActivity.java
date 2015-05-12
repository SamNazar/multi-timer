package com.baziiz.gametimer2;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public static final int MAX_PLAYERS = 8;

    FloatingActionButton buttonFloatAddPlayer;
    DragNDropListView listViewPlayers;
    ArrayList<GamePlayer> dataItems;
    PlayerListAdapter adapter;
    int blah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonFloatAddPlayer = (FloatingActionButton) this.findViewById(R.id.buttonFloatAddPlayer);
        listViewPlayers = (DragNDropListView) this.findViewById(R.id.listViewPlayers);

        dataItems = new ArrayList<>();
        dataItems.add(new GamePlayer("Player 1", Color.RED));
        adapter = new PlayerListAdapter(MainActivity.this, dataItems, R.id.dragHandle);
        listViewPlayers.setDragNDropAdapter(adapter);

        buttonFloatAddPlayer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){

                if (adapter.getCount() < MAX_PLAYERS) {
                    adapter.add(new GamePlayer("Player " + String.valueOf(blah), Color.BLUE));
                    adapter.notifyDataSetChanged();
                    blah = blah + 1;
                    if (adapter.getCount() == MAX_PLAYERS) {
                        buttonFloatAddPlayer.setEnabled(false);
                    }
                } else {
                    Toast.makeText(v.getContext(), R.string.error_max_players, Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                long viewId = view.getId();

                if (viewId == R.id.buttonIconPlayerListItemColor) {
                    onItemSetColorClicked(position);
                } else if (viewId == R.id.buttonIconPlayerListItemDelete) {
                    onItemDeleteClicked(position);
                } else {
                    Toast.makeText(parent.getContext(), "ListView clicked" + id, Toast.LENGTH_SHORT).show();
                }
            }
        });

        blah = 1;
    }

//    public void onAddPlayerButtonClicked(View v){
//
//        if (adapter.getCount() < MAX_PLAYERS) {
//            adapter.add(new GamePlayer("Player " + String.valueOf(blah), Color.BLUE));
//            adapter.notifyDataSetChanged();
//            blah = blah + 1;
//            if (adapter.getCount() == MAX_PLAYERS) {
//                buttonFloatAddPlayer.setEnabled(false);
//            }
//        } else {
//            Toast.makeText(this, R.string.error_max_players, Toast.LENGTH_SHORT).show();
//        }
//    }

    public void onItemDeleteClicked(int position){
        adapter.remove(adapter.getItem(position));
        adapter.notifyDataSetChanged();
        buttonFloatAddPlayer.setEnabled(true);
    }

    public void onItemSetColorClicked(final int position){
    }

    public void onBeginGameButtonClicked(View beginButton){

        Intent intent = new Intent(this, GamePlayActivity.class);
        intent.putExtra("Players", adapter.getCount());
        startActivity(intent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
