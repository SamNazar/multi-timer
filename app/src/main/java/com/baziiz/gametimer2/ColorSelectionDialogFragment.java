package com.baziiz.gametimer2;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Toast;


/**
 * Created by samn on 5/12/15.
 */
public class ColorSelectionDialogFragment extends DialogFragment {

    GridView colorListGrid;
    ColorDialogListener mListener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // set up gridView with colors to pick from
        colorListGrid = new GridView(getActivity());
        colorListGrid.setNumColumns(3);
        final ColorSelectAdapter colorAdapter = new ColorSelectAdapter(getActivity());
        colorListGrid.setAdapter(colorAdapter);
        colorListGrid.setOnItemClickListener(new GridView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // when a color in the list of colors is selected
                //Toast.makeText(getActivity(), "COLOR SELECTED", Toast.LENGTH_SHORT).show();
                ColorDialogListener activity = (ColorDialogListener) getActivity();
                activity.onColorSelected(i);
                getDialog().dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.select_color)
                .setView(colorListGrid)
                .setIcon(R.drawable.ic_action_format_color_fill_bk)
                .setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        Toast.makeText(getActivity(), "COLOR SELECTED was", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if the user hits cancel, do nothing and return to the main screen
                    }
                });
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        // verify that the host activity implements the interface to pick color
        try {
            // Instantiate the ColorDialogListener so we can send events to the host
            mListener = (ColorDialogListener) activity;
        } catch (ClassCastException e) {
            // The parent activity doesn't implement our interface, throw an exception
            throw new ClassCastException(activity.toString() + " must implement ColorDialogListener");
        }
    }

    public interface ColorDialogListener {
        public void onColorSelected(int colorIndex);
    }
}
