package com.example.uclinix;

import com.example.uclinix.AppMainFragmentActivity.AppConstants;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class AppTabAFirstFragment extends BaseFragment {
	Button mGotoButton;
	ListView lvAppointment;
		
	final String LOG_TAG = "Appointment_List : ";
	static LayoutInflater inflater;
	String s_token;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.app_tab_a_first_screen, container,false);
		
		// Set title bar to actionbar
	    ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Settings");
	    
		mGotoButton = (Button) view.findViewById(R.id.id_next_tab_a_button);
		lvAppointment = (ListView) view.findViewById(R.id.listViewAppointment);
		
		
		mGotoButton.setOnClickListener(listener);
		return view;
	}
	
	private OnClickListener listener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			mActivity.pushFragments(AppConstants.TAB_A, new AppTabASecondFragment(), true, true);
			
		}
	};
	
	
	 private static class CustomAdapter extends BaseAdapter {
			
		 
//		 ArrayList<HashMap<String, Object>> strArrList;
		 Context ctx;
	
			public CustomAdapter(Context context) {	
				// let android do the initializing :)
				ctx = context;
				inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				strArrList = hashStr;				
			}
			
			// @Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				return 10;
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
			private class ViewHolder {
				public TextView txtName;
				public TextView txtEmail;
				public ImageView imgPhoto;
				public TextView textMobile;					
			}

			ViewHolder holder;

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {

				if (convertView == null) {
					convertView = inflater.inflate(R.layout.row_patient, null);
					
					holder = new ViewHolder();
					holder.txtName = (TextView) convertView
							.findViewById(R.id.pa_name);

					holder.txtEmail = (TextView) convertView
							.findViewById(R.id.pa_email);
					holder.textMobile = (TextView) convertView
							.findViewById(R.id.pa_mobile);
		
					holder.imgPhoto = (ImageView) convertView
							.findViewById(R.id.pa_img);

					convertView.setTag(holder);

				} else
					holder = (ViewHolder) convertView.getTag();

				holder.txtName.setText("Gaurav");
						
				holder.txtEmail.setText("gaurav.mehta@gmail.com");
				holder.textMobile.setText("1234567889");
				
//				holder.txtSmart.setText(searchResults.get(position)
//						.get("type_of_loyalty_cardArrl").toString());
				

				return convertView;
			}
	
		}//End
	
	 
	
}
