package com.baziiz.gametimer2;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
        super.onCreate(savedInstanceState);

        // restore player data if activity is being re-created:
        if (savedInstanceState != null) {
            dataItems = savedInstanceState.getParcelableArrayList("dataItems");
            currentPlayer = savedInstanceState.getInt("currentPlayer");
        } else {
            // otherwise initialize the data
            dataItems = new ArrayList<>();
            dataItems.add(new GamePlayer(getResources().getStringArray(R.array.player_color_names)[9], getResources().getColor(R.color.bg_green)));
            currentPlayer = 0;
        }

        setContentView(R.layout.activity_main);
        buttonFloatAddPlayer = (FloatingActionButton) this.findViewById(R.id.buttonFloatAddPlayer);
        listViewPlayers = (DragNDropListView) this.findViewById(R.id.listViewPlayers);

        adapter = new PlayerListAdapter(MainActivity.this, dataItems, R.id.dragHandle);
        listViewPlayers.setDragNDropAdapter(adapter);

        buttonFloatAddPlayer.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v){

                if (adapter.getCount() < MAX_PLAYERS) {
                    // we can add a new player
                    // show the color selection dialog for the new player
                    showColorSelectionDialog(NEW_PLAYER_POSITION);
//                    if (adapter.getCount() == MAX_PLAYERS) {
//                        buttonFloatAddPlayer.setEnabled(false);
//                    }
                } else {
                    Toast.makeText(v.getContext(), R.string.error_max_players, Toast.LENGTH_SHORT).show();
                }
            }
        });

        listViewPlayers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (view.getId()) {
                    case R.id.buttonIconPlayerListItemColor:
                        onItemSetColorClicked(position);
                        break;
                    case R.id.buttonIconPlayerListItemDelete:
                        onItemDeleteClicked(position);
                        break;
                    case R.id.buttonIconPlayerListItemName:
                        onItemEditNameClicked(position);
                        break;
                    default:
                        Toast.makeText(parent.getContext(), "ListView clicked" + id, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void addPlayer(int colorIndex) {
        final int[] colorList = getResources().getIntArray(R.array.player_colors);
        final String[] colorNameList = getResources().getStringArray(R.array.player_color_names);
        int color = colorList[colorIndex];
        String playerName = colorNameList[colorIndex];
        int multipleNum = 2;
        // the following while statement is to help avoiding duplicate names when adding players with the same color
        while(adapter.hasPlayerWithName(playerName) && multipleNum < 9)
        {
            if (multipleNum == 2) {
                // we haven't added a numerical suffix yet
                playerName = playerName + " " + String.valueOf(multipleNum);
            } else {
                // there is already a number suffix (or other tomfoolery)
                playerName = playerName.substring(0, playerName.length()-1) + String.valueOf(multipleNum);
            }
            multipleNum = multipleNum + 1;
        }
        adapter.add(new GamePlayer(playerName, color));
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

    public void onItemEditNameClicked(final int position) {
        showNameEditDialog(position);
    }

    public void showColorSelectionDialog(int playerPosition) {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new ColorSelectionDialogFragment();
        dialog.show(getFragmentManager(), "ColorSelectionDialog");
        currentPlayer = playerPosition;
    }

    public void showNameEditDialog(final int playerPosition) {
        // Create a dialog fragment with an editText and show it
        currentPlayer = playerPosition;
        final EditText input = new EditText(this);
        input.setText(dataItems.get(playerPosition).getName());
        input.setSelectAllOnFocus(true);
        input.setSingleLine();
        input.setMaxLines(1);
        new AlertDialog.Builder(MainActivity.this)
                .setTitle(R.string.enter_name)
                .setView(input)
                .setPositiveButton(R.string.button_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dataItems.get(currentPlayer).setName(input.getText().toString());
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton(R.string.button_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Do nothing.
                        dialog.dismiss();
                    }
                }).show();
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
        //intent.putExtra("Players", adapter.getCount());
        intent.putParcelableArrayListExtra("players", dataItems);
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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // save the list of players
        savedInstanceState.putParcelableArrayList("dataItems", dataItems);
        // save the current player being edited
        savedInstanceState.putInt("currentPlayer", currentPlayer);
        super.onSaveInstanceState(savedInstanceState);
    }
}
