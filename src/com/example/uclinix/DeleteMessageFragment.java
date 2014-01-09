package com.example.uclinix;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DeleteMessageFragment extends BaseFragment {
	
	String strMsg; 
	public DeleteMessageFragment(){}
	public DeleteMessageFragment(String msg)
	{
		strMsg = msg;
		
		if(strMsg.length() == 0){
			strMsg = " ";
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_deletemessage,container, false);		
		TextView delmsg = (TextView) view.findViewById(R.id.txt_deletemessage);
		delmsg.setText(strMsg);
		
		return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return super.onBackPressed();
	}
}
