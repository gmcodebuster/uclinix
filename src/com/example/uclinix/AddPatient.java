package com.example.uclinix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.content.CursorLoader;
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
	 
	 final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 	1;
	 Uri imageUri 		 = null;
	 
	 private static final int SELECT_PICTURE = 2;
	 private String selectedImagePath;
	 
	 	private final static String DB_PATH = "/data/data/com.example.uclinix/files/";
		private final static String DB_NAME ="ic_launcher.png";			//new db file name
				
	 
	 Context ctx;
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		ctx=mActivity;
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
		
		copyLauncherFile(ctx);
		String uriStr = "android.resource://com.example.uclinix/res/drawable" + "/" + R.raw.ic_launcher; 
		File imageFile = new File(uriStr); 
		Log.d("Add Patient activity >> ","Absolute path >> "+imageFile.getAbsolutePath()+" >>> path > "+imageFile.getPath());
//		path = "android.resource://com.example.uclinix/res/raw" + "/" + R.raw.ic_launcher;
		
		path = "/data/data/com.example.uclinix/files/ic_launcher.png";		
		
		try {
						
		    File filename = new File(Environment.getExternalStorageDirectory()+"/logfile_uclinix_addpati.txt"); 
		    filename.createNewFile(); 
		    String cmd = "logcat -d -f -v time"+filename.getAbsolutePath();
		    Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
		    // TODO Auto-generated catch block
		    e.printStackTrace();
		}
		edtUname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtUname.setError(null);	
			}
		});
		
		edtPass.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtPass.setError(null);
			}
		});
		
		edtFname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtFname.setError(null);
			}
		});
		
		edtLname.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtLname.setError(null);
			}
		});
		
		edtEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtEmail.setError(null);
			}
		});
		
		edtAddr.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtAddr.setError(null);
			}
		});
		
		edtLno.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtLno.setError(null);	
			}
		});
		
		edtMno.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				edtMno.setError(null);	
			}
		});
		
		
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
				
				btnGender.setError(null);
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
				//optional
				/*if (edtEmail.getText().toString().equals("")
						|| edtEmail.getText().toString().equals("null")) {
					
					edtEmail.setError("Please enter email");
					
				}
			*/
				if (edtAddr.getText().toString().equals("")
						|| edtAddr.getText().toString().equals("null")) {
					edtAddr.requestFocus();
					edtAddr.setError("Please enter address");
					return;
				}
				
				//optional
			/*	if (edtLno.getText().toString().equals("")
						|| edtLno.getText().toString().equals("null")) {
					edtLno.requestFocus();
					edtLno.setError("Please enter landline no");
					return;
				}
				//optional
				if (edtMno.getText().toString().equals("")
						|| edtMno.getText().toString().equals("null")) {
					edtMno.requestFocus();
					edtMno.setError("Please enter mobile no");
					return;
				}
				*/
				
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
				//optional
				if (path == "" || path == null || path.length() == 0) {
//					selectPhoto.requestFocus();
//					selectPhoto.setError("Please select photo");
//					return;
					Log.d("AddPatient Activity", "if path is empty");
					
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
			btnBdate.setError(null);
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
/*		                    
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);			                    
			                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
			                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
			                    getActivity().startActivityForResult(intent, 1);*/
							 
							 /********************camera intent start***************/
								//Define the file-name to save photo taken by Camera activity
								
								String fileName = "Camera_Example.jpg";
								
								//Create parameters for Intent with filename
								
								ContentValues values = new ContentValues();
								
								values.put(MediaStore.Images.Media.TITLE,fileName);
								values.put(MediaStore.Images.Media.DESCRIPTION, "Image capture by camera");
								
								//imageUri is the current activity attribute, define and save it for later usage
								
								imageUri = mActivity.getContentResolver().insert(
										MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
								Toast.makeText(mActivity, "Image URI path >> "+imageUri.toString(), 5000).show();
								/***** EXTERNAL_CONTENT_URI : style URI for the "primary" external storage volume. **/
								
								//standard Intent action that can be sent to have the camera
								//application capture an image and return it.
								
								Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
								
								intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
								intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
								
								getActivity().startActivityForResult(intent,CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
								
								
								/***Camera Intent End *****/
		                }
			                else if (options[which].equals("Choose from Gallery"))
			                {
			                    Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			                    getActivity().startActivityForResult(intent, 2);
			                	
			                	
//			                	Intent intent = new Intent();
//			                    intent.setType("image/*");
//			                    intent.setAction(Intent.ACTION_GET_CONTENT);
//			                    getActivity().startActivityForResult(Intent.createChooser(intent,"Select Picture"), SELECT_PICTURE);
			 
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
		Bitmap bitmap = null;
		
		Toast.makeText(mActivity, "Result code >> "+resultCode, Toast.LENGTH_LONG).show();
		Toast.makeText(mActivity, "Request code >"+requestCode, Toast.LENGTH_LONG).show();
		
		 /*if (resultCode == -1) {
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
	                    
	                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
	 
	                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(),
	                            bitmapOptions); 
	                   
	                    viewImage.setImageBitmap(bitmap);
	                    
	                    path = android.os.Environment
	                            .getExternalStorageDirectory()
	                            + File.separator
	                            + "Phoenix" + File.separator + "default";
	                    
	                    
	                    File d = new File(path);
	            		
	            		if(!d.exists()){
	            			d.mkdir();
	            		}
	            		
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
	                }finally{
	                	if(bitmap != null)
	                	bitmap.recycle();
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
	        }*/
		
//		if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
//			if(resultCode == Activity.RESULT_OK){
//				/********* Load Captured Image And Data Start ******************/
//				
//				String imageId = convertImageUriToFile(imageUri,mActivity);
//				Toast.makeText(mActivity, "Physical path >> "+imageId, 5000).show();
//				
//				// Create and execute AsyncTask to load capture image
//				
//				new LoadImagesFromSDCard().execute(""+imageId);
//				
//				/****** Load Captured Image And Data End ***********/
//			}else if( resultCode == Activity.RESULT_CANCELED){
//				Toast.makeText(mActivity, "Picture was not taken ", Toast.LENGTH_SHORT).show();
//			
//			}else {
//				Toast.makeText(mActivity, "Picture was not taken ", Toast.LENGTH_SHORT).show();
//			}
//		}
//		
//		
//		
//        if (requestCode == SELECT_PICTURE) {
//           	if (resultCode == Activity.RESULT_OK) {
//                Uri selectedImageUri = data.getData();
//                selectedImagePath = getPath(selectedImageUri);
//                System.out.println("Image Path : " + selectedImagePath);
//                Toast.makeText(mActivity.getApplicationContext(), "Image Path : " + selectedImagePath, Toast.LENGTH_LONG).show();
//                Bitmap bm = reduceImageSize(selectedImagePath);
//                if(bm != null)
//                	viewImage.setImageBitmap(bm);
//                bm.recycle();
//            }
//        }
		
		
		if(resultCode == mActivity.RESULT_OK){
			Log.d("Inside Add Patient ","Activity.RESULT_OK");
			if(requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE){
				/********* Load Captured Image And Data Start ******************/
				Log.d("Inside Add Patient ","CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE");
				String imageId = convertImageUriToFile(imageUri,mActivity);				
				Toast.makeText(mActivity, "Physical path >> "+imageId, 5000).show();
				
				// Create and execute AsyncTask to load capture image
				
				new LoadImagesFromSDCard().execute(""+imageId);
				
				/****** Load Captured Image And Data End ***********/
			}
			
			if (requestCode == SELECT_PICTURE) {
				
				Log.d("Inside Add Patient ","SELECT_PICTURE");
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                path = getPath(selectedImageUri);
                System.out.println("Image Path : " + selectedImagePath);
                Toast.makeText(mActivity.getApplicationContext(), "Image Path : " + selectedImagePath, Toast.LENGTH_LONG).show();
                Bitmap bm = reduceImageSize(selectedImagePath);
                if(bm != null)
                	viewImage.setImageBitmap(bm);
               
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
			Log.d("UCLINIX ADD PATIENT", "URL >> "+params[0]);
			System.out.println("Add Patient "+ "URL >> "+params[0]);
			response = HttpApiCalling.AddNewPatient(params[0],params[1],params[2],params[3],params[4],params[5],params[6],params[7],params[8],params[9],params[10],params[11],params[12]);
			
			Log.d("UCLINIX ADD PATIENT", "RESPONSE >> "+response[0] + "   "	+ response[1]);
			System.out.println("patient_list details" + response[0] + "   "	+ response[1]);

			
			if(response != null){
				Log.d("UCLINIX ADD PATIENT", "RESPONSE >> NOT NULL");
				if(response[0].equalsIgnoreCase("201")){
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
						Log.d("UCLINIX ADD PATIENT", "RESPONSE OBJECT>> "+objResult);
						System.out.println("Patient activity >> "+objResult.length());
						
						System.out.println("id >"+objResult.optString("id", "")+
								"username >"+objResult.optString("username", "")+
								"email >"+objResult.optString("email", ""));
						
						
						
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				Log.d("UCLINIX ADD PATIENT", "RESPONSE ERROR 201>> ");
				e.printStackTrace();
			}
		}//response is 200
		if(response[0].equalsIgnoreCase("400")){		
			Log.d("UCLINIX ADD PATIENT", "RESPONSE >>400");
			try {
				JSONObject objResult = new JSONObject(response[1]);
				
					
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//response is 400
		if(response[0].equalsIgnoreCase("401")){		
			Log.d("UCLINIX ADD PATIENT", "RESPONSE >> 401");
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
	
	
	
	
/******* Convert Image Uri path physical path **********************/
	
	public String convertImageUriToFile(Uri imageUri, FragmentActivity activity){
		
		Cursor cursor = null;
		int imageID = 0;
		int file_ColumnIndex;
		String Path = null;
		try{
			
			/******** Which columns values want to get ****************/
			String[] proj = {
							MediaStore.Images.Media.DATA,
							MediaStore.Images.Media._ID,
							MediaStore.Images.Thumbnails._ID,
							MediaStore.Images.ImageColumns.ORIENTATION
					
			};
			
			
//			cursor = activity.managedQuery(
//					imageUri,		//Get data for specific imageURI
//					proj,			// Which columns to return
//					null,			// WHERE clause; which rows to return (all rows)
//					null,			// WHERE clause selection arguments (none)
//					null			// Order-by clause (ascending by name)
//					);
			CursorLoader loader = new CursorLoader(mActivity, imageUri, proj, null, null, null); 
			cursor = loader.loadInBackground();
			
//			 cursor = mActivity.getContentResolver().query(imageUri, proj, null, null, null);
			   			
			//Get Query Data
			
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
			int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
			file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			
//			int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
			
			int size = cursor.getCount();
			
			/****** If size is 0, there are no images on the SD card.  *********/
			
			if(size == 0){
//				imageDetails.setText("No Image");
			}else{
				int thumbID = 0;
				if(cursor.moveToFirst()){
					
					/******* Captured image details *************/
					
					// Used to show image on view in LoadImagesFromSDCard class
					
					imageID = cursor.getInt(columnIndex);
					
					thumbID = cursor.getInt(columnIndexThumb);
					
					Path = cursor.getString(file_ColumnIndex);
					path = Path;
					//String orientation = cursor.getString(orientation_ColumnIndex);
					
					String CapturedImageDetails = "CapturedImageDetails : \n\n"
													+" ImageID :"+imageID+"\n"
													+" ThumbID :"+thumbID+"\n"
													+" Path :"+Path+"\n";
					
					//Show Captured Image detail on activity
//					imageDetails.setText(CapturedImageDetails);
					
				}
			}
		}finally{
			if(cursor != null){
				cursor.close();
			}
		}
		
		//Return captured Image ImageID ( By this ImageID Image will load from sdcard )
		
		return ""+imageID;
	}
	
	/**
	 * 
	 * Async Task for loading the images from sd card
	 */
	
	public class LoadImagesFromSDCard extends AsyncTask<String, Void, Void>{
		
		private ProgressDialog Dialog = new ProgressDialog(mActivity);
		
		Bitmap mBitmap;
		
		protected void onPreExecute(){
			
			//Note : You can call UI Element here.
			
			//progress dialog
			
			Dialog.setMessage("Loading image from sdcard..");
			Dialog.show();
		}
		
		
		//call after onPreExecute method
		protected Void doInBackground(String... urls){
			
			Bitmap bitmap = null;
			Bitmap newBitmap = null;
			
			Uri uri = null;
			
			try{
//				Uri.withAppendedPath Method Description parameters				
//				baseUri Uri to append path segment to pathSegment encoded path segment to append 
//				Returns a new Uri based on baseUri with the given segment appended to the path
				
				uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,""+urls[0]);
//				uri = Uri.parse(urls[0]);
				/**** Decode an input stream into bitmap *****/
				bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(uri));
				
				
				if(bitmap != null){
					
					//Creates a new bitmap, scaled from an existing bitmap
					
//					Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//                    int w = scaled.getWidth();
//                    int h = scaled.getHeight();
                    // Setting post rotate to 90
//                    Matrix mtx = new Matrix();
//                    mtx.postRotate(90);
                    // Rotating Bitmap
//                   newBitmap = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);	
                   
											
				newBitmap = Bitmap.createScaledBitmap(bitmap,70,70,true);
				
				bitmap.recycle();
				if(newBitmap != null){
					mBitmap = newBitmap;
				}
				}
			}catch(IOException e){
				//Error fetching image, try to recover
				
				//cancel execution of this task.
				cancel(true);
				
			}
			return null;
		}
		
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
//		super.onPostExecute(result);
//		Note : You can call UI Element here.
//		Close progress
	
		Dialog.dismiss();
		
		if(mBitmap!= null){
			
			//set Image to ImageView
			
			viewImage.setImageBitmap(mBitmap);
			
			
		}
	}
	}
	
	
	
	//Select photo from gallery
	
	 public String getPath(Uri uri) {
	        String[] projection = { MediaStore.Images.Media.DATA };
	       
//			@SuppressWarnings("deprecation")
//			Cursor cursor = mActivity.managedQuery(uri, projection, null, null, null);
			
			CursorLoader loader = new CursorLoader(mActivity, uri, projection, null, null, null); 
			Cursor cursor = loader.loadInBackground();
			
	        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
	        cursor.moveToFirst();
	        return cursor.getString(column_index);
	    }
	 
	 public Bitmap reduceImageSize(String mSelectedImagePath){
		 
      Bitmap m = null;
      try {
        File f = new File(mSelectedImagePath);

      //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(new FileInputStream(f),null,o);

        //The new size we want to scale to
        final int REQUIRED_SIZE=150;

        //Find the correct scale value. It should be the power of 2.
        int width_tmp=o.outWidth, height_tmp=o.outHeight;
        int scale=1;
        while(true){
            if(width_tmp/2 < REQUIRED_SIZE || height_tmp/2 < REQUIRED_SIZE)
                break;
            width_tmp/=2;
            height_tmp/=2;
            scale*=2;
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize=scale;
        m = BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
    } catch (FileNotFoundException e) {
        Toast.makeText(mActivity.getApplicationContext(), "Image File not found in your phone. Please select another image.", Toast.LENGTH_LONG).show();
    }
    return  m;
}
	 
	 
	 public void copyLauncherFile(Context ctx)
		{
			
			
			
			try{
				
							
			InputStream myInput = ctx.getAssets().open(DB_NAME);
			
			File d = new File(DB_PATH);
			
			if(!d.exists()){
				d.mkdir();
			}
			
			String outputFileName = DB_PATH + DB_NAME;
			
			File f = new File(outputFileName);
			
			//Do nothing if file exists already
			if(f.exists()){
				return;
			}			
			
			OutputStream myOutput = new FileOutputStream(outputFileName);
			
			byte[] buffer = new byte[1024];
			int length; 
			
			while((length=myInput.read(buffer)) > 0){
				myOutput.write(buffer,0,length);
			}
			
			myOutput.flush();
			myOutput.close();
			myInput.close();
			
			}
			catch(FileNotFoundException f)
			{
				f.printStackTrace();
				f.getMessage();
			}
			catch (IOException e) {
				e.printStackTrace();
				e.getMessage();
			}
			
		}
	 
	 
}
	
