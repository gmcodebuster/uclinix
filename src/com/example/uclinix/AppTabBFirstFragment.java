package com.example.uclinix;
//http://www.truiton.com/2013/05/android-fragmentpageradapter-example/#chitika_close_button
//http://www.piwai.info/android-adapter-good-practices/
import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import quick.action.QuickActionBar;
import quick.action.QuickActionIcons;

import com.example.uclinix.AppMainFragmentActivity.AppConstants;
import com.example.uclinix.AppTabAppointmentFragment.DeleteAppointmentListAsync;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;

public class AppTabBFirstFragment extends BaseFragment{
	
	ListView listPatient;
	final String LOG_TAG = "Patient_List : ";
	static LayoutInflater inflater;
	String s_token;
	String b_id,b_pusername,p_email,p_add,p_ll,p_mo,p_dob,p_gen,p_fsnm,p_fname,p_sname;
	
	ArrayList<String> arrlpid = new ArrayList<String>();
	ArrayList<String> arrlpusername = new ArrayList<String>();
	ArrayList<String> arrlpemail = new ArrayList<String>();
	ArrayList<String> arrlpaddress = new ArrayList<String>();
	ArrayList<String> arrlplandline = new ArrayList<String>();
	ArrayList<String> arrlpmobile = new ArrayList<String>();
	ArrayList<String> arrlpdob = new ArrayList<String>();
	ArrayList<String> arrlpgender = new ArrayList<String>();
	ArrayList<String> arrlpimage = new ArrayList<String>();
	ArrayList<String> arrlpfsname = new ArrayList<String>();
	
	CustomAdapter cadpt;
	AlertDialog alert;
	
	DisplayImageOptions options;
	ImageLoader imageLoader;
	Button addPatient;
	QuickActionBar qab;
	
	String[] splited;
	final QuickActionIcons edit = new QuickActionIcons();
	final QuickActionIcons del = new QuickActionIcons();
	
	public AppTabBFirstFragment(){
		
	}
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
//		setHasOptionsMenu(true);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.ic_menu_search)
//		.showImageForEmptyUri(R.drawable.ic_menu_search)
//		.showImageOnFail(R.drawable.ic_menu_search)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
		
		new PatientListAsync().execute();
		
		
		
		
		edit.setTitle("Edit");  
        edit.setIcon(getResources().getDrawable(R.drawable.edit));
        edit.setOnClickListener(new OnClickListener()
        {
         public void onClick(View v)
         {
        	 
        	 
        if(ApplicationActivity.getUserRole().equalsIgnoreCase("Patient")){	 
//         Toast.makeText(getActivity(),"Edit Contact",Toast.LENGTH_SHORT).show();
         qab.dismiss();
         
                 
         System.out.println("Inside AppTab p_id >> "+b_id +" unamed >> "+b_pusername+" e email > "+p_email +" e add > "+p_add+ " p_ll > "+p_ll+" p_mo > "+p_mo+" dob >> "+p_dob+" p_gen >> "+p_gen+" p_fname >>" +p_fsnm);
         EditPatient fragment = new EditPatient();
         Bundle bundle = new Bundle();
         bundle.putString("pid", b_id);
         bundle.putString("pusernm", b_pusername);
         bundle.putString("pemail", p_email);
         bundle.putString("padd", p_add);
         bundle.putString("pll", p_ll);
         bundle.putString("pmo", p_mo);
         bundle.putString("pdob", p_dob);
         bundle.putString("pgen", p_gen);
         bundle.putString("pfname", splited[0]);
         bundle.putString("plname", splited[1]);
         
         fragment.setArguments(bundle);
       
//         Bundle bundle = new Bundle();
//         bundle.putInt("position", 10);
//         fragment.setArguments(bundle);
         
         mActivity.pushFragments(AppConstants.TAB_B, fragment, true, true);        
        }else{
        	
        	 qab.dismiss();
//        	 alert.show();
        	 alertMessage("TO use this feature please login as a patient.");
        	
        }
      }
    

  });
        
               
        
        
        /*del.setTitle("Delete");       
        del.setIcon(getResources().getDrawable(R.drawable.deleteicon));
        del.setOnClickListener(new OnClickListener()
        {
         public void onClick(View v)
         {
        	 
        if(ApplicationActivity.getUserRole().equalsIgnoreCase("Patient")){	 
         Toast.makeText(getActivity(),"Delete Contact",Toast.LENGTH_SHORT).show();
//         new DeletePatientListAsync().execute();         
         	qab.dismiss();
         	
         
        }else{
        	qab.dismiss();
        	 alertMessage("TO use this feature please login as a patient.");
        }
         }
    
         
        });*/
        
        
      
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		
		View view = inflater.inflate(R.layout.app_tab_b_first_screen, container, false);		
		listPatient = (ListView) view.findViewById(R.id.list_tab2);
		addPatient = (Button) view.findViewById(R.id.addnewpatient);
		// Set title bar to actionbar
	    ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Patient");
		addPatient.setOnClickListener(addPatientListener);
		
		
		listPatient.setOnItemLongClickListener(new OnItemLongClickListener() {

	        @Override
	        public boolean onItemLongClick(AdapterView<?> parent, View view,
	            int position, long id) {
	        	
	        	
	        	
	        	b_id = arrlpid.get(position);
	        	b_pusername = arrlpusername.get(position);						
	        	p_email = arrlpemail.get(position);
	        	p_add = arrlpaddress.get(position);
	        	p_ll = arrlplandline.get(position);
	        	p_mo = arrlpmobile.get(position);
	        	p_dob = arrlpdob.get(position);
	        	p_gen = arrlpgender.get(position);
	        	p_fsnm = arrlpfsname.get(position);
	        	
//	        	str = "Hello I'm your String";
	        	splited = p_fsnm.split("\\s+");
	        	p_fname = splited[0];
//	        	Toast.makeText(mActivity, "ROW ID = ",Toast.LENGTH_LONG).show();
	        	qab = new QuickActionBar(view);

				qab.addItem(edit);
//				qab.addItem(del);
				
				qab.setAnimationStyle(QuickActionBar.GROW_FROM_CENTER);

				qab.show();
				
	          return true;
	        }
	      });
	    
		
		
		
		
		
		return view;
	}//End

	
	OnClickListener addPatientListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(ApplicationActivity.getUserRole().equalsIgnoreCase("Doctor")){
				mActivity.pushFragments(AppConstants.TAB_B, new AddPatient(), true, true);
			}else{
				 alertMessage("TO use this feature please login as a doctor.");
			}
		}
	};
	//AsyncTask
	
	class PatientListAsync extends AsyncTask<String, Void, String>{
		ProgressDialog pdialog = new ProgressDialog(mActivity);
		String[] response = new String[2];
		
		
		
		public PatientListAsync(){
			s_token = ApplicationActivity.getToken();
			
		}
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = ProgressDialog.show(mActivity, "", "");
			pdialog.setContentView(R.layout.wheel);

			pdialog.setCancelable(true);
			arrlpusername.clear();
			arrlpemail.clear();
			arrlpmobile.clear();
			arrlpimage.clear();
			
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
						JSONArray objResult = new JSONArray(response[1]);
						System.out.println("Patient activity >> "+objResult.length());
						for(int i=0;i<objResult.length();i++){
							
							JSONObject jobj = objResult.getJSONObject(i);
							arrlpid.add(jobj.optString("id",""));
							arrlpusername.add(jobj.optString("username",""));							
							arrlpemail.add(jobj.optString("email",""));
							arrlpaddress.add(jobj.optString("address",""));
							arrlplandline.add(jobj.optString("landline",""));
							arrlpmobile.add(jobj.optString("mobile",""));
							arrlpdob.add(jobj.optString("dob",""));
							arrlpgender.add(jobj.optString("gender",""));
						    
							arrlpfsname.add(jobj.optString("patient_name",""));
							
							
							String imgurl = new String(jobj.optString("image", ""));
							
							System.out.println("IMAGE URL Before REMOVING 2 CHAR "+jobj.optString("image", ""));
															
								if(imgurl.startsWith("../")){									
									imgurl = imgurl.substring(2);								
								}
							
							System.out.println("IMAGE URL AFTER REMOVING 2 CHAR "+imgurl);
							arrlpimage.add(imgurl);
							
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
					
					cadpt = new CustomAdapter(mActivity);		
					listPatient.setAdapter(cadpt);
				}
				if(response[0].equalsIgnoreCase("400")){
//					System.out.println(LOG_TAG+" >> "+s_msg);
				}
			}	
	
		}
	}
	
	 //new base adapter
	 
	 private class CustomAdapter extends BaseAdapter {
			
			 
//			 ArrayList<HashMap<String, Object>> strArrList;
			 Context ctx;
		
				public CustomAdapter(Context context) {	
					// let android do the initializing :)
					ctx = context;
					inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					strArrList = hashStr;				
				}
				
				// @Override
				public int getCount() {
					// TODO Auto-generated method stub
					
					return arrlpusername.size();
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
					
					holder.txtName.setText(arrlpusername.get(position));
							
					holder.txtEmail.setText(arrlpemail.get(position));
					holder.textMobile.setText(arrlpmobile.get(position));
					
//					holder.txtSmart.setText(searchResults.get(position)
//							.get("type_of_loyalty_cardArrl").toString());					

					System.out.println("");
					//imageLoader.displayImage("http://www.uclinix.in/images/" + arrlpimage.get(position), holder.imgPhoto, options);//getResources().getString(R.string.url_base)
imageLoader.displayImage("http://www.uclinix.in/images/" + arrlpimage.get(position), holder.imgPhoto, options, new ImageLoadingListener() {
						
						@Override
						public void onLoadingStarted(String imageUri, View view) {
							// TODO Auto-generated method stub
							
							if(arrlpgender.get(position).toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookmale);
							}else{
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookfemale);
							}
						}
									
						
						@Override
						public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
							// TODO Auto-generated method stub
							if(arrlpgender.get(position).toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(0);
							}else{
								holder.imgPhoto.setBackgroundResource(0);
							}
						}
						
						@Override
						public void onLoadingCancelled(String imageUri, View view) {
							// TODO Auto-generated method stub
							if(arrlpgender.get(position).toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookmale);
							}else{
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookfemale);
							}
						}

						@Override
						public void onLoadingFailed(String imageUri, View view,
								FailReason failReason) {
							// TODO Auto-generated method stub
							if(arrlpgender.get(position).toString().equalsIgnoreCase("male")){
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookmale);
							}else{
								holder.imgPhoto.setBackgroundResource(R.drawable.facebookfemale);
							}
						}
					});//getResources().getString(R.string.url_base)
					return convertView;
				}
		
			}//End
	 
	 
	 
	 
	 
//	
//		@Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//                    switch (item.getItemId()) {
//                    case R.id.menuitem_search:
//                                Toast.makeText(mActivity, getString(R.string.ui_menu_search),
//                                                        Toast.LENGTH_LONG).show();
//                                return true;
//                    case R.id.menuitem_send:
//                                Toast.makeText(mActivity, getString(R.string.ui_menu_send),
//                                                        Toast.LENGTH_LONG).show();
//                 
//
//                    case R.id.menuitem_quit:
//                                Toast.makeText(mActivity, getString(R.string.ui_menu_quit),
//                                                        Toast.LENGTH_SHORT).show();
//                                mActivity.finish(); // close the activity
//                                return true;
//                    }
//                    return false;
//        }
	 
}
