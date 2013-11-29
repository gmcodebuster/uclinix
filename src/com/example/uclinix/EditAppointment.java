package com.example.uclinix;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.AlertDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.ProgressDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class EditAppointment extends BaseFragment {

	Button btnPatient,btnCategory,btnAppdate,btnAppTime,btnAppDur,btnEdit,btnCancel;
	private AlertDialog myDialog;
	private AlertDialog catDialog;
	private AlertDialog durationdia;
	
	private String[] duration = {"5","10","15","20","30","35","40","45","50","55","60"};
	final String LOG_TAG = "Edit Appointment : ";
	
	ArrayList<PatientData> pd_arraylist = new ArrayList<PatientData>();
	ArrayList<CategoryListData> cd_arraylist = new ArrayList<CategoryListData>();
	String s_token;
	static LayoutInflater inflater;
	
	CustomPatientAdapter_AddAppointment cpadapter;
	CustomCategoryAdapter_AddAppointment ccadapter;
	
	private int year;
	private int month;
	private int day;

	static final int DATE_DIALOG_ID = 100;
	String pid,cid,st,et,pa,paon,did,str_time;
	
	String da,mo,yy,hh,mm,ss;
	String str_dur;
	String f_p_id,f_c_id;
	
	TextView txtPname,txtCname,txtStdate,txtstTime,txtEndtime;
	String e_id = "",e_pid = "",e_cid = "",e_sdate = "",e_stime = "",e_dur = "";
	
	HashMap<String,String> cat_hash = new HashMap<String,String>();
	HashMap<String,String> pat_hash = new HashMap<String,String>();
	
	AlertDialog alert;
	
	DisplayImageOptions options;
	ImageLoader imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		         
//		setHasOptionsMenu(true);
		//call patient aPI
		
		
		// call category API
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.ic_menu_search)
//		.showImageForEmptyUri(R.drawable.ic_menu_search)
//		.showImageOnFail(R.drawable.ic_menu_search)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		new AddAppointPatientListAsync().execute();
		
		AlertDialog.Builder patientbuilder = new AlertDialog.Builder(mActivity);
		patientbuilder.setTitle("select patient name");
		patientbuilder.setIcon(R.drawable.ic_launcher);				

		
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
		categorybuilder.setIcon(R.drawable.ic_launcher);		
		categorybuilder.setAdapter(new CustomCategoryAdapter_AddAppointment(mActivity), new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
//				Toast toast = Toast.makeText(mActivity, "Selected: "+cd_arraylist.get(which).category, Toast.LENGTH_SHORT);
//				toast.show();
				cid = cd_arraylist.get(which).id;
				txtCname.setText(cd_arraylist.get(which).category);
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
			txtEndtime.setText(duration[which] +"Minutes");
			str_dur = duration[which];
			}
			});
		
		durationbuilder.setCancelable(true);
		durationdia = durationbuilder.create();

	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		Bundle bundle = this.getArguments();
		 if(bundle != null){
				e_id = bundle.getString("id");
				e_pid = bundle.getString("pid");
				e_cid = bundle.getString("cid");
				e_sdate = bundle.getString("sdate");
				e_stime = bundle.getString("stime");
				e_dur = bundle.getString("dur");
		 }
		
		System.out.println("e_id >> "+e_id +" e_pid >> "+e_pid+" e cid > "+e_cid +" e sdate > "+e_sdate+ " e_stime > "+e_stime+" e_dur > "+e_dur);
		
		
		View view = inflater.inflate(R.layout.frag_editappointment,container, false);
		 ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Edit Appointment");
		 
//		 	btnPatient = (Button) view.findViewById(R.id.btn_patient);
			btnCategory = (Button) view.findViewById(R.id.btn_category);
			btnAppdate = (Button) view.findViewById(R.id.btn_startdate);
			btnAppTime = (Button) view.findViewById(R.id.btn_starttime);
			btnAppDur = (Button) view.findViewById(R.id.btn_durtime); 
			btnEdit = (Button) view.findViewById(R.id.btn_ok);
			btnCancel = (Button) view.findViewById(R.id.btn_cancel);
			
			
			
			txtPname = (TextView) view.findViewById(R.id.txt_patient_name);
			txtCname = (TextView) view.findViewById(R.id.txt_category_name);
			txtStdate = (TextView) view.findViewById(R.id.txt_startdate);
			txtstTime = (TextView) view.findViewById(R.id.txt_starttime);
			txtEndtime = (TextView) view.findViewById(R.id.txt_endtime);
			
			
			/** Setting a button click listener for the choose button */
//			btnPatient.setOnClickListener(patientlistener);
			btnCategory.setOnClickListener(categorylistener);
			btnAppdate.setOnClickListener(datedialog);
			btnAppTime.setOnClickListener(timedialog);
			btnAppDur.setOnClickListener(durationdialog);
			btnEdit.setOnClickListener(btnoklistener);
			btnCancel.setOnClickListener(btncalcellistener);
			
			
			txtEndtime.setText(e_dur + " Minutes");
			txtPname.setText(e_pid);
			txtCname.setText(e_cid);
			txtStdate.setText(e_sdate);
			txtstTime.setText(e_stime);
			
		return view;
	}
	
	
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
		  try{
		  SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date1 = parseFormat.parse(e_sdate);
			calender.setTime(date1);
			
			args.putInt("year", calender.get(Calendar.YEAR));
			args.putInt("month", calender.get(Calendar.MONTH));
			args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
			
		  }catch(Exception e){
			  args.putInt("year", calender.get(Calendar.YEAR));
			  args.putInt("month", calender.get(Calendar.MONTH));
			  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
			  
		  }
		  
		  
		  
		  
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
//		   Toast.makeText(
//		   mActivity,
//		     String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
//		       + "-" + String.valueOf(dayOfMonth),
//		     Toast.LENGTH_LONG).show();
		   st = strDate;
		   txtStdate.setText(strDate);
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
	
	
	 try{
		 SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
			Date date1 = parseFormat.parse(e_stime);
			calender.setTime(date1);
			
			args.putInt("Hour", calender.get(Calendar.HOUR_OF_DAY));
			args.putInt("Minu", calender.get(Calendar.MINUTE));
			
			
		  }catch(Exception e){
			  args.putInt("Hour", calender.get(Calendar.HOUR_OF_DAY));
				args.putInt("Minu", calender.get(Calendar.MINUTE));
			  
		  }
	
	time.setArguments(args);
	
	 /**
	   * Set Call back to capture selected date
	   */
	  time.setCallBack(ontime);
	  time.show(getFragmentManager(), "Time Picker");//getChildFragmentManager()
}
		
	OnTimeSetListener ontime = new OnTimeSetListener() {
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// TODO Auto-generated method stub
		
//		hh = String.valueOf(hourOfDay);
//		mm = String.valueOf(minute);
		hh = "" + hourOfDay;
		mm = "" +minute;
		
		
		String strTime = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
//		Toast.makeText(mActivity, String.valueOf(hourOfDay) + ":" + String.valueOf(minute), Toast.LENGTH_LONG).show();
		
//		String strTime = (hourOfDay<10 ? "0" + hh : hh) + ":" + (minute < 10 ? "0" + mm : mm) + ":00";
		
//		txtstTime.setText(""+hh+":"+mm);		
		
//		String diffTime = (hours<10 ? "0" + hours : hours) + ":" + (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds) + " h";
		str_time = strTime;
		
		
		
		
		
		// Used to convert 24hr format to 12hr format with AM/PM values
//	    private void updateTime(int hours, int mins) {
	         
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
	 
	         txtstTime.setText(aTime);
	    
		
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
		
		
		if(str_time == null){
			str_time = txtstTime.getText().toString().trim();
			try{
			SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
			SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
			Date date = parseFormat.parse(str_time);//"10:30 PM"
			
			str_time = displayFormat.format(date);
			
			System.out.println("Start time >> "+parseFormat.format(date) + " = " + displayFormat.format(date));
			}catch(Exception e){
				
			}
		}
//		st = date + time
//		et  = date +time+ duration
//		pa = edtpaid	
		
		
		
		if(st == null){
			try{
//			st = txtStdate.getText().toString().trim();	
			SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy");
			Date date = parseFormat.parse(e_sdate);//"10:30 PM" st
			st = displayFormat.format(date);
			
			System.out.println("start date >> "+parseFormat.format(date) + " = " + displayFormat.format(date));
			}catch(Exception e){
				
			}
		}
		
		
//		et = st+" "+hh+":"+mm+
		
		if(str_dur == null){
			
			str_dur = e_dur;
		}
		
		//check patient id is not null
		
		
		
		//check category id is not null
		if(cid == null){
			
			cid = f_c_id;
		}
		
		try {
			
			st = st+" "+str_time+":00";
			
			System.out.println("start datetime >> "+st);
			SimpleDateFormat  format = new SimpleDateFormat("yyyy-MM-dd HH:mm",Locale.getDefault());
			Date da = format.parse(st);
			Calendar calendar1 = Calendar.getInstance();
			calendar1.setTime(da);
			calendar1.add(Calendar.MINUTE, Integer.parseInt(str_dur));
			System.out.println(calendar1.getTime());  // Output : Wed Jul 25 14:00:00 IST 2012
			
			 String e_year = String.valueOf(calendar1.get(Calendar.YEAR));
			 String e_month = String.valueOf((calendar1.get(Calendar.MONTH))+1);
			 String e_day = String.valueOf(calendar1.get(Calendar.DAY_OF_MONTH));
			 String e_hour = String.valueOf((calendar1.get(Calendar.HOUR_OF_DAY)<10)?"0"+calendar1.get(Calendar.HOUR_OF_DAY):calendar1.get(Calendar.HOUR_OF_DAY));
			 String e_min = String.valueOf((calendar1.get(Calendar.MINUTE)<10)?"0"+calendar1.get(Calendar.MINUTE):calendar1.get(Calendar.MINUTE));
			
			
				
			 
			 System.out.println("e_year >> "+e_year+" e_month >> "+e_month+" e_day >> "+e_day+" e_hour >> "+e_hour+" e_min >> "+e_min );
			 et = e_year+"-"+e_month+"-"+e_day+" "+e_hour+":"+e_min+":00";
			 did = ApplicationActivity.getDoctorId();
			 
			 System.out.println("patient id "+ pid+"  new patient id "+ f_p_id+" category id "+cid+" start date "+st+" end date "+et+" doctor id "+did+" appointment id " +e_id);
			 
			 
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		new EditAppointmentAsync().execute(f_p_id,cid,st,et,did,e_id);
	}
};



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
							
							pat_hash.put(jobj.optString("id",""), jobj.optString("patient_name",""));
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
			
			if(response[1] != null){
				if(response[0].equalsIgnoreCase("200")){
					
					for(Object o1:pat_hash.keySet())
					{
						 if (pat_hash.get(o1).equals(e_pid))//selectedCountry = tidofcountry
						 {
							 f_p_id = ""+o1;
							 System.out.println("Patient Key = "+o1);		//prints key by passing value as parameter
//							 childlist.add((String) o1);				// make an arraylist of all culture belong to selected country
														
						 }
							 
					}
					
//					cpadapter = new CustomPatientAdapter_AddAppointment(mActivity);
					new AddAppointCategoryAsyncHashmap().execute();
					
					
				}
				if(response[0].equalsIgnoreCase("400")){
//					System.out.println(LOG_TAG+" >> "+s_msg);
					
				}
			}	
	
		}
	}
	

	
	//fetch category list and show dialog box
	

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

//								[
//							    {
//							        "id": 1,
//							        "category": "Allergy"
//							    },
//							    {
//							        "id": 2,
//							        "category": "General Checkup"
//							    }
//							]
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
							
//							catDialog.show();
						}
						if(response[0].equalsIgnoreCase("400")){
//							System.out.println(LOG_TAG+" >> "+s_msg);
							
						}
					}	
			
				}
			}
		
		
		//fetch category list for hashmap
		

		class AddAppointCategoryAsyncHashmap extends AsyncTask<String, Void, String>{
				ProgressDialog pdialog = new ProgressDialog(mActivity);
				String[] response = new String[2];
				
				
				
				public AddAppointCategoryAsyncHashmap(){
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

//								[
//							    {
//							        "id": 1,
//							        "category": "Allergy"
//							    },
//							    {
//							        "id": 2,
//							        "category": "General Checkup"
//							    }
//							]
								JSONArray objResult = new JSONArray(response[1]);
								System.out.println("AddAppointment category activity >> "+objResult.length());
								for(int i=0;i< objResult.length();i++){
									
									JSONObject jobj = objResult.getJSONObject(i);
																
									cd_arraylist.add(new CategoryListData(jobj.optString("id",""),
																	 jobj.optString("category", "")
																	 
											));
									
									cat_hash.put(jobj.optString("id",""), jobj.optString("category", ""));
									
									
									
									
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
							
							
							for(Object o1:cat_hash.keySet())
							{
								 if (cat_hash.get(o1).equals(e_cid))//selectedCountry = tidofcountry
								 {
									 System.out.println("category Key = "+o1);		//prints key by passing value as parameter
//									 childlist.add((String) o1);				// make an arraylist of all culture belong to selected country
									 f_c_id = ""+o1;
								 }
									 
							}							
						
						}
						if(response[0].equalsIgnoreCase("400")){
//							System.out.println(LOG_TAG+" >> "+s_msg);
							
						}
					}	
					
					new AddAppointCategoryAsync().execute();
			
				}
			}
		
		//AsyncTask for Edit Appointment
		
		
		public class EditAppointmentAsync extends AsyncTask<String, Void, String>{
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
//				str_paid = params[4];
//				str_paidon = params[5];
				doctor_id = params[4];
				String a_id = params[5];
				
				String url_editAppointment = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_editappointment);
				System.out.println(LOG_TAG + "Add appo URL >> "+url_editAppointment);
//				response = HttpApiCalling.AddAppointment(url_addAppointment,ApplicationActivity.getToken(), patient_id,category_id,start_time,end_time,str_paid,str_paidon,doctor_id);
				response = HttpApiCalling.EditAppointment(url_editAppointment,ApplicationActivity.getToken(), patient_id,doctor_id,category_id,start_time,end_time,a_id);
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
				
				if(response[1] != null){
					if(response[0].equalsIgnoreCase("200")){
						
						
						if(response[1].contains("status")){
							alertMessage("You can't edit appointment.Please check your date and time.");
							
						}else{
	/*					 FragmentManager fm = getFragmentManager();
						 fm.popBackStack();*/
//						Toast.makeText(getActivity(), "Appointment edited successfully", Toast.LENGTH_LONG).show();
						alertMessage("Appointment edited successfully");
						((AppMainFragmentActivity) getActivity()).popFragments();
						}
					}
					if(response[0].equalsIgnoreCase("400")){
//						System.out.println(LOG_TAG+" >> "+s_msg);
						alertMessage("Error in editing,please try again.");
//						Toast.makeText(getActivity(), "Error in editing,please try again.", Toast.LENGTH_LONG).show();
					}
					if(response[0].equalsIgnoreCase("500")){
						alertMessage("Error in editing,please try again.");
//						Toast.makeText(getActivity(), "Please check inputs.", Toast.LENGTH_LONG).show();
					}
					
				}	
				
				pid= null;
				cid=null;
				st=null;
				et=null;
				pa=null;
				paon=null;
				did=null;
				str_time=null;
			}
		}

		//custom adapter for category
		
		 private class CustomCategoryAdapter_AddAppointment extends BaseAdapter {
				
			 
//			 ArrayList<HashMap<String, Object>> strArrList;
			 Context ctx;
		
				public CustomCategoryAdapter_AddAppointment(Context context) {	
					// let android do the initializing :)
					ctx = context;
					inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					strArrList = hashStr;				
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
							
					
					
//					holder.txtSmart.setText(searchResults.get(position)
//							.get("type_of_loyalty_cardArrl").toString());
					

					return convertView;
				}
		
			}//End
	 

		 
		//custom adapter for patient
			
		 private class CustomPatientAdapter_AddAppointment extends BaseAdapter {
				
			 
//			 ArrayList<HashMap<String, Object>> strArrList;
			 Context ctx;
		
				public CustomPatientAdapter_AddAppointment(Context context) {	
					// let android do the initializing :)
					ctx = context;
					inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					strArrList = hashStr;				
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
							
//					holder.txtSmart.setText(searchResults.get(position)
//							.get("type_of_loyalty_cardArrl").toString());
					
					imageLoader.displayImage("http://www.uclinix.in/images/" + pd.getPatientImage(), holder.imgPhoto, options, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
							if(pd.getPatientGender().toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookmale);
							}else{
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookfemale);
							}
						}
									
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							if(pd.getPatientGender().toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(0);
							}else{
								holder.imgPhoto.setBackgroundResource(0);
							}
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							if(pd.getPatientGender().toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookmale);
							}else{
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookfemale);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							if(pd.getPatientGender().toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookmale);
							}else{
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookfemale);
							}
						}
					});//getResources().getString(R.string.url_base)

					return convertView;
				}
		
			}//End
	 
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
		 
}


/*
 * http 200
 
if date and time is old
{
    "status": "You can't add appointment.Please check your date and time."
}*/