package com.example.uclinix;

import java.util.ArrayList;
import java.util.List;


import com.example.uclinix.AppMainFragmentActivity.AppConstants;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppSettingsFragment extends BaseFragment {
	private String[] setting_list = {"Change password","Logout"};
	ArrayList<String> lst_setting;
	
	ListView lst_settings;
	AlertDialog alert;
	
	static LayoutInflater inflater;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	
//		CustomSettingAdapter adpt1 = new CustomSettingAdapter(mActivity);
//		lst_settings.setAdapter(adpt1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_settings, container, false);
		lst_setting = new ArrayList<String>(); 
		lst_setting.add("Change password");
		lst_setting.add("Logout");
		
		
		lst_settings = (ListView) view.findViewById(R.id.list_tab2);
		// Set title bar to actionbar
		CustomSettingAdapter adapterSet = new CustomSettingAdapter(mActivity);
		
		if(adapterSet != null){
//			Toast.makeText(getActivity(), "adapter is  not empty",Toast.LENGTH_LONG).show();
			lst_settings.setAdapter(adapterSet);
		}else{
			
//			Toast.makeText(getActivity(), "adapter is empty",Toast.LENGTH_LONG).show();
		}
		
	    ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Settings");
		
	    lst_settings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				
				switch(position){
				
				case 0:
						
					mActivity.pushFragments(AppConstants.TAB_C, new ChangePasswordFragment(), true, true);
					break;
					
				case 1:
					ApplicationActivity.setLogin(false);
					((AppMainFragmentActivity) getActivity()).finish();
					
					break;
				
				
				}
				
			}
	    	
		});
	    
	    
	    
		return view;
	}
	
	private void alertMessage(String arg1){ 
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(arg1)
               .setCancelable(false)
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                        //do things
                	  alert.dismiss();
                   }
               });
        alert = builder.create();
        alert.show();
       }
 

	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return super.onBackPressed();
	}
	
//custom adapter for list
	 private class CustomSettingAdapter extends BaseAdapter {
			
		 
//		 ArrayList<HashMap<String, Object>> strArrList;
		 Context ctx;
	
			public CustomSettingAdapter(Context context) {	
				// let android do the initializing :)
				ctx = context;
				inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				strArrList = hashStr;		
				
			}
			
			// @Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				return lst_setting.size();
			}

			// @Override
			public Object getItem(int position) {
				// TODO Auto-generated method stub
				return position;
			}

			// @Override
			public long getItemId(int position) {
				// TODO Auto-generated method stub
				return position;
			}
							
			// class for caching the views in a row
			private class ViewHolder1 {
				public TextView txtcategory;				
								
			}

			ViewHolder1 holder;

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.row_settings, null);
					
					holder = new ViewHolder1();
					
					
					holder.txtcategory = (TextView) convertView
							.findViewById(R.id.pda_settingname);
		
				
					convertView.setTag(holder);

				} else
					holder = (ViewHolder1) convertView.getTag();

				holder.txtcategory.setText(lst_setting.get(position));				
						
				
		
				

				return convertView;
			}
	
		}//End
	
	
}
