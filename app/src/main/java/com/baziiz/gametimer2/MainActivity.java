package com.baziiz.gametimer2;

import android.app.DialogFragment;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.rey.material.widget.FloatingActionButton;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity
    implements ColorSelectionDialogFragment.ColorDialogListener {

    public static final int MAX_PLAYERS = 8;
    public static final int NEW_PLAYER_POSITION = -1;

    FloatingActionButton buttonFloatAddPlayer;
    DragNDropListView listViewPlayers;
    ArrayList<GamePlayer> dataItems;
    PlayerListAdapter adapter;
    // keeps track of the index of the player we are currently editing
    private int currentPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        currentPlayer = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonFloatAddPlayer = (FloatingActionButton) this.findViewById(R.id.buttonFloatAddPlayer);
        listViewPlayers = (DragNDropListView) this.findViewById(R.id.listViewPlayers);

        dataItems = new ArrayList<>();
        dataItems.add(new GamePlayer("Green Player", getResources().getColor(R.color.bg_green)));
        adapter = new PlayerListAdapter(MainActivity.this, dataItems, R.id.dragHandle);
        listViewPlayers.setDragNDropAdapter(adapter);

        buttonFloatAddPlayer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){

                if (adapter.getCount() < MAX_PLAYERS) {
                    // we can add a new player
                    // show the color selection dialog for the new player
                    showColorSelectionDialog(NEW_PLAYER_POSITION);
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
    }

    private void addPlayer(int colorIndex) {
        final int[] colorList = getResources().getIntArray(R.array.player_colors);
        final String[] colorNameList = getResources().getStringArray(R.array.player_color_names);
        int color = colorList[colorIndex];
        adapter.add(new GamePlayer(colorNameList[colorIndex] + " Player", color));
        adapter.notifyDataSetChanged();
    }

//    public void onAddPlayerButtonClicked(View v){
//
//        if (adapter.getCount() < MAX_PLAYERS) {
//            adapter.add(new GamePlayer("Player " + String.valueOf(addedPlayerIndex), Color.BLUE));
//            adapter.notifyDataSetChanged();
//            addedPlayerIndex = addedPlayerIndex + 1;
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
        showColorSelectionDialog(position);
    }

    public void showColorSelectionDialog(int playerPosition) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ColorSelectionDialogFragment();
        dialog.show(getFragmentManager(), "ColorSelectionDialog");
        currentPlayer = playerPosition;
    }

    @Override
    public void onColorSelected(int colorIndex) {
        // user selected a color in the color selection dialog
        int colorValue = getResources().getIntArray(R.array.player_colors)[colorIndex];
        GamePlayer playerToChange;
        if (currentPlayer == NEW_PLAYER_POSITION) {
            // we are adding a new player
            addPlayer(colorIndex);
        } else {
            // we are editing the color of an existing player
            playerToChange = dataItems.get(currentPlayer);
            playerToChange.setColor(colorValue);
            dataItems.set(currentPlayer, playerToChange);
            adapter.notifyDataSetChanged();
            String colorName = getResources().getStringArray(R.array.player_color_names)[colorIndex];
            Toast.makeText(this, playerToChange.getName() + " set to " + colorName , Toast.LENGTH_SHORT).show();
        }
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
