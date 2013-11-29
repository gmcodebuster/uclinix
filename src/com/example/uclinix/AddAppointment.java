package com.example.uclinix;
//http://kirill-poletaev.blogspot.in/2013/01/android-beginner-tutorial-part-41.html
//"APA91bESLFiBLQpBd8pG6HyQqVZseYJawpfuVhdWPXClogW_vrdfbg2CaqU5ssalqa8ADTnSe1B6k-9AOXkD5iXle-bZ_2qA_y8gGZNXWiofxjUSkb_DIkjxaP7nR16zi07wwTLZrKKWLAtqbYNQgy6QYlL_ZVsvNQ"
//AUTHKEY = "key=AIzaSyBwOO6m5Bb5By3W-PM7Qk02Uo_ThtQAK8k"
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.dialog.AlertDialogRadio;
import com.example.dialog.AlertDialogRadio.AlertPositiveListener;
import com.example.dialog.Android;
import com.example.dialog.MyDialogFragment;
import com.example.uclinix.AppMainFragmentActivity.AppConstants;

import com.example.uclinix.AppTabBFirstFragment.PatientListAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebChromeClient.CustomViewCallback;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddAppointment extends BaseFragment {
	
	static LayoutInflater inflater;
	Button btnPatient,btnCategory,btnAppdate,btnAppTime,btnAppDur,btnOk,btnCancel;
	int position = 0;
	String[] list = {"One","Two","Three","Four"};
	String title = "Patient List";
	private AlertDialog myDialog;
	private AlertDialog catDialog;
	private AlertDialog durationdia;
	private String[] items = {"Ravi Pal","Ravi P.","Ravi"};
	private String[] category = {"Allergy","Fever"};
	private String[] duration = {"5","10","15","20","30","35","40","45","50","55","60"};
	final String LOG_TAG = "Add Appointment : ";
	
	ArrayList<PatientData> pd_arraylist = new ArrayList<PatientData>();
	ArrayList<CategoryListData> cd_arraylist = new ArrayList<CategoryListData>();
	String s_token;
	
	
	CustomPatientAdapter_AddAppointment cpadapter;
	CustomCategoryAdapter_AddAppointment ccadapter;
	
	private int year;
	private int month;
	private int day;

	static final int DATE_DIALOG_ID = 100;
	String pid,cid,st,et,pa,paon,did,str_time;
	
	String da,mo,yy,hh,mm,ss;
	String str_dur;
	
	DisplayImageOptions options;
	ImageLoader imageLoader;
	AlertDialog alert;
	
//	getActivity().supportInvalidateOptionsMenu().
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.frag_addappointment,container, false);
		 ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Add Appointment");
		btnPatient = (Button) view.findViewById(R.id.btn_patient);
		btnCategory = (Button) view.findViewById(R.id.btn_category);
		btnAppdate = (Button) view.findViewById(R.id.btn_startdate);
		btnAppTime = (Button) view.findViewById(R.id.btn_starttime);
		btnAppDur = (Button) view.findViewById(R.id.btn_durtime); 
		btnOk = (Button) view.findViewById(R.id.btn_ok);
		btnCancel = (Button) view.findViewById(R.id.btn_cancel);
		
		/** Getting the reference of the button from the main layout */
	   

	    /** Setting a button click listener for the choose button */
		btnPatient.setOnClickListener(patientlistener);
		btnCategory.setOnClickListener(categorylistener);
		btnAppdate.setOnClickListener(datedialog);
		btnAppTime.setOnClickListener(timedialog);
		btnAppDur.setOnClickListener(durationdialog);
		btnOk.setOnClickListener(btnoklistener);
		btnCancel.setOnClickListener(btncalcellistener);
		
		return view;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setHasOptionsMenu(true);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.ic_menu_search)
		.showImageForEmptyUri(R.drawable.ic_launcher)
		.showImageOnFail(R.drawable.ic_launcher)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		new AddAppointPatientListAsync().execute();
//		new AddAppointCategoryAsync().execute();
		AlertDialog.Builder patientbuilder = new AlertDialog.Builder(mActivity);
		patientbuilder.setTitle("select patient name");
		patientbuilder.setIcon(R.drawable.icon);				
		
		
		patientbuilder.setAdapter(new CustomPatientAdapter_AddAppointment(mActivity), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast toast = Toast.makeText(mActivity, "Selected: "+pd_arraylist.get(which).patient_name, Toast.LENGTH_SHORT);
//				toast.show();
				pid = pd_arraylist.get(which).id;
				btnPatient.setText(pd_arraylist.get(which).patient_name);
			}
		});
		patientbuilder.setCancelable(true);
		myDialog = patientbuilder.create();
		
		AlertDialog.Builder categorybuilder = new AlertDialog.Builder(mActivity);
		categorybuilder.setTitle("select categoty");
		categorybuilder.setIcon(R.drawable.icon);
		CustomCategoryAdapter_AddAppointment cca = new CustomCategoryAdapter_AddAppointment(mActivity);
		cca.notifyDataSetChanged();
		categorybuilder.setAdapter( cca , new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast toast = Toast.makeText(mActivity, "Selected: "+cd_arraylist.get(which).category, Toast.LENGTH_SHORT);
//				toast.show();
				cid = cd_arraylist.get(which).id;
				btnCategory.setText(cd_arraylist.get(which).category);
			}
		});
		categorybuilder.setCancelable(true);
		catDialog = categorybuilder.create();
		
		AlertDialog.Builder durationbuilder = new AlertDialog.Builder(mActivity);
		durationbuilder.setTitle("select duration");
		durationbuilder.setIcon(R.drawable.ic_launcher);				
		durationbuilder.setItems(duration, new DialogInterface.OnClickListener() {
			
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//			Toast toast = Toast.makeText(mActivity, "Selected: "+duration[which], Toast.LENGTH_SHORT);
//			toast.show();
			btnAppDur.setText(duration[which] +"Minutes");
			str_dur = duration[which];
			}
			});
		
		durationbuilder.setCancelable(true);
		durationdia = durationbuilder.create();
		
	}//End of on create
		
		
	private OnClickListener patientlistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			myDialog.show();
		}
	};
	
	private OnClickListener categorylistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
//			OpenDialog();
			try{
//				new AddAppointCategoryAsync().execute();
				catDialog.show();
			}catch(Exception e){
				
			}
		}
	};
	
	private void showDatePicker() {
		  DatePickerFragment date = new DatePickerFragment();
		  /**
		   * Set Up Current Date Into dialog
		   */
		  Calendar calender = Calendar.getInstance();
		  Bundle args = new Bundle();
		  
		  
		  args.putInt("year", calender.get(Calendar.YEAR));
		  args.putInt("month", calender.get(Calendar.MONTH));
		  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
		  date.setArguments(args);
		  /**
		   * Set Call back to capture selected date
		   */
		  date.setCallBack(ondate);
		  date.show(getFragmentManager(), "Date Picker");//getChildFragmentManager()
		 }
	
	OnDateSetListener ondate = new OnDateSetListener() {
		  @Override
		  public void onDateSet(DatePicker view, int year, int monthOfYear,
		    int dayOfMonth) {
			  //2013-9-17 19:0:30
			  
			  
			  da = String.valueOf(dayOfMonth);
			  mo = String.valueOf(monthOfYear+1);
			  yy = String.valueOf(year);
			  
			  
			  String strDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
				       + "-" + String.valueOf(dayOfMonth);
		  /* Toast.makeText(
		   mActivity,
		     String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
		       + "-" + String.valueOf(dayOfMonth),
		     Toast.LENGTH_LONG).show();*/
		   st = strDate;
		   btnAppdate.setText(strDate);
		  }
		 };
	
	private OnClickListener datedialog = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showDatePicker();
	}
}; 
	private OnClickListener durationdialog = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			durationdia.show();
		}
	};


	private void showTimePicker(){
	
	TimePickerFragment time = new TimePickerFragment();
	
	Calendar calender = Calendar.getInstance();
	Bundle args = new Bundle();
	args.putInt("Hour", calender.get(Calendar.HOUR));
	args.putInt("Minu", calender.get(Calendar.MINUTE));
	time.setArguments(args);
	
	 /**
	   * Set Call back to capture selected date
	   */
	  time.setCallBack(ontime);
	  time.show(getFragmentManager(), "Time Picker");
}
		
	OnTimeSetListener ontime = new OnTimeSetListener() {
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		Calendar calender = Calendar.getInstance();
			
		
		hh = String.valueOf(hourOfDay);
		mm = String.valueOf(minute);
		System.out.println("hh >> "+hh +" mm >> "+mm);
		
		
		String strTime = String.valueOf(( (hourOfDay<10) ?"0"+hourOfDay:hourOfDay)) + ":" + String.valueOf((minute<10) ?"0"+minute:minute)+":00";
		
		System.out.println("final time >>"+ strTime);
//		Toast.makeText(mActivity, String.valueOf(hourOfDay) + ":" + String.valueOf(minute), Toast.LENGTH_LONG).show();
		
		String timeSet = "";
        if (hourOfDay > 12) {
        	hourOfDay -= 12;
            timeSet = "PM";
        } else if (hourOfDay == 0) {
        	hourOfDay += 12;
            timeSet = "AM";
        } else if (hourOfDay == 12)
            timeSet = "PM";
        else
            timeSet = "AM";
 
         
        String minutes = "";
        
        if (minute < 10)
            minutes = "0" + minute;
        else
            minutes = String.valueOf(minute);
 
        // Append in a StringBuilder
         String aTime = new StringBuilder().append(hourOfDay).append(':')
                .append(minutes).append(" ").append(timeSet).toString();
 
       
		
		btnAppTime.setText("Appointment time: "+aTime);
		str_time = strTime;
		
	}
};

	private OnClickListener timedialog = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		showTimePicker();
	}
};

private OnClickListener btncalcellistener = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		((AppMainFragmentActivity) getActivity()).popFragments();
	}
};

private OnClickListener btnoklistener = new OnClickListener() {
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		//validate all data
			
//		st = date + time
//		et  = date +time+ duration
//		pa = edtpaid	
		st = st+" "+str_time;
//		et = st+" "+hh+":"+mm+
		
		
		try {
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
			Date da = format.parse(st);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(da);
			calendar1.add(Calendar.MINUTE, Integer.parseInt(str_dur));
			System.out.println(calendar1.getTime());  // Output : Wed Jul 25 14:00:00 IST 2012
			
			String e_year = String.valueOf(calendar1.get(Calendar.YEAR));
			 String e_month = String.valueOf((calendar1.get(Calendar.MONTH))+1);
			 String e_day = String.valueOf(calendar1.get(Calendar.DAY_OF_MONTH));
			 String e_hour = String.valueOf(calendar1.get(Calendar.HOUR_OF_DAY));
			 String e_min = String.valueOf(calendar1.get(Calendar.MINUTE));
			 
			 
			 System.out.println("e_year >> "+e_year+" e_month >> "+e_month+" e_day >> "+e_day+" e_hour >> "+e_hour+" e_min >> "+e_min );
			 et = e_year+"-"+e_month+"-"+e_day+" "+e_hour+":"+e_min+":00";
			 did = ApplicationActivity.getDoctorId();
			 
			 System.out.println("patient id "+ pid+" category id "+cid+" start date "+st+"end date "+et+" doctor id "+did);
			 
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		new SaveAppointment().execute(pid,cid,st,et,did);
	}
};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub
		//super.onCreateOptionsMenu(menu, inflater);
	
//		inflater.inflate(R.menu.addappointment_menu, menu);
	
	}
	
	void OpenDialog(){
    	MyDialogFragment myDialogFragment = MyDialogFragment.newInstance();
    	myDialogFragment.show(getFragmentManager(), "myDialogFragment");
    }
	
	
//custom adapter for patient
	
	 private class CustomPatientAdapter_AddAppointment extends BaseAdapter {
			
		 
//		 ArrayList<HashMap<String, Object>> strArrList;
		 Context ctx;
	
			public CustomPatientAdapter_AddAppointment(Context context) {	
				// let android do the initializing :)
				ctx = context;
				inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				strArrList = hashStr;				
			}
			
			// @Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				return pd_arraylist.size();
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
				public ImageView imgPhoto;							
			}

			ViewHolder holder;

			@Override
			public View getView(final int position, View convertView,
					ViewGroup parent) {
				final PatientData pd = pd_arraylist.get(position);
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.row_data_addpatient, null);
					
					holder = new ViewHolder();
					
					
					holder.txtName = (TextView) convertView
							.findViewById(R.id.pda_patientname);
		
					holder.imgPhoto = (ImageView) convertView
							.findViewById(R.id.pda_pda_patientimage);

					convertView.setTag(holder);

				} else
					holder = (ViewHolder) convertView.getTag();

				holder.txtName.setText(pd.getPatientName());
					
//				holder.txtSmart.setText(searchResults.get(position)
//						.get("type_of_loyalty_cardArrl").toString());
				
				imageLoader.displayImage("http://www.uclinix.in/images/" + pd.getPatientImage(), holder.imgPhoto, options);//getResources().getString(R.string.url_base)
				
				

				return convertView;
			}
	
		}//End
 


//custom adapter for category
	
	 private class CustomCategoryAdapter_AddAppointment extends BaseAdapter {
			
		 
//		 ArrayList<HashMap<String, Object>> strArrList;
		 Context ctx;
	
			public CustomCategoryAdapter_AddAppointment(Context context) {	
				// let android do the initializing :)
				ctx = context;
				inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//				strArrList = hashStr;				
			}
			
			// @Override
			public int getCount() {
				// TODO Auto-generated method stub
				
				return cd_arraylist.size();
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
				final CategoryListData cd = cd_arraylist.get(position);
				if (convertView == null) {
					convertView = inflater.inflate(R.layout.row_category, null);
					
					holder = new ViewHolder1();
					
					
					holder.txtcategory = (TextView) convertView
							.findViewById(R.id.pda_patientname1);
		
				
					convertView.setTag(holder);

				} else
					holder = (ViewHolder1) convertView.getTag();

				holder.txtcategory.setText(cd.getCategoryName());				
						
				
				
//				holder.txtSmart.setText(searchResults.get(position)
//						.get("type_of_loyalty_cardArrl").toString());
				

				return convertView;
			}
	
		}//End
 
	
//fetch patient name and images
	
	class AddAppointPatientListAsync extends AsyncTask<String, Void, String>{
		ProgressDialog pdialog = new ProgressDialog(mActivity);
		String[] response = new String[2];
		
		
		
		public AddAppointPatientListAsync(){
			s_token = ApplicationActivity.getToken();
			
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = ProgressDialog.show(mActivity, "", "");
			pdialog.setContentView(R.layout.wheel);

			pdialog.setCancelable(true);
						
			pdialog.setOnCancelListener(new OnCancelListener() {

				
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
						
			String url_patientlist = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_patient_list);
			System.out.println(LOG_TAG + "URL >> "+url_patientlist);
			response = HttpApiCalling.getPatentList(url_patientlist,ApplicationActivity.getToken());
			System.out.println("patient_list details" + response[0] + "   "
					+ response[1]);

			if(response[1] != null){
				if(response[0].equalsIgnoreCase("200")){
					try {
//						{
//					        "id": 14,
//					        "username": "patient10",
//					        "email": "rohan@fafadiatech.com",
//					        "address": "asd",
//					        "landline": "3210654987",
//					        "mobile": "9632014587",
//					        "dob": "1985-09-10",
//					        "gender": "Male",
//					        "image": "../images/18_2.jpg",
//					        "patient_name": "R S"
//					    }
						JSONArray objResult = new JSONArray(response[1]);
						System.out.println("Patient activity >> "+objResult.length());
						for(int i=0;i<objResult.length();i++){
							
							JSONObject jobj = objResult.getJSONObject(i);
														
							pd_arraylist.add(new PatientData(jobj.optString("id",""),
															 jobj.optString("image", ""),
															 jobj.optString("patient_name",""),
															 jobj.optString("patient_name","")
									));
							
						}
						
						
						
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//response is 200
		if(response[0].equalsIgnoreCase("400")){		
		
			try {
				JSONObject objResult = new JSONObject(response[1]);
				
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//response is 400
		}//response is null

			return null;
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			if (pdialog.isShowing()) {
				pdialog.dismiss();

			}
			
			if(response != null){
				if(response[0].equalsIgnoreCase("200")){
					
					cpadapter = new CustomPatientAdapter_AddAppointment(mActivity);
				}
				if(response[0].equalsIgnoreCase("400")){
//					System.out.println(LOG_TAG+" >> "+s_msg);
					
				}
				
				new AddAppointCategoryAsync().execute();
			}	
	
		}
	}
	
//fetch category list
	

	class AddAppointCategoryAsync extends AsyncTask<String, Void, String>{
			ProgressDialog pdialog = new ProgressDialog(mActivity);
			String[] response = new String[2];
			
			
			
			public AddAppointCategoryAsync(){
				s_token = ApplicationActivity.getToken();
				
			}
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pdialog = ProgressDialog.show(mActivity, "", "");
				pdialog.setContentView(R.layout.wheel);

				pdialog.setCancelable(true);
							
				pdialog.setOnCancelListener(new OnCancelListener() {

					
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
						cancel(true);
					}
				});
			}
			
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
							
				String url_categorylist = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_category_list);
				System.out.println(LOG_TAG + "URL >> "+url_categorylist);
				response = HttpApiCalling.getPatentList(url_categorylist,ApplicationActivity.getToken());
				System.out.println("category_list details" + response[0] + "   "
						+ response[1]);

				if(response != null){
					if(response[0].equalsIgnoreCase("200")){
						try {

//							[
//						    {
//						        "id": 1,
//						        "category": "Allergy"
//						    },
//						    {
//						        "id": 2,
//						        "category": "General Checkup"
//						    }
//						]
							JSONArray objResult = new JSONArray(response[1]);
							System.out.println("AddAppointment category activity >> "+objResult.length());
							for(int i=0;i< objResult.length();i++){
								
								JSONObject jobj = objResult.getJSONObject(i);
															
							cd_arraylist.add(new CategoryListData(jobj.optString("id",""),
																 jobj.optString("category", "")
																 
										));
								
							}
							
							
							
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//response is 200
			if(response[0].equalsIgnoreCase("400")){		
			
				try {
					JSONObject objResult = new JSONObject(response[1]);
					
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//response is 400
			}//response is null

				return null;
			}
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
			
				if (pdialog.isShowing()) {
					pdialog.dismiss();

				}
				
				if(response != null){
					if(response[0].equalsIgnoreCase("200")){
						
						ccadapter = new CustomCategoryAdapter_AddAppointment(mActivity);
//						catDialog.show();
					}
					if(response[0].equalsIgnoreCase("400")){
//						System.out.println(LOG_TAG+" >> "+s_msg);
						
					}
				}	
		
			}
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
	
	//AsyncTask for getAppointment
	
	
	public class SaveAppointment extends AsyncTask<String, Void, String>{
		ProgressDialog pdialog = new ProgressDialog(mActivity);
		String[] response = new String[2];
		String patient_id,category_id,start_time,end_time,str_paid,str_paidon,doctor_id;
		String status_msg;
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = ProgressDialog.show(mActivity, "", "");
			pdialog.setContentView(R.layout.wheel);

			pdialog.setCancelable(true);
						
			pdialog.setOnCancelListener(new OnCancelListener() {

				
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					cancel(true);
				}
			});
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			patient_id = params[0];
			category_id = params[1];
			start_time = params[2];
			end_time = params[3];
//			str_paid = params[4];
//			str_paidon = params[5];
			doctor_id = params[4];
			
			String url_addAppointment = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_addappointment);
			System.out.println(LOG_TAG + "Add appo URL >> "+url_addAppointment);
//			response = HttpApiCalling.AddAppointment(url_addAppointment,ApplicationActivity.getToken(), patient_id,category_id,start_time,end_time,str_paid,str_paidon,doctor_id);
			response = HttpApiCalling.AddAppointment(url_addAppointment,ApplicationActivity.getToken(), patient_id,category_id,start_time,end_time,doctor_id);
			System.out.println("category_list details" + response[0] + "   "
					+ response[1]);
			
			if(response[1] != null){
				
				if(response[0] == "200"){	
				if(response[1].contains("status")){
					try{
					JSONObject stmsg = new JSONObject(response[1]);
					
					status_msg = stmsg.getString("status"); 
					
					
				}catch(Exception e){
					status_msg = " ";
				}
				}
			}	
			}
			
			return null;
		
		}
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (pdialog.isShowing()) {
				pdialog.dismiss();

			}
			
			if(response != null){
				if(response[0].equalsIgnoreCase("200")){
					
/*					 FragmentManager fm = getFragmentManager();
					 fm.popBackStack();*/
//					((AppMainFragmentActivity) getActivity()).popFragments();
					
					
					
					
					if(response[1].contains("status")){
						alertMessage("You can't Add appointment.Please check your date and time.");
						
					}else{
/*					 FragmentManager fm = getFragmentManager();
					 fm.popBackStack();*/
//					Toast.makeText(getActivity(), "Appointment edited successfully", Toast.LENGTH_LONG).show();
					alertMessage("Appointment Added successfully");
					((AppMainFragmentActivity) getActivity()).popFragments();
					}
				}
				if(response[0].equalsIgnoreCase("400")){
//					System.out.println(LOG_TAG+" >> "+s_msg);
					alertMessage("Error in adding, please try again.");					
				}
				
				if(response[0].equalsIgnoreCase("500")){
					alertMessage("Error in adding, please try again.");
//					Toast.makeText(getActivity(), "Please check inputs.", Toast.LENGTH_LONG).show();
				}
			}	
		}
	}
}

/*
{
    "id": 58,
    "patient": 14,
    "category": 1,
    "start_time": "2013-10-23T17:00:30",
    "end_time": "2013-10-23T19:00:30",
    "doctor": 13
}


//add patient
 *http://192.168.2.28:8000/api2/patient_add/
 *password extra added
 * {
    "id": 23,
    "username": "patient20",
    "password": "test123",
    "email": "ravi.pal@fafadiatech.com",
    "address": "asd",
    "landline": "3210654987",
    "mobile": "9632014587",
    "dob": "1985-09-10",
    "gender": "Male",
    "image": null,
    "first_name": "R",
    "last_name": "S"
}

{
    "status": "You can't add appointment.Please check your date and time."
}

 * 
 */