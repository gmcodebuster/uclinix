package com.example.uclinix;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class TimePickerFragment extends DialogFragment{

	OnTimeSetListener ontimeset;
	
	public TimePickerFragment(){
		
	}
	
	void setCallBack(OnTimeSetListener ontime) {
		// TODO Auto-generated method stub
		ontimeset = ontime;
	}
	
	private int hour, minu;
	
	@Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
	
		 hour = args.getInt("Hour");
		  minu = args.getInt("Minu");
		  
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new TimePickerDialog(getActivity(), ontimeset, hour, minu, false);
	}
}
