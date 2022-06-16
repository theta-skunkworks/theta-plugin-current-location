package skunkworks.currentlocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class OfflineWarningDaialog extends DialogFragment {

    public interface OfflineWarningDaialogListener {
        public void onDialogNegativeClick(DialogFragment dialog);
    }

    OfflineWarningDaialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            listener = (OfflineWarningDaialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(this.toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("- Notice -")
                .setMessage("Offline allows you to view a map of the range and zoom level you have seen online in the past.\n\nIf you do not see the map, please use it online.")
                .setPositiveButton("OK",null )
                .setNegativeButton("Never display again", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onDialogNegativeClick(OfflineWarningDaialog.this);
                    }
                });
        return builder.create();
    }


}

