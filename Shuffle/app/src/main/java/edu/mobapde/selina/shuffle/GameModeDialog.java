package edu.mobapde.selina.shuffle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by ryana on 3/11/2016.
 */
public class GameModeDialog extends DialogFragment {
    public static final int TIME_ATTACK = 5;
    public static final int SONG_RUSH = 4;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("Select Game Mode")
                .setItems(new String[]{
                        "Time Attack",
                        "Song Rush"
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((SelectPlaylistActivity)getActivity()).startGame(which);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                });
        return builder.create();
    }
}
