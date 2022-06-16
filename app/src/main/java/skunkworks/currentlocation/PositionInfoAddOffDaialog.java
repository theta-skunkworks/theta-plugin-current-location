package skunkworks.currentlocation;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.widget.ImageView;

public class PositionInfoAddOffDaialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        ImageView imageView = new ImageView(getContext());
        imageView.setImageResource( R.drawable.daialog_position );

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("- Notice -")
                .setView(  imageView )
                .setMessage("Please close the plug-in and turn on the \"position information addition function\" in the menu UI before using it.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //NOP
                    }
                });
        return builder.create();
    }

}
