package com.example.sin.projectone;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by nanth on 12/10/2016.
 */

public class MessageAlertDialog extends DialogFragment {
    private String message = "";
    private String textBtnOk = "OK";
    private String textBtnCancel = "Cancel";
    private String titel ="Message alert";
    private boolean hasOkCancelButton = true;
    private boolean hasOkButton = false;

    public static MessageAlertDialog newInstance(Bundle bundle){
        MessageAlertDialog dialog = new MessageAlertDialog();
        dialog.setArguments(bundle);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle b = getArguments();
        if(b!=null){
            message = b.getString(Constant.KEY_BUNDLE_MESSAGE_DIALOG, message);
            titel = b.getString(Constant.KEY_BUNDLE_TITLE_DIALOG, titel);
            hasOkCancelButton = b.getBoolean(Constant.KEY_BYNDLE_HAS_OK_CANCEL_DIALOG, true);
            hasOkButton = b.getBoolean(Constant.KEY_BYNDLE_HAS_OK_DIALOG, false);
        }
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(titel);
        if(hasOkCancelButton && !hasOkButton){
            builder.setMessage(message)
                    .setPositiveButton(textBtnOk, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Fragment target = getTargetFragment();
                            if(target!=null){
                                target.onActivityResult(Constant.REQUEST_CODE_OK_CANCEL, Constant.RESULT_CODE_OK, new Intent());
                            }
                        }
                    })
                    .setNegativeButton(textBtnCancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Fragment target = getTargetFragment();
                            if(target!=null){
                                target.onActivityResult(Constant.REQUEST_CODE_OK_CANCEL, Constant.RESULT_CODE_CANCEL, new Intent());
                            }
                        }
                    });
        }
        else if(hasOkButton){
            builder.setMessage(message)
                    .setPositiveButton(textBtnOk, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Fragment target = getTargetFragment();
                            if(target!=null){
                                target.onActivityResult(Constant.REQUEST_CODE_OK_CANCEL, Constant.RESULT_CODE_OK, new Intent());
                            }
                        }
                    });
        }
        else{
            builder.setMessage(message);
        }

        // Create the AlertDialog object and return it
        return builder.create();
    }


}
