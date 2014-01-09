package com.example.uclinix;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.uclinix.AddPatient.AsynkTaskAddPatient;
import com.example.uclinix.AddPatient.LoadImagesFromSDCard;
import com.example.uclinix.EditAppointment.AddAppointCategoryAsync;
import com.nostra13.universalimageloader.cache.disc.DiscCacheAware;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.DiscCacheUtil;
import com.nostra13.universalimageloader.utils.StorageUtils;


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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.CursorLoader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class EditPatient extends BaseFragment {
	EditText edt_user,edt_fname,edt_sname,edt_email,edt_add,edt_ll,edt_mno;
	TextView txt_dob,txt_gen;
	Button btn_dob,btn_gen,btnEdit,btnCancel,btn_selectPhoto;
	ImageView imguserphoto;
	String da,mo,yy,st,s_token,str_gen,path;
	static LayoutInflater inflater;
	ArrayList<CategoryListData> cd_arraylist = new ArrayList<CategoryListData>();
	
	private AlertDialog catDialog;
	final String LOG_TAG = "Edit Patient : ";
	String pb_id = "",pb_username = "",pb_email = "",pb_add = "",pb_ll = "",pb_mo = "",pb_dob="",pgen="",pfname="",plname="",pimg="";
	private String[] gender_type = {"Male","Female"};
	private AlertDialog genderdia;
	 final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 	1;
	 private static final int SELECT_PICTURE = 2;
	 private String selectedImagePath;
	 
	 Uri imageUri 		 = null;
	 
	 DisplayImageOptions options;
	 ImageLoader imageLoader;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		 imageLoader = ImageLoader.getInstance();
			options = new DisplayImageOptions.Builder()
			.showStubImage(R.drawable.ic_launcher)
			.showImageForEmptyUri(R.drawable.ic_menu_search)
			.showImageOnFail(R.drawable.ic_launcher)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
		
		
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
			 pimg = bundle.getString("pimg");	//full path of image
			 
			 Log.d("EditaPatient Bundle",""+pimg);
		 }
		
		 
		System.out.println("pb_id >> "+pb_id +" pb_username >> "+pb_username+" pb_email > "+pb_email +" pb_add > "+pb_add+ " pb_ll > "+pb_ll+
		 " pb_mo > "+pb_mo+" pb_dob > "+pb_dob+" pgen > "+pgen+" pfname > "+pfname+" plname > "+plname+" pimg :>> "+pimg );
		
		
		
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
		 imguserphoto = (ImageView)  view.findViewById(R.id.img_userphoto);
		 
		 btn_dob = (Button) view.findViewById(R.id.btn_dob);
		 btn_gen = (Button) view.findViewById(R.id.btn_gender);
		 
		 btn_selectPhoto = (Button) view.findViewById(R.id.btn_userphoto);
		 
		 btn_dob.setOnClickListener(datedialog);
		 btn_gen.setOnClickListener(genderlistener);
		 
		 btnEdit = (Button) view.findViewById(R.id.btnEdit);
		 btnCancel = (Button) view.findViewById(R.id.btnCan);
		 
		 btnEdit.setOnClickListener(editbtnlistener);
		 btnCancel.setOnClickListener(null);
		 
		 btn_selectPhoto.setOnClickListener(btnphotolistener);
//		 btn_selectPhoto.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					selectImage();
//				}
//			});
		 
		 
		 
		 edt_user.setText(pb_username);
		 edt_email.setText(pb_email);
		 edt_add.setText(pb_add);
		 edt_ll.setText(pb_ll);
		 edt_mno.setText(pb_mo);
		 txt_dob.setText(pb_dob);
		 txt_gen.setText(pgen);
		 edt_fname.setText(pfname);
		 edt_sname.setText(plname);
		 
		
		imageLoader.displayImage(pimg, imguserphoto, options);//getResources().getString(R.string.url_base)
		 return view;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		

		File cacheDir = StorageUtils.getCacheDirectory(mActivity);
		File exCacheDir = mActivity.getExternalCacheDir();
		Log.d("EditPatient Cache >> ",cacheDir.getPath()+" : External Cache : "+exCacheDir.getPath());

				
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
		
		
		
	}// End of onCreate
	
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	
	Bitmap bitmap = null;
		
		Toast.makeText(mActivity, "Result code >> "+resultCode, Toast.LENGTH_LONG).show();
		Toast.makeText(mActivity, "Request code >"+requestCode, Toast.LENGTH_LONG).show();
	
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
                	imguserphoto.setImageBitmap(bm);
               
            }
			
			
		}
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
			
			btn_dob.setError(null);
			showDatePicker();
		}
	}; 
	
	private OnClickListener genderlistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			btn_gen.setError(null);
			genderdia.show();
			
		}
	};
	
	private OnClickListener btnphotolistener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			selectImage();
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
//						edt_fname.requestFocus();
//						edt_fname.setError("Please enter first name");
//						return;
					}

					if (edt_sname.getText().toString().equals("")
							|| edt_sname.getText().toString().equals("null")) {
//						edt_sname.requestFocus();
//						edt_sname.setError("Please enter last name");
//						return;
					}
					
					if (edt_email.getText().toString().equals("")
							|| edt_email.getText().toString().equals("null")) {
//						edt_email.requestFocus();
//						edt_email.setError("Please enter email");
//						return;
					}

					if (edt_add.getText().toString().equals("")
							|| edt_add.getText().toString().equals("null")) {
						edt_add.requestFocus();
						edt_add.setError("Please enter address");
						return;
					}
					
					
					if (edt_ll.getText().toString().equals("")
							|| edt_ll.getText().toString().equals("null")) {
//						edt_ll.requestFocus();
//						edt_ll.setError("Please enter landline no");
//						return;
					}

					if (edt_mno.getText().toString().equals("")
							|| edt_mno.getText().toString().equals("null")) {
//						edt_mno.requestFocus();
//						edt_mno.setError("Please enter mobile no");
//						return;
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
			}//End of AsynkTask AddPatient
			
			
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
//						Uri.withAppendedPath Method Description parameters				
//						baseUri Uri to append path segment to pathSegment encoded path segment to append 
//						Returns a new Uri based on baseUri with the given segment appended to the path
						
						uri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,""+urls[0]);
//						uri = Uri.parse(urls[0]);
						/**** Decode an input stream into bitmap *****/
						bitmap = BitmapFactory.decodeStream(mActivity.getContentResolver().openInputStream(uri));
						
						
						if(bitmap != null){
							
							//Creates a new bitmap, scaled from an existing bitmap
							
//							Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 70, 70, true);
//		                    int w = scaled.getWidth();
//		                    int h = scaled.getHeight();
		                    // Setting post rotate to 90
//		                    Matrix mtx = new Matrix();
//		                    mtx.postRotate(90);
		                    // Rotating Bitmap
//		                   newBitmap = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);	
		                   
													
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
//				super.onPostExecute(result);
//				Note : You can call UI Element here.
//				Close progress
			
				Dialog.dismiss();
				
				if(mBitmap!= null){
					
					//set Image to ImageView
					
					imguserphoto.setImageBitmap(mBitmap);
					
					
				}
			}
		}//End of load image async task
			
			
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
					
					
//					cursor = activity.managedQuery(
//							imageUri,		//Get data for specific imageURI
//							proj,			// Which columns to return
//							null,			// WHERE clause; which rows to return (all rows)
//							null,			// WHERE clause selection arguments (none)
//							null			// Order-by clause (ascending by name)
//							);
					CursorLoader loader = new CursorLoader(mActivity, imageUri, proj, null, null, null); 
					cursor = loader.loadInBackground();
					
//					 cursor = mActivity.getContentResolver().query(imageUri, proj, null, null, null);
					   			
					//Get Query Data
					
					int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID);
					int columnIndexThumb = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);
					file_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					
//					int orientation_ColumnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.ORIENTATION);
					
					int size = cursor.getCount();
					
					/****** If size is 0, there are no images on the SD card.  *********/
					
					if(size == 0){
//						imageDetails.setText("No Image");
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
//							imageDetails.setText(CapturedImageDetails);
							
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
		
			//Select photo from gallery
			
			 public String getPath(Uri uri) {
			        String[] projection = { MediaStore.Images.Media.DATA };
			       
//					@SuppressWarnings("deprecation")
//					Cursor cursor = mActivity.managedQuery(uri, projection, null, null, null);
					
					CursorLoader loader = new CursorLoader(mActivity, uri, projection, null, null, null); 
					Cursor cursor = loader.loadInBackground();
					
			        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			        cursor.moveToFirst();
			        return cursor.getString(column_index);
			    }
			 
			 
			 //Cachedirectory location
			/* public static File getCacheDirectory(Context context) {
			        File appCacheDir = null;
			        if (Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)) {
			            appCacheDir = StorageUtils.getExternalCacheDir(context);
			        }
			        if (appCacheDir == null) {
			            appCacheDir = context.getCacheDir();
			        }
			        return appCacheDir;
			    }*/
			 
			
}
