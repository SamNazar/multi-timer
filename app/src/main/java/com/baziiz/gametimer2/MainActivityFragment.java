package com.baziiz.gametimer2;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.gc.materialdesign.views.ButtonFloat;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    ButtonFloat buttonFloatAddPlayer;
    ListView listViewPlayers;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_main, container, false);
        buttonFloatAddPlayer = (ButtonFloat) v.findViewById(R.id.buttonFloatAddPlayer);
        listViewPlayers = (ListView) v.findViewById(R.id.listViewPlayers);

        String[] cars = {"Porsche Cayman", "Chevy Camaro", "Mazda MX-5"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(listViewPlayers.getContext(), android.R.layout.simple_list_item_1, cars);
        listViewPlayers.setAdapter(adapter);

        return v;
    }
}
