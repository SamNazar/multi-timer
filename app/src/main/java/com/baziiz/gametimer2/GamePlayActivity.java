package com.baziiz.gametimer2;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.rey.material.widget.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import android.widget.Chronometer;


public class GamePlayActivity extends ActionBarActivity
    implements View.OnClickListener{

    RelativeLayout gameScreen;
    ArrayList<GamePlayer> players;
    FloatingActionButton buttonFloatPause;
    ArrayList<Chronometer> playerButtons;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        playerButtons = new ArrayList<Chronometer>();

        TableRow tableRow = new TableRow(this);
        players = getIntent().getExtras().getParcelableArrayList("players");
        gameScreen = (RelativeLayout)this.findViewById(R.id.relLayoutGameScreen);
        buttonFloatPause = (FloatingActionButton)this.findViewById(R.id.buttonFloatPause);
//        txtTest = (TextView) this.findViewById(R.id.txtTest);
//        txtTest.setText(players.size() + " player game");
        TableLayout layoutButtons = new TableLayout(this);
        layoutButtons.setStretchAllColumns(true);
        layoutButtons.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
        Log.i("TIMER", String.valueOf(layoutButtons.getMeasuredHeight()));

        // create a button for each player in the game
        for (GamePlayer player: players) {
            if ((players.size() <= 3) || (players.indexOf(player) % 2 == 0)) {
                tableRow = new TableRow(this);
                tableRow.setGravity(Gravity.CENTER);
                tableRow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f));
            }
            Chronometer btnPlayer = new Chronometer(this);
            btnPlayer.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
            btnPlayer.setText(player.getName());
            if (ColorUtility.getBrightness(player.getColor()) > 127) {
                btnPlayer.setTextColor(Color.BLACK);
            } else {
                btnPlayer.setTextColor(Color.WHITE);
            }
            btnPlayer.setBackgroundColor(player.getColor());
            btnPlayer.setId(players.indexOf(player));
            btnPlayer.setOnClickListener(this);
            playerButtons.add(btnPlayer);
            tableRow.addView(btnPlayer);
            if ((players.size() <= 3) || (players.indexOf(player) % 2 == 1) || (players.indexOf(player) == players.size()-1)) {
                layoutButtons.addView(tableRow);
            }
        }
        gameScreen.addView(layoutButtons);
        buttonFloatPause.bringToFront();
        Log.i("TIMER", String.valueOf(layoutButtons.getMeasuredHeight()));
    }

    @Override
    public void onClick(View v) {
        Log.i("TIMER", String.valueOf(v.getId()));
        activatePlayerView(v.getId());
    }

    private void activatePlayerView(int btnId) {
        // first reset all backgrounds to their proper unselected state
        for (Chronometer bt: playerButtons) {
            bt.setBackgroundColor(players.get(bt.getId()).getColor());
            bt.invalidate();
            bt.stop();
        }
        // now set the black border on the button that was just pressed
        Chronometer btn = (Chronometer)findViewById(btnId);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(players.get(btnId).getColor());
        gd.setStroke(30, 0xFF000000);
        // TODO : Fix the API issue or use a different effect
        btn.setBackground((Drawable) gd);
        btn.start();
        btn.invalidate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game_play, menu);
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
