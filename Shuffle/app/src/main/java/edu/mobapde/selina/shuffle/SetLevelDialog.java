package edu.mobapde.selina.shuffle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

/**
 * Created by ryana on 2/25/2016.
 */
public class SetLevelDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        /*AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Set Level")
                .setItems(SettingsActivity.LEVELS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((SettingsActivity)getActivity()).setLevel(which);
                        dismiss();
                    }
                });
        return dialogBuilder.create();*/

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity())
                .setTitle("Set Level")
                .setItems(SettingsActivity.LEVELS, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((MainActivity)getActivity()).setLevel(which);
                        dismiss();
                    }
                });
        return dialogBuilder.create();

    }
}
