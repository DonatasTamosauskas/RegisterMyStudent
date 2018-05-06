package com.jjanonis.donatas.registermystudent.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.jjanonis.donatas.registermystudent.R;

public class AbsenceReasonDialogFragment extends DialogFragment {

    public interface AbsenceDialogListener {
        void onDialogAddClick(DialogFragment dialog, String lecture, String reason);
        void onDialogCancelClick(DialogFragment dialog);

    }

    AbsenceDialogListener clickListener;

    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            clickListener = (AbsenceDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement AbsenceDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View dialogView = layoutInflater.inflate(R.layout.absence_reason_dialog, null);
        final EditText lectureEditText = dialogView.findViewById(R.id.lecture_text);
        final EditText reasonEditText = dialogView.findViewById(R.id.absence_reason);


        builder.setMessage("Add unattended lecture")
                .setView(dialogView)
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
//                        EditText lecture = (EditText) findViewById(R.id.lecture_text);
                        clickListener.onDialogAddClick(AbsenceReasonDialogFragment.this, lectureEditText.getText().toString(), reasonEditText.getText().toString());
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        clickListener.onDialogCancelClick(AbsenceReasonDialogFragment.this);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
