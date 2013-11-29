package com.example.uclinix;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Example activity.
 */
public class SectionListActivity extends BaseFragment {

	ArrayList<String> dateList = new ArrayList<String>();
	ArrayList<String> categoryList = new ArrayList<String>();
	ArrayList<String> name = new ArrayList<String>();
	ArrayList<String> end_timeList = new ArrayList<String>();
	ArrayList<String> patient = new ArrayList<String>();
	ArrayList<String> time = new ArrayList<String>();
	ArrayList<String> patientphone = new ArrayList<String>();
	ArrayList<String> patientid = new ArrayList<String>();
	ArrayList<String> appointmentid = new ArrayList<String>();
	ArrayList<String> duration = new ArrayList<String>();
	ArrayList<String> fees = new ArrayList<String>();
	ArrayList<String> PaidList = new ArrayList<String>();
	Context ctx;
	SectionListItem[] exampleArray1;
	View empty;
	ProgressDialog pdialog;

	// SectionComposerAdapter adapter;
//	ActionBar actionBar;
	SharedPreferences spusername, sptoken, spsecret;

	String[] datearray, categoryArray, patientArray, timeArray, patientphoneArray,
			patientidArray, appointmentidArray,feesarray,end_timeArray,paidArray;

	String token, secret;

	private class StandardArrayAdapter extends ArrayAdapter<SectionListItem> {

		private final SectionListItem[] items;

		public StandardArrayAdapter(final Context context,
				final int textViewResourceId, final SectionListItem[] items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(final int position, final View convertView,
				final ViewGroup parent) {
			View view = convertView;
			if (view == null) {
				final LayoutInflater vi = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = vi.inflate(R.layout.item_composer, null);
			}
			final SectionListItem currentItem = items[position];

			if (currentItem != null) {
				final TextView textpatient = (TextView) view
						.findViewById(R.id.txtpatient);

				final TextView txtcategory = (TextView) view
						.findViewById(R.id.txtcategory);
				final TextView texttime = (TextView) view
						.findViewById(R.id.txttime);

				
				if (textpatient != null) {
					textpatient.setText(currentItem.item.toString());
				}
				if (txtcategory != null) {
					txtcategory.setText(currentItem.category);
				}

			
				if (texttime != null) {
					texttime.setText(currentItem.time );
				}
			}

			return view;
		}

	}

	SectionListItem[] exampleArray = { // Comment to prevent re-format
	/*
	 * new SectionListItem("Test 1 - A", "0", "A"), // new
	 * SectionListItem("Test 2 - A", "1", "A"), // new
	 * SectionListItem("Test 3 - A", "2", "A"), // new
	 * SectionListItem("Test 4 - A", "3", "A"), // new
	 * SectionListItem("Test 5 - A", "4", "A"), // new
	 * SectionListItem("Test 6 - B", "5", "B"), // new
	 * SectionListItem("Test 7 - B", "6", "B"), // new
	 * SectionListItem("Test 8 - B", "7", "B"), // new
	 * SectionListItem("Test 9 - Long", "8", "Long section"), // new
	 * SectionListItem("Test 10 - Long", "9", "Long section"), // new
	 * SectionListItem("Test 11 - Long", "10", "Long section"), // new
	 * SectionListItem("Test 12 - Long", "11", "Long section"), // new
	 * SectionListItem("Test 13 - Long", "12", "Long section"), // new
	 * SectionListItem("Test 14 - A again", "13", "A"), // new
	 * SectionListItem("Test 15 - A again", "14", "A"), // new
	 * SectionListItem("Test 16 - A again", "15", "A"), // new
	 * SectionListItem("Test 17 - B again", "16", "B"), // new
	 * SectionListItem("Test 18 - B again", "17", "B"), // new
	 * SectionListItem("Test 19 - B again", "18", "B"), // new
	 * SectionListItem("Test 20 - B again", "19", "B"), // new
	 * SectionListItem("Test 21 - B again", "20", "B"), // new
	 * SectionListItem("Test 22 - B again", "21", "B"), // new
	 * SectionListItem("Test 23 - C", "22", "C"), // new
	 * SectionListItem("Test 24 - C", "23", "C"), // new
	 * SectionListItem("Test 25 - C", "24", "C"), // new
	 * SectionListItem("Test 26 - C", "25", "C"), //
	 */

	};

	private StandardArrayAdapter arrayAdapter;

	private SectionListAdapter sectionAdapter;

	private SectionListView listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.appointments_layout, container, false);		
		listView = (SectionListView) view.findViewById(R.id.section_list_view);

		
		empty = (ViewStub)view.findViewById(R.id.patientemptylistview);
		return view;
	}
	
	@Override
	public void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ctx = mActivity;

		spusername = PreferenceManager.getDefaultSharedPreferences(mActivity);
		sptoken = PreferenceManager
				.getDefaultSharedPreferences(mActivity);
		spsecret = PreferenceManager
				.getDefaultSharedPreferences(mActivity);

//		setActionBar();

		

	}


	class ProfileAsync extends AsyncTask {
		// Dialog dg;
		String code;
		String status;
		String response_value;
		String[] response = new String[2];
	

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			categoryList.clear();
			time.clear();
			dateList.clear();
			name.clear();
			patientphone.clear();
			patientid.clear();
			appointmentid.clear();
			duration.clear();
			
			
			 pdialog = ProgressDialog.show(ctx,"" ,"");
			 pdialog.setContentView(R.layout.wheel);
		


			 pdialog.setCancelable(true);
			 pdialog.setCancelable(true);
			 pdialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialogs) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});

		}

		protected Object doInBackground(Object... params) {

			try {

				token = sptoken.getString("token", null);
				secret = spsecret.getString("secret", null);

				response = HttpApiCalling.appointmentList(token, secret);
				System.out.println("responsevalue" + response[1]);
				if (response[1] != null) {

					JSONObject objResult = new JSONObject(response[1]);

					JSONObject objResponse = objResult
							.getJSONObject("response");
					status = objResponse.getString("status");

					if (status.equalsIgnoreCase("1")) {
						JSONArray objarray = objResponse
								.getJSONArray("appointments");
						System.out.println("objarray" + objarray.length());

						

						for (int i = 0; i < objarray.length(); i++) {
							JSONObject menuObject = objarray.getJSONObject(i);
						

							dateList.add(menuObject.getString("date"));
							categoryList.add(menuObject.getString("category"));
							name.add(menuObject.getString("patient"));
							time.add((menuObject.getString("start_time")).substring(10));
							patientphone.add(menuObject
									.getString("patient_phone"));
							patientid.add(menuObject.getString("patient_id"));
							appointmentid.add(menuObject
									.getString("appointment_id"));
							end_timeList.add((menuObject.getString("end_time")).substring(10));
							
							fees.add(menuObject.getString("fees"));
							PaidList.add(menuObject.getString("paid"));

						}

					}
				}
			} catch (Exception e) {
				// TODO: handle exception
			}

			return null;
		}

		@Override
		protected void onPostExecute(Object result) {
			// TODO Auto-generated method stub*
			super.onPostExecute(result);
			if (pdialog.isShowing()) {

				pdialog.dismiss();
			}
			if(status != null)
			{
				if(status.equalsIgnoreCase("1"))
				{
				
					
				end_timeArray =	end_timeList.toArray(new String[end_timeList.size()]);
				datearray = dateList.toArray(new String[dateList.size()]);
				categoryArray = categoryList.toArray(new String[categoryList.size()]);
				patientArray = name.toArray(new String[name.size()]);
				timeArray = time.toArray(new String[time.size()]);
				patientphoneArray = patientphone.toArray(new String[patientphone
						.size()]);
				patientidArray = patientid.toArray(new String[patientid.size()]);
				appointmentidArray = appointmentid.toArray(new String[appointmentid
						.size()]);
				paidArray = PaidList.toArray(new String[PaidList
				                              						.size()]);
				
				
				feesarray = fees.toArray(new String[fees.size()]);

				exampleArray1 = new SectionListItem[patientArray.length];

				System.out.println("patientArray" + patientArray.length);
				for (int i = 0; i < patientArray.length; i++) {

					exampleArray1[i] = new SectionListItem(patientArray[i],
							categoryArray[i], timeArray[i],
							datearray[i]);
				}

				arrayAdapter = new StandardArrayAdapter(mActivity,
						R.id.txtpatient, exampleArray1);
				sectionAdapter = new SectionListAdapter(mActivity.getLayoutInflater(),
						arrayAdapter);

				listView.setAdapter(sectionAdapter);
				listView.setEmptyView(empty);

				sectionAdapter.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {

						// System.out.println("Inside SectionListActivity position >> "+_index);
						Intent i = new Intent(ctx, AppTabAFirstFragment.class);
						Bundle bundle = new Bundle();
						bundle.putString("patientname", patientArray[position]);
						
						bundle.putString("category", categoryArray[position]);
						bundle.putString("date", datearray[position]);
						bundle.putString("time", timeArray[position]);
						bundle.putString("phone", patientphoneArray[position]);
						bundle.putString("patientid", patientidArray[position]);
						bundle.putString("appointmentid",
								appointmentidArray[position]);
						bundle.putString("fees",
								feesarray[position]);
						bundle.putString("end_time",
								end_timeArray[position]);
						bundle.putString("category",
								categoryArray[position]);
						bundle.putString("paid",
								paidArray[position]);

						i.putExtras(bundle);
						startActivity(i);
					}
				});

				listView.setOnItemClickListener(sectionAdapter);

			}else if (status.equalsIgnoreCase("0")) {
				
				Toast.makeText(ctx, "Your Session have been Expired.", 1000).show();
				
				Intent i = new Intent(ctx,LoginActivity.class);
				startActivity(i);
				
			}
			}

			
		}

	}// end of Async class

	public void runforme() {
		new ProfileAsync().execute((String)null);

	}

	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		runforme();
//		if(Functions.haveNetworkConnection(ctx))
//		{
//			runforme();
//		}else {
//			Toast.makeText(ctx, "No internet Connection", 1000).show();
//		}
		
	};
	
//	 @Override
//	  public void onBackPressed() {
////	    this.getParent().onBackPressed();   
//	  }

}