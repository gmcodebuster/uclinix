package com.example.uclinix;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class DatePickerFragment extends DialogFragment {

	OnDateSetListener ondateSet;
	public DatePickerFragment() {
	 }

	 public void setCallBack(OnDateSetListener ondate) {
	  ondateSet = ondate;
	 }

	 private int year, month, day;

	 @Override
	public void setArguments(Bundle args) {
		// TODO Auto-generated method stub
		super.setArguments(args);
		 year = args.getInt("year");
		  month = args.getInt("month");
		  day = args.getInt("day");
	}
	 
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return new DatePickerDialog(getActivity(), ondateSet, year, month, day);
	} 
}
