package com.baziiz.gametimer2;

import android.app.ActionBar;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;

import com.rey.material.widget.FloatingActionButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import android.widget.Chronometer;


public class GamePlayActivity extends ActionBarActivity
    implements View.OnClickListener{

    RelativeLayout gameScreen;
    ArrayList<GamePlayer> players;
    FloatingActionButton buttonFloatPause;
    ArrayList<Chronometer> playerButtons;
    long[] times;
    int activeButton;
    boolean paused;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

        playerButtons = new ArrayList<Chronometer>();

        TableRow tableRow = new TableRow(this);
        players = getIntent().getExtras().getParcelableArrayList("players");

        times = new long[players.size()];
        Arrays.fill(times, 0l);

        paused = true;
        activeButton = -1;

        gameScreen = (RelativeLayout)this.findViewById(R.id.relLayoutGameScreen);
        buttonFloatPause = (FloatingActionButton)this.findViewById(R.id.buttonFloatPause);
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
            btnPlayer.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            btnPlayer.setText(player.getName() + System.getProperty("line.separator") + "00:00");
            btnPlayer.setFormat(player.getName() + System.getProperty("line.separator") + "%s");
            btnPlayer.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
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
        buttonFloatPause.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (paused) {
                    if (activeButton != -1) {
                        // unpause and go back to activating previously pressed button
                        buttonFloatPause.setIcon(getResources().getDrawable(R.drawable.ic_action_pause), true);
                        // this will set paused to false
                        activatePlayerView(activeButton);
                    }
                } else {
                    // pause the game
                    paused = true;
                    // stop currently active timer
                    stopCurrentPlayer();
                    buttonFloatPause.setIcon(getResources().getDrawable(R.drawable.ic_action_play_arrow), true);
                }
            }
        });
        Log.i("TIMER", String.valueOf(layoutButtons.getMeasuredHeight()));
    }

    // when a player view is clicked
    @Override
    public void onClick(View v) {
        activatePlayerView(v.getId());
    }

    private void activatePlayerView(int btnId) {
        // first reset the currently active button to proper unselected state
        Log.i("TIMER", "Activating " + String.valueOf(btnId));
        // ignore re-clicks of the same button
        if ((activeButton == btnId) && !paused) return;
        if (!paused) {
            stopCurrentPlayer();
        }
        // now set the black border on the button that was just pressed
        activeButton = btnId;
        paused = false;
        Chronometer btn = (Chronometer)findViewById(btnId);
        GradientDrawable gd = new GradientDrawable();
        gd.setColor(players.get(btnId).getColor());
        gd.setStroke(30, 0xFF000000);
        if (ColorUtility.getBrightness(players.get(btnId).getColor()) < 128) {
            gd.setStroke(30, 0xFFFFFFFF);
        }
        // TODO : Fix the API issue or use a different effect
        btn.setBackground((Drawable) gd);
        btn.setBase(SystemClock.elapsedRealtime() - times[btnId]);
        btn.start();
        btn.invalidate();
    }

    private void stopCurrentPlayer() {
        if (activeButton != -1) {
            Log.i("TIMER", "Stopping " + activeButton);
            Chronometer bt = (Chronometer)findViewById(activeButton);
            bt.setBackgroundColor(players.get(bt.getId()).getColor());
            bt.stop();
            times[activeButton] = SystemClock.elapsedRealtime() - bt.getBase();
            bt.invalidate();
        }
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
