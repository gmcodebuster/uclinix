package com.example.dialog;

import javax.crypto.Mac;

import com.example.uclinix.AddAppointment;
import com.example.uclinix.R;

import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class MyDialogFragment extends DialogFragment {
	
	public static MyDialogFragment newInstance() {
		
		String title = "My Fragment";
		
        MyDialogFragment f = new MyDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        f.setArguments(args);
        return f;
    }

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		String title = getArguments().getString("title");
		Dialog myDialog = new AlertDialog.Builder(getActivity())
					.setIcon(R.drawable.ic_launcher)
					.setTitle(title)
					
					.setPositiveButton("OK", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									
								}
							})
					.setNegativeButton("Cancel", 
							new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
//									((AndroidDialogFragmentActivity)getActivity()).cancelClicked();
								}
							})
					.create();

		return myDialog;
	}
	
}
