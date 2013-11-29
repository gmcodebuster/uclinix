package com.example.uclinix;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uclinix.AddPatient.AsynkTaskAddPatient;
import com.example.uclinix.EditAppointment.AddAppointCategoryAsync;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditPatient extends BaseFragment {
	EditText edt_user,edt_fname,edt_sname,edt_email,edt_add,edt_ll,edt_mno;
	TextView txt_dob,txt_gen;
	Button btn_dob,btn_gen,btnEdit,btnCancel;
	String da,mo,yy,st,s_token,str_gen;
	static LayoutInflater inflater;
	ArrayList<CategoryListData> cd_arraylist = new ArrayList<CategoryListData>();
	
	private AlertDialog catDialog;
	final String LOG_TAG = "Edit Patient : ";
	String pb_id = "",pb_username = "",pb_email = "",pb_add = "",pb_ll = "",pb_mo = "",pb_dob="",pgen="",pfname="",plname="";
	private String[] gender_type = {"Male","Female"};
	private AlertDialog genderdia;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		
		Bundle bundle = this.getArguments();
		 if(bundle != null){
			 pb_id = bundle.getString("pid");
			 pb_username = bundle.getString("pusernm");
			 pb_email = bundle.getString("pemail");
			 pb_add = bundle.getString("padd");
			 pb_ll = bundle.getString("pll");
			 pb_mo = bundle.getString("pmo");
			 pb_dob = bundle.getString("pdob");
			 pgen = bundle.getString("pgen");
			 pfname = bundle.getString("pfname");
			 plname = bundle.getString("plname");
		 }
		
		 
		 
		System.out.println("pb_id >> "+pb_id +" pb_username >> "+pb_username+" pb_email > "+pb_email +" pb_add > "+pb_add+ " pb_ll > "+pb_ll+
		 " pb_mo > "+pb_mo+" pb_dob > "+pb_dob+" pgen > "+pgen+" pfname > "+pfname+" plname > "+plname);
		
		
		
		View view = inflater.inflate(R.layout.frag_editpatient,container, false);
		 ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Edit Patient");
		 
		 
		 edt_user = (EditText) view.findViewById(R.id.edt_username);
		 edt_fname = (EditText) view.findViewById(R.id.edt_firstname);
		 edt_sname = (EditText) view.findViewById(R.id.edt_secondname);
		 edt_email = (EditText) view.findViewById(R.id.edt_email);
		 edt_add = (EditText) view.findViewById(R.id.edt_address);
		 edt_ll = (EditText) view.findViewById(R.id.edt_landline);
		 edt_mno = (EditText) view.findViewById(R.id.edt_mobileno);
		 
		 txt_dob = (TextView) view.findViewById(R.id.txt_dob);
		 txt_gen = (TextView) view.findViewById(R.id.txt_gender);
		 
		 btn_dob = (Button) view.findViewById(R.id.btn_dob);
		 btn_gen = (Button) view.findViewById(R.id.btn_gender);
		 
		 btn_dob.setOnClickListener(datedialog);
		 btn_gen.setOnClickListener(genderlistener);
		 
		 btnEdit = (Button) view.findViewById(R.id.btnEdit);
		 btnCancel = (Button) view.findViewById(R.id.btnCan);
		 
		 btnEdit.setOnClickListener(editbtnlistener);
		 btnCancel.setOnClickListener(null);
		 
		 
		 
		 
		 
		 
		 
		 
		 edt_user.setText(pb_username);
		 edt_email.setText(pb_email);
		 edt_add.setText(pb_add);
		 edt_ll.setText(pb_ll);
		 edt_mno.setText(pb_mo);
		 txt_dob.setText(pb_dob);
		 txt_gen.setText(pgen);
		 edt_fname.setText(pfname);
		 edt_sname.setText(plname);
		 return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		
		AlertDialog.Builder durationbuilder = new AlertDialog.Builder(mActivity);
		durationbuilder.setTitle("select gender");
		durationbuilder.setIcon(R.drawable.ic_launcher);				
		durationbuilder.setItems(gender_type, new DialogInterface.OnClickListener() {
			
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
//			Toast toast = Toast.makeText(mActivity, "Selected: "+gender_type[which], Toast.LENGTH_SHORT);
//			toast.show();
			txt_gen.setText(gender_type[which]);
			str_gen = gender_type[which];
			}
			});
		
		durationbuilder.setCancelable(true);
		genderdia = durationbuilder.create();
		
	}
	
	private void showDatePicker() {
		  DatePickerFragment date = new DatePickerFragment();
		  /**
		   * Set Up Current Date Into dialog
		   */
		  Calendar calender = Calendar.getInstance();
		  Bundle args = new Bundle();
		  
		  try{
			  SimpleDateFormat parseFormat = new SimpleDateFormat("dd-MM-yyyy");
				Date date1 = parseFormat.parse(pb_dob);
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
		   txt_dob.setText(strDate);
		  }
		 };
	
	private OnClickListener datedialog = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDatePicker();
		}
	}; 
	
	private OnClickListener genderlistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			genderdia.show();
			
		}
	};
	
		private OnClickListener editbtnlistener = new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (edt_user.getText().toString().equals("")
							|| edt_user.getText().toString().equals("null")) {
						
						edt_user.setError("Please enter username");
						return;
					}

										
					if (edt_fname.getText().toString().equals("")
							|| edt_fname.getText().toString().equals("null")) {
						edt_fname.requestFocus();
						edt_fname.setError("Please enter first name");
						return;
					}

					if (edt_sname.getText().toString().equals("")
							|| edt_sname.getText().toString().equals("null")) {
						edt_sname.requestFocus();
						edt_sname.setError("Please enter last name");
						return;
					}
					
					if (edt_email.getText().toString().equals("")
							|| edt_email.getText().toString().equals("null")) {
						edt_email.requestFocus();
						edt_email.setError("Please enter email");
						return;
					}

					if (edt_add.getText().toString().equals("")
							|| edt_add.getText().toString().equals("null")) {
						edt_add.requestFocus();
						edt_add.setError("Please enter address");
						return;
					}
					
					
					if (edt_ll.getText().toString().equals("")
							|| edt_ll.getText().toString().equals("null")) {
						edt_ll.requestFocus();
						edt_ll.setError("Please enter landline no");
						return;
					}

					if (edt_mno.getText().toString().equals("")
							|| edt_mno.getText().toString().equals("null")) {
						edt_mno.requestFocus();
						edt_mno.setError("Please enter mobile no");
						return;
					}
					
					
					if (txt_gen.getText().toString().equals("")
							|| txt_gen.getText().toString().equals("null")) {
						txt_gen.requestFocus();
						txt_gen.setError("Please select gender");
						return;
					}

					if (txt_dob.getText().toString().equals("")
							|| txt_dob.getText().toString().equals("null")) {
						txt_dob.requestFocus();
						txt_dob.setError("Please select birth date");
						return;
					}
					
							
					String str_uname = edt_user.getText().toString().trim();
					
					String str_Fname = edt_fname.getText().toString().trim();
					String str_Lname = edt_sname.getText().toString().trim();
					String str_Email = edt_email.getText().toString().trim();
					String str_Add = edt_add.getText().toString().trim();
					String str_Lno = edt_ll.getText().toString().trim();
					String str_mno = edt_mno.getText().toString().trim();
					String str_Bday = txt_dob.getText().toString().trim();
					String str_Gen = txt_gen.getText().toString().trim();
					
					System.out.println("str_uname >>"+str_uname);
					
					System.out.println("str_Fname >>"+str_Fname);
					System.out.println("str_Lname >>"+str_Lname);
					System.out.println("str_Email >>"+str_Email);
					System.out.println("str_Lno >>"+str_Lno);
					System.out.println("str_mno >>"+str_mno);
					System.out.println("str_Bday >>"+str_Bday);
					System.out.println("str_Gen >>"+str_Gen);
					
					//get file path
					String url_editPatient = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_editpatient);
					
					
					new AsynkTaskAddPatient().execute(url_editPatient,
														ApplicationActivity.getToken(),
														str_uname,
														str_Fname,
														str_Lname,
														str_Email,
														str_Add,
														str_Lno,
														str_mno,
														str_Bday,
														str_Gen,
														pb_id);
				/*	if (!cd.isConnectingToInternet()) {
						alert.showAlertDialog(getActivity(),
								"Internet Connection Error",
								"Please connect to working Internet connection",
								false);
					} else {
						try {
												
							new AsynkTaskAddPatient().execute(url_addPatient,ApplicationActivity.getToken(),str_uname,strPass,str_Fname,str_Lname,str_Email,str_Add,str_Lno,str_mno,str_Bday,str_Gen);
						} catch (Exception e) {
							// TODO: handle exception
							e.printStackTrace();
						}
					}
					
					*/
				}
			};
			
			
			class AsynkTaskAddPatient extends AsyncTask<String,Void,String>{
				ProgressDialog pdialog = new ProgressDialog(mActivity);
				String[] response = new String[2];
				@Override
				protected void onPreExecute() {
					// TODO Auto-generated method stub
					super.onPreExecute();
//					Toast.makeText(mActivity, "AsyncTask is called", Toast.LENGTH_LONG).show();
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
					
					/*
					public static String[] doPostProfUpload(String url, String token,
															String username, String f_name,
															String l_name, String email,
															String adrr,String l_line,
															String m_mo, String dob, String gender,
																String pass,
															String attachment)
															
					*/
					
					
//					new AsynkTaskAddPatient().execute(url_addPatient,ApplicationActivity.getToken(),str_uname,strPass,str_Fname,str_Lname,str_Email,str_Add,str_Lno,str_mno,str_Bday,str_Gen);
					
					System.out.println("Edit Patient "+ "URL >> "+params[0]);
					response = HttpApiCalling.EditPatient(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11]);
					System.out.println("patient_list details" + response[0] + "   "
							+ response[1]);

					if(response != null){
						if(response[0].equalsIgnoreCase("200")){
							try {
//								{
//							        "id": 14,
//							        "username": "patient10",
//							        "email": "rohan@fafadiatech.com",
//							        "address": "asd",
//							        "landline": "3210654987",
//							        "mobile": "9632014587",
//							        "dob": "1985-09-10",
//							        "gender": "Male",
//							        "image": "../images/18_2.jpg",
//							        "patient_name": "R S"
//							    }
								
								
							/*	{
								    "id": 25,
								    "username": "patient201",
								    "password": "test123",
								    "email": "rohan.tare201@fafadiatech.com",
								    "address": "asd",
								    "landline": "3215649870",
								    "mobile": "9632014587",
								    "dob": "1985-09-10",
								    "gender": "Male",
								    "image": null,
								    "first_name": "patient",
								    "last_name": "roh"
									}*/
								
								JSONObject objResult = new JSONObject(response[1]);
								System.out.println("Patient activity >> "+objResult.length());
								
								System.out.println("id >"+objResult.optString("id", "")+
										"username >"+objResult.optString("username", "")+
										"email >"+objResult.optString("email", ""));
								
								
								
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}//end of response is 200
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
					
//					Toast.makeText(mActivity, "AsyncTask is executed", Toast.LENGTH_LONG).show();

					if (pdialog.isShowing()) {
						pdialog.dismiss();
						
						if(response != null){
							if(response[0].equalsIgnoreCase("200")){
								//success
								System.out.println("User is created");
//								Toast.makeText(getActivity(), "Patient is created", Toast.LENGTH_LONG).show();
								((AppMainFragmentActivity) getActivity()).popFragments();								 
							}
							if(response[0].equalsIgnoreCase("400")){
//								System.out.println(LOG_TAG+" >> "+s_msg);
								
							}
						}	
					}
				}
			}
}
