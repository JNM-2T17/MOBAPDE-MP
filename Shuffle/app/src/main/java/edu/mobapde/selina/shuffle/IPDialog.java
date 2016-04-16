package edu.mobapde.selina.shuffle;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by ryana on 4/16/2016.
 */
public class IPDialog extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.ip_dialog,null);
        final EditText ipField = (EditText)v.findViewById(R.id.ipField);
        ipField.setText(MainActivity.ip);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Server IP")
                    .setView(v)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.ip = ipField.getText().toString();
                            dismiss();
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
