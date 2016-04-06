package com.li.xroads.fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import com.li.xroads.R;
/**
 * Created by nagnath_p on 3/8/2016.
 */
public class InternetCheckDialogFragment extends DialogFragment {

    InternetCheckDialogListener internetCheckDialogListener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.not_connected_to_internet)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        internetCheckDialogListener.onDialogOkClick(InternetCheckDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            internetCheckDialogListener = (InternetCheckDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement InternetCheckDialogListener");
        }
    }


    public interface InternetCheckDialogListener {
        public void onDialogOkClick(DialogFragment dialog);
    }


}
