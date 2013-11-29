package com.example.uclinix;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
//wallmart effect
public class AddPatient extends BaseFragment{

	/*
	{
	    "id": 24,
	    "username": "patient1",
	    "password": "test123",
	    "email": "rohan.tare1@fafadiatech.com",
	    "address": "asd",
	    "landline": "3215649870",
	    "mobile": "9632014587",
	    "dob": "1985-09-10",
	    "gender": "Male",
	    "image": "images/Penguins.jpg",
	    "first_name": "patient",
	    "last_name": "roh"	    
	}
	
	
	{
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
	}
	*/
	
	static LayoutInflater inflater;
	 ImageView viewImage;
	 Button selectPhoto,btnOk,btnCan;
	 EditText edtUname,edtPass,edtFname,edtLname,edtEmail,edtAddr,edtLno,edtMno;
	 Button btnBdate,btnGender;
	 private String[] gender_type = {"Male","Female"};
	 private static int RESULT_LOAD_IMAGE = 1;
	 String da,mo,yy,hh,mm,ss,str_gen,path,file_name;
	 private AlertDialog genderdia;
	 AlertDialogManager alert = new AlertDialogManager();
	 ConnectionDetector cd;
	 
	 
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.addpatient, container, false);
		((AppMainFragmentActivity) getActivity()).setActionBarTitle("Add Patient");
		 
		edtUname = (EditText) view.findViewById(R.id.edt_username);
		edtPass = (EditText) view.findViewById(R.id.edt_password);
		edtFname = (EditText) view.findViewById(R.id.edt_firstname); 
		edtLname = (EditText) view.findViewById(R.id.edt_secondname); 
		edtEmail = (EditText) view.findViewById(R.id.edt_email);
		edtAddr = (EditText) view.findViewById(R.id.edt_address);
		edtLno = (EditText) view.findViewById(R.id.edt_landline);
		edtMno = (EditText) view.findViewById(R.id.edt_mobileno);
		
		
		
		
		viewImage = (ImageView) view.findViewById(R.id.viewImage);
		selectPhoto = (Button) view.findViewById(R.id.btnSelectPhoto);
		
		btnBdate = (Button) view.findViewById(R.id.edt_dob);
		btnGender = (Button) view.findViewById(R.id.edt_gender);
		btnGender.setOnClickListener(genderdialog);
		selectPhoto.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				selectImage();
			}
		});
		btnBdate.setOnClickListener(datedialog);
		btnGender.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				genderdia.show();
			}
		});
		btnOk = (Button) view.findViewById(R.id.btnOk);
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				
				if (edtUname.getText().toString().equals("")
						|| edtUname.getText().toString().equals("null")) {
					edtUname.requestFocus();
					edtUname.setError("Please enter username");
					return;
				}

				if (edtPass.getText().toString().equals("")
						|| edtPass.getText().toString().equals("null")) {
					edtPass.requestFocus();
					edtPass.setError("Please enter password");
					return;
				}
			
				
				if (edtFname.getText().toString().equals("")
						|| edtFname.getText().toString().equals("null")) {
					edtFname.requestFocus();
					edtFname.setError("Please enter first name");
					return;
				}

				if (edtLname.getText().toString().equals("")
						|| edtLname.getText().toString().equals("null")) {
					edtLname.requestFocus();
					edtLname.setError("Please enter last name");
					return;
				}
				
				if (edtEmail.getText().toString().equals("")
						|| edtEmail.getText().toString().equals("null")) {
					edtEmail.requestFocus();
					edtEmail.setError("Please enter email");
					return;
				}

				if (edtAddr.getText().toString().equals("")
						|| edtAddr.getText().toString().equals("null")) {
					edtAddr.requestFocus();
					edtAddr.setError("Please enter address");
					return;
				}
				
				
				if (edtLno.getText().toString().equals("")
						|| edtLno.getText().toString().equals("null")) {
					edtLno.requestFocus();
					edtLno.setError("Please enter landline no");
					return;
				}

				if (edtMno.getText().toString().equals("")
						|| edtMno.getText().toString().equals("null")) {
					edtMno.requestFocus();
					edtMno.setError("Please enter mobile no");
					return;
				}
				
				
				if (btnGender.getText().toString().equals("")
						|| btnGender.getText().toString().equals("null")) {
					btnGender.requestFocus();
					btnGender.setError("Please select gender");
					return;
				}

				if (btnBdate.getText().toString().equals("")
						|| btnBdate.getText().toString().equals("null")) {
					btnBdate.requestFocus();
					btnBdate.setError("Please select birth date");
					return;
				}
				
				if (path == "" || path == null) {
					selectPhoto.requestFocus();
					selectPhoto.setError("Please select photo");
					return;
				}
			
				
				String str_uname = edtUname.getText().toString().trim();
				String strPass = edtPass.getText().toString().trim();
				String str_Fname = edtFname.getText().toString().trim();
				String str_Lname = edtLname.getText().toString().trim();
				String str_Email = edtEmail.getText().toString().trim();
				String str_Add = edtAddr.getText().toString().trim();
				String str_Lno = edtLno.getText().toString().trim();
				String str_mno = edtMno.getText().toString().trim();
				String str_Bday = btnBdate.getText().toString().trim();
				String str_Gen = btnGender.getText().toString().trim();
				
				System.out.println("str_uname >>"+str_uname);
				System.out.println("strPass >>"+strPass);
				System.out.println("str_Fname >>"+str_Fname);
				System.out.println("str_Lname >>"+str_Lname);
				System.out.println("str_Email >>"+str_Email);
				System.out.println("str_Lno >>"+str_Lno);
				System.out.println("str_mno >>"+str_mno);
				System.out.println("str_Bday >>"+str_Bday);
				System.out.println("str_Gen >>"+str_Gen);
				
				//get file path
				String url_addPatient = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_addpatient);
				
				new AsynkTaskAddPatient().execute(url_addPatient,ApplicationActivity.getToken(),str_uname,strPass,str_Fname,str_Lname,str_Email,str_Add,str_Lno,str_mno,str_Bday,str_Gen,path);
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
		});
		
		btnCan = (Button) view.findViewById(R.id.btnCan);
		btnCan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				((AppMainFragmentActivity) getActivity()).popFragments();
				
			}
		});
		return view;
	}//End of onCreateView
	
	private OnClickListener datedialog = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			showDatePicker();
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
		   /*Toast.makeText(
		   mActivity,
		     String.valueOf(year) + "-" + String.valueOf(monthOfYear+1)
		       + "-" + String.valueOf(dayOfMonth),
		     Toast.LENGTH_LONG).show();*/

		   btnBdate.setText(strDate);
		  }
		 };
		
		 
		 
	
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
			btnGender.setText(gender_type[which]);
			str_gen = gender_type[which];
			}
			});
		
		durationbuilder.setCancelable(true);
		genderdia = durationbuilder.create();
	}//End of onCreate
	
	private void selectImage(){
	
		 final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
		 PackageManager pm = getActivity().getPackageManager();

			

			if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {

//				text.setText("This device has a Camera");

			} else {

//				text.setText("This device doesn't have a Camera");
			}
	
			  AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        builder.setTitle("Add Photo!");
		        builder.setItems(options, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						 if (options[which].equals("Take Photo"))
			                {
//							 	Intent i = new Intent( android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//								startActivityForResult(i, RESULT_LOAD_IMAGE);
			                    
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);			                    
			                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
			                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			                    getActivity().startActivityForResult(intent, 1);
			                }
			                else if (options[which].equals("Choose from Gallery"))
			                {
			                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			                    getActivity().startActivityForResult(intent, 2);
			 
			                }
			                else if (options[which].equals("Cancel")) {
			                    dialog.dismiss();
			                }
			            }
			        });
			        builder.show();
					
	}//End
	
	
	private OnClickListener genderdialog = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			genderdia.show();
		}
	};


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	
		
//		Toast.makeText(mActivity, "Result code >> "+resultCode, Toast.LENGTH_LONG).show();
//		Toast.makeText(mActivity, "Request code >"+requestCode, Toast.LENGTH_LONG).show();
		
		 if (resultCode == -1) {
	            if (requestCode == 1) {
	            	
	            	
	            	
	            	File d = new File("/mnt/sdcard/Phoenix/default/");
            		
            		if(!d.exists()){
            			d.mkdirs();
            		}
	            	
	                File f = new File(Environment.getExternalStorageDirectory().toString());
	                for (File temp : f.listFiles()) {
	                    if (temp.getName().equals("temp.jpg")) {
	                        f = temp;
	                        break;
	                    }
	                }
	                try {
	                    Bitmap bitmap;
	                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	 
	                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
	                            bitmapOptions); 
	                   
	                    viewImage.setImageBitmap(bitmap);
	 
	                    path = android.os.Environment
	                            .getExternalStorageDirectory()
	                            + File.separator
	                            + "Phoenix" + File.separator + "default";
	                    
	                    
	               /*     File d = new File(path);
	            		
	            		if(!d.exists()){
	            			d.mkdir();
	            		}
	            	*/	
//	                   Toast.makeText(mActivity, "PAth  >> "+path, Toast.LENGTH_LONG).show();
	                    f.delete();
	                    OutputStream outFile = null;
	                    file_name = String.valueOf(System.currentTimeMillis());
	                    File file = new File(path,  file_name + ".jpg");
//	                   File file = new File(path,  String.valueOf(System.currentTimeMillis()) + ".jpg");
//	                    File file = new File(path, "temp.jpg");
	                    path = file.getAbsolutePath();
//	                    Toast.makeText(getActivity(), "name >> "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
	                    try {
	                        outFile = new FileOutputStream(file);
	                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
	                        outFile.flush();
	                        outFile.close();
	                    } catch (FileNotFoundException e) {
	                        e.printStackTrace();
	                    } catch (IOException e) {
	                        e.printStackTrace();
	                    } catch (Exception e) {
	                        e.printStackTrace();
	                    }
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	                
	            } else if (requestCode == 2) {
	 
	                Uri selectedImage = data.getData();
	                String[] filePath = { MediaStore.Images.Media.DATA };
	                Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, null, null, null);
	                c.moveToFirst();
	                int columnIndex = c.getColumnIndex(filePath[0]);
	                path = c.getString(columnIndex);//String picturePath 
	                c.close();
	                Bitmap thumbnail = (BitmapFactory.decodeFile(path));
	                Log.w("path of image from gallery......******************.........", path+"");
//	                Toast.makeText(getActivity(), "Path of image > "+ path, Toast.LENGTH_LONG).show();
	                viewImage.setImageBitmap(thumbnail);
	            }
	        }
	}
	
	@Override
	public boolean onBackPressed() {
		// TODO Auto-generated method stub
		return super.onBackPressed();
		
	}
	
	
	class AsynkTaskAddPatient extends AsyncTask<String,Void,String>{
		ProgressDialog pdialog = new ProgressDialog(mActivity);
		String[] response = new String[2];
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
//			Toast.makeText(mActivity, "AsyncTask is called", Toast.LENGTH_LONG).show();
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
			
			
//			new AsynkTaskAddPatient().execute(url_addPatient,ApplicationActivity.getToken(),str_uname,strPass,str_Fname,str_Lname,str_Email,str_Add,str_Lno,str_mno,str_Bday,str_Gen);
			
			System.out.println("Add Patient "+ "URL >> "+params[0]);
			response = HttpApiCalling.AddNewPatient(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12]);
			System.out.println("patient_list details" + response[0] + "   "
					+ response[1]);

			if(response != null){
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
			
//			Toast.makeText(mActivity, "AsyncTask is executed", Toast.LENGTH_LONG).show();

			if (pdialog.isShowing()) {
				pdialog.dismiss();
				
				if(response != null){
					if(response[0].equalsIgnoreCase("201")){
						
						System.out.println("User is created");
						((AppMainFragmentActivity) getActivity()).popFragments();
						 
						 
						 
					}
					if(response[0].equalsIgnoreCase("400")){
//						System.out.println(LOG_TAG+" >> "+s_msg);
						
					}
				}	
			}
		}
	}
	
	
	 
}
	
