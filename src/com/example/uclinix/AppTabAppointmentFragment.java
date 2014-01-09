package com.example.uclinix;
//https://github.com/iamjayanth/FragmentTabStudy/blob/master/src/com/research/fragmenttabstudy/tabB/AppTabBFirstFragment.java
//http://www.androiddevelopersolution.com/
//http://bartinger.at/listview-with-sectionsseparators/
//http://samir-mangroliya.blogspot.in/

import java.text.ParseException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import quick.action.QuickActionBar;
import quick.action.QuickActionIcons;

import com.example.uclinix.AppMainFragmentActivity.AppConstants;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.AdapterView.OnItemLongClickListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AppTabAppointmentFragment extends BaseFragment implements OnItemClickListener{
	ArrayList<Item> items = new ArrayList<Item>();
	
	ListView listview;
	static LayoutInflater inflater;
	Button btnaddAppointment,btnemptyReload;
	TextView txtEmptyView;
	String s_token;
	final String LOG_TAG = "Appointment_List : ";
	LinearLayout emptylayout;
	final QuickActionIcons edit = new QuickActionIcons();
	final QuickActionIcons del = new QuickActionIcons();
	String appid,pid,cid,sttime,edtime,sdate;
	QuickActionBar qab;
	String strdate;
	EntryAdapter1 adapter;
	AlertDialog alert;
	
	DisplayImageOptions options;
	ImageLoader imageLoader;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		
		
		
		imageLoader = ImageLoader.getInstance();
		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.ic_menu_search)
//		.showImageForEmptyUri(R.drawable.ic_menu_search)
//		.showImageOnFail(R.drawable.ic_menu_search)
		.cacheInMemory(true)
		.cacheOnDisc(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.build();
		
	  System.out.println(LOG_TAG + " OnCreate()");
	       edit.setTitle("Edit");  
	        edit.setIcon(getResources().getDrawable(R.drawable.edit));
	        edit.setOnClickListener(new OnClickListener()
	        {
	         public void onClick(View v)
	         {
	        	 
	        	if(ApplicationActivity.getUserRole().equalsIgnoreCase("doctor")){	        	 
	        	 
//	         Toast.makeText(getActivity(),"Edit Contact",Toast.LENGTH_SHORT).show();
	         qab.dismiss();	         
	        
	         java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm a");
	         java.util.Date date1,date2;
	         long diff = 0;
	         long timeInMinutes= 0;
				try {
					date1 = df.parse(sttime);
					date2 = df.parse(edtime);
					diff = date2.getTime() - date1.getTime();
					
					long timeInSeconds = diff / 1000;
					timeInMinutes = timeInSeconds/60;
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					diff = -1;
				}
	              
				String s = String.valueOf(timeInMinutes);
				System.out.println("Time difference  >> "+diff +" >> time in minutes >>"+ timeInMinutes);
				System.out.println("Date of appointment >> "+strdate);
	         
		         System.out.println("Inside AppTab e_id >> "+appid +" e_pid >> "+pid+" e cid > "+cid +" e sdate > "+sdate+ " e_stime > "+sttime+" e_dur > "+s);
		         EditAppointment fragment = new EditAppointment();
		         Bundle bundle = new Bundle();
		         bundle.putString("id", appid);
		         bundle.putString("pid", pid);
		         bundle.putString("cid", cid);
		         bundle.putString("sdate", sdate);//strdate
		         bundle.putString("stime", sttime);
		         bundle.putString("dur", s);
		         
		         fragment.setArguments(bundle);
		       
	//	         Bundle bundle = new Bundle();
	//	         bundle.putInt("position", 10);
	//	         fragment.setArguments(bundle);
		         
		         mActivity.pushFragments(AppConstants.TAB_A, fragment, true, true);        
	       }else{
	    	   qab.dismiss();
	        	 alert.show();		    	   
	       		}      	
	        	
	         }
	    

	        });
	        
	               
	        
	        
	        del.setTitle("Delete");       
	        del.setIcon(getResources().getDrawable(R.drawable.deleteicon));
	        del.setOnClickListener(new OnClickListener()
	        {
	         public void onClick(View v)
	         {
//	         Toast.makeText(getActivity(),"Delete Contact",Toast.LENGTH_SHORT).show();
	         
	         if(ApplicationActivity.getUserRole().equalsIgnoreCase("Doctor")){
	        	 qab.dismiss();
	        	 new DeleteAppointmentListAsync().execute(appid);
	         
	        
	         } else{
	        	
	        	 qab.dismiss();
	        	 alert.show();	
	        	      	 
	         	}	         
	         }
	    
	         
	        });
	        
	        
	        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	        builder.setMessage("TO use this feature please login as a doctor.")
	               .setCancelable(false)
	               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
	                   public void onClick(DialogInterface dialog, int id) {
	                        //do things
	                	   
	                   }
	               });
	        alert = builder.create();
	           	
	        
		
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.app_tab_c_first_screen, container, false);	
		// Set title bar to actionbar
	    ((AppMainFragmentActivity) getActivity()).setActionBarTitle("Appointment");
	    
		listview = (ListView) view.findViewById(R.id.listView_main);		
		btnaddAppointment = (Button) view.findViewById(R.id.addappointment);
		btnaddAppointment.setOnClickListener(btnAddAppointment);
	    listview.setOnItemClickListener(this);
	    
	    
	    listview.setOnItemLongClickListener(new OnItemLongClickListener() {

	        @Override
	        public boolean onItemLongClick(AdapterView<?> parent, View view,
	            int position, long id) {
	        	
	        	EntryItem item = (EntryItem)items.get(position);
	        	
	        	appid = item.id;
	        	
	        	pid = item.patient;
	        	cid = item.category;
	        	sttime = item.starttime;
	        	edtime = item.endtime;
	        	sdate = item.stdate;
	        	
//	        	Toast.makeText(mActivity, "ROW ID = "+appid,Toast.LENGTH_LONG).show();
	        	qab = new QuickActionBar(view);

				qab.addItem(edit);
				qab.addItem(del);
				
				qab.setAnimationStyle(QuickActionBar.GROW_FROM_CENTER);

				qab.show();
				
	          return true;
	        }
	      });
	    
	    
		return view;
	
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		System.out.println(LOG_TAG + " OnResume()");
		new AppointmentListAsync().execute();
		
	}
	
	
	private OnClickListener btnReload = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new AppointmentListAsync().execute();
		}
	};
	
	private OnClickListener btnAddAppointment = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(ApplicationActivity.getUserRole().equalsIgnoreCase("doctor")){
			
			mActivity.pushFragments(AppConstants.TAB_A, new AddAppointment(), true, true);
			}else{
				alert.show();				
			}
			
		}
	};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		EntryItem item = (EntryItem)items.get(arg2);		
//		Toast.makeText(mActivity, "You clicked " + item.getPatient()+ " Position >> "+ arg2 , Toast.LENGTH_SHORT).show();
	}

	public class EntryAdapter1 extends ArrayAdapter<Item>{

		private Context cont;
		private ArrayList<Item> items;
		private LayoutInflater vi;
		
		public EntryAdapter1(ArrayList<Item> item){
		super(mActivity,0,item);	
			
			this.items = item;
			
			vi = (LayoutInflater) mActivity.getSystemService(mActivity.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View v= convertView;
			final Item i = items.get(position);
			if(i!=null){
				if(i.isSection()){
					SectionItem si = (SectionItem)i;
					v = vi.inflate(R.layout.list_item_section, null);
					v.setOnClickListener(null);
				    v.setOnLongClickListener(null);
				    v.setLongClickable(false);
				     
				    final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
				    sectionView.setText(si.getTitle());
					
				}else{
					final EntryItem ei = (EntryItem)i;
					final ImageView img_photo;
					 v = vi.inflate(R.layout.list_item_entry, null);
					    final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
					    final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
					    final TextView starttime = (TextView)v.findViewById(R.id.list_item_entry_starttime);
					    final TextView endtime = (TextView)v.findViewById(R.id.list_item_entry_endtime);
					    img_photo = (ImageView) v.findViewById(R.id.list_item_entry_drawable);
					    
					    if (title != null) 
					    	title.setText(ei.getPatient());//
					    if(subtitle != null)//subtitle
					    	subtitle.setText("Category:"+ei.getCategory());//subtitle
					    if(starttime != null)
					    	starttime.setText("Start:"+ei.getStarttime());
					    if(endtime != null)
					    	endtime.setText("End:"+ei.getEndtime());
					    
					    imageLoader.displayImage("http://www.uclinix.in/images/" + ei.getimgUrl(), img_photo, options,new ImageLoadingListener() {
							
							@Override
							public void onLoadingStarted(String imageUri, View view) {
								// TODO Auto-generated method stub

								if(ei.getGender().equalsIgnoreCase("male")){
									
									img_photo.setBackgroundResource(R.drawable.facebookmale);
									
								}else{
									img_photo.setBackgroundResource(R.drawable.facebookfemale);
								}
							}
													
							@Override
							public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								if(ei.getGender().equalsIgnoreCase("male")){
									
									img_photo.setBackgroundResource(0);
								}else{
									img_photo.setBackgroundResource(0);
								}
							}
							
							@Override
							public void onLoadingCancelled(String imageUri, View view) {
								// TODO Auto-generated method stub
								if(ei.getGender().equalsIgnoreCase("male")){
									
									img_photo.setBackgroundResource(R.drawable.facebookmale);
								}else{
									img_photo.setBackgroundResource(R.drawable.facebookfemale);
								}
							}

							@Override
							public void onLoadingFailed(String imageUri,
									View view, FailReason failReason) {
								// TODO Auto-generated method stub
									if(ei.getGender().equalsIgnoreCase("male")){
									
									img_photo.setBackgroundResource(R.drawable.facebookmale);
								}else{
									img_photo.setBackgroundResource(R.drawable.facebookfemale);
								}
							}
						});//getResources().getString(R.string.url_base)
					  //  imageLoader.displayImage("http://www.uclinix.in/images/" + ei.getImageURL(), imgViewPhoto, options);//getResources().getString(R.string.url_base)
				}
			}
			
			return v;
		}
	
	}//End

	 //new base adapter
	 
	 private static class CustomAdapter1 extends BaseAdapter {
			
			 
//			 ArrayList<HashMap<String, Object>> strArrList;
			 Context ctx;
			 ArrayList<Item> items;
		
				public CustomAdapter1(Context context,ArrayList<Item> item) {	
					// let android do the initializing :)
					ctx = context;
					items = item;
					inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//					strArrList = hashStr;				
				}
				
				// @Override
				public int getCount() {
					// TODO Auto-generated method stub
					
					return items.size();
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
				private class ViewHolder2 {
					public TextView title;
					public TextView subtitle;
					public ImageView imgPhoto;
					public TextView textMobile;					
				}
	
				private class ViewHolder1{
					public TextView section;
				}
				
				ViewHolder2 holder2;
				ViewHolder1 holder1;
				@Override
				public View getView(int position, View convertView,
						ViewGroup parent) {					
				
					final Item i = items.get(position);
					if(i!=null){
					if (convertView == null) {
					
						if(i.isSection()){
							
								convertView = inflater.inflate(R.layout.list_item_section, null);
								//section view
								holder1 = new ViewHolder1();
								holder1.section = (TextView) convertView.findViewById(R.id.list_item_section_text);		
								convertView.setTag(holder1);
						}else{
							convertView = inflater.inflate(R.layout.list_item_entry, null);
							//Item view
							holder2 = new ViewHolder2();
							holder2.title = (TextView) convertView.findViewById(R.id.list_item_entry_title);
							holder2.subtitle = (TextView) convertView.findViewById(R.id.list_item_entry_summary);
														
							convertView.setTag(holder2);
						}
	
					}
					else
					{
						if(i.isSection()){
							holder1 = (ViewHolder1) convertView.getTag();
						}else{
							holder2 = (ViewHolder2) convertView.getTag();
						}
						
					}
					
					holder1.section.setText("Gaurav");
					
					holder2.title.setText("gaurav.mehta@gmail.com");
					holder2.subtitle.setText("1234567889");
					
					}//End of i-if
					return convertView;
				}
		
			}//End
	 
 //new base adapter
	 
	 private static class CustomAdapter extends BaseAdapter {
			
			 
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
					
//					holder.txtSmart.setText(searchResults.get(position)
//							.get("type_of_loyalty_cardArrl").toString());
					

					return convertView;
				}
		
			}//End
	 

		@Override
     public boolean onOptionsItemSelected(MenuItem item) {
			
			getActivity().supportInvalidateOptionsMenu();
                 switch (item.getItemId()) {
                 case R.id.menuitem_search:
//                             Toast.makeText(mActivity, getString(R.string.ui_menu_search),
//                                                     Toast.LENGTH_LONG).show();
                             return true;
                 case R.id.menuitem_send:
//                             Toast.makeText(mActivity, getString(R.string.ui_menu_send),
//                                                     Toast.LENGTH_LONG).show();
              

                 case R.id.menuitem_quit:
//                             Toast.makeText(mActivity, getString(R.string.ui_menu_quit),
//                                                     Toast.LENGTH_SHORT).show();
//                             mActivity.finish(); // close the activity
                             return true;
                 }
                 return false;
     }
		
		
		//AsyncTask
		
		class AppointmentListAsync extends AsyncTask<String, Void, String>{
			ProgressDialog pdialog = new ProgressDialog(mActivity);
			String[] response = new String[2];
			
			
			public AppointmentListAsync(){
				s_token = ApplicationActivity.getToken();
								
			}
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pdialog = ProgressDialog.show(mActivity, "", "");
				pdialog.setContentView(R.layout.wheel);
				items.clear();
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
							
				String url_patientlist = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_appointment_list);
				System.out.println(LOG_TAG + "URL >> "+url_patientlist);
				response = HttpApiCalling.appointmentList(url_patientlist,ApplicationActivity.getToken());
				System.out.println("patient_list details" + response[0] + "   "
						+ response[1]);

				if(response[1] != null){
					if(response[0].equalsIgnoreCase("200")){
						try{
							//first array
//						JSONArray topArray = new JSONArray(getResources().getString(R.string.json_appointmentlist));
						JSONArray topArray = new JSONArray(response[1].toString());
						System.out.println("MAIN ACTIVITY JSON Array"+topArray);
						for(int i=0;i<topArray.length();i++){
							//Create object
							JSONObject outerObj = topArray.getJSONObject(i);
							
							strdate = outerObj.optString("date","");
							
							
							System.out.println("MAIN ACTIVITY Date > "+strdate);
							
							
							items.add(new SectionItem(strdate));
							JSONArray appointArray = outerObj.getJSONArray("appointments");
							for(int j=0;j<appointArray.length();j++){
							System.out.println("MAIN ACTIVITY appointArray > "+appointArray.length());
								JSONObject innerObj = appointArray.getJSONObject(j);
								
								System.out.println("MAIN ACTIVITY innerObj > "+innerObj.toString());

								String imgUrl = new String(innerObj.optString("image", ""));
								
								System.out.println("IMAGE URL Before REMOVING 2 CHAR "+innerObj.optString("image", ""));
																
									if(imgUrl.startsWith("../")){									
									imgUrl = imgUrl.substring(2);								
								}
								
								System.out.println("IMAGE URL AFTER REMOVING 2 CHAR "+imgUrl);
								
								
								items.add(new EntryItem(innerObj.optString("id",""),
														innerObj.optString("patient",""),
														innerObj.optString("category",""),
														innerObj.optString("start_time",""),
														innerObj.optString("end_time",""),
														strdate,
														imgUrl,

														innerObj.optString("gender", "")

														//innerObj.optString(strdate),
														//innerObj.optString(imgurl, "")
										));				
							}	
							
							System.out.println("MAIN ACTIVITY item Obj size > "+items.size());
						}	
					
						
						}catch(Exception e){
							e.printStackTrace();
						}
						System.out.println("MAIN ACTIVITY item Obj size2 > "+items.size());
						
						
						
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
						
						adapter = new EntryAdapter1(items);
//						CustomAdapter adapter = new CustomAdapter(mActivity);
						System.out.println("MAIN ACTIVITY item Obj adapter > "+adapter.getCount());
						if(adapter == null){
//							listview.setEmptyView(emptylayout);
						}else{
//						adapter.notifyDataSetChanged();
						listview.setAdapter(adapter);
						
						}
											
					}
					if(response[0].equalsIgnoreCase("400")){
//						System.out.println(LOG_TAG+" >> "+s_msg);
					}
				}	
		
			}
		} //End
	 
		
		@Override
		public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
			// TODO Auto-generated method stub
			super.onCreateOptionsMenu(menu, inflater);
		}
//AsyncTask  for delete
		
		class DeleteAppointmentListAsync extends AsyncTask<String, Void, String>{
			ProgressDialog pdialog = new ProgressDialog(mActivity);
			String[] response = new String[2];
			
			
			public DeleteAppointmentListAsync(){
				s_token = ApplicationActivity.getToken();
			}
			
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				pdialog = ProgressDialog.show(mActivity, "", "");
				pdialog.setContentView(R.layout.wheel);
				items.clear();
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
							
				String a_id = params[0];
				
				String url_patientlist = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_appointment_delete)+appid;
				System.out.println(LOG_TAG + "URL >> "+url_patientlist +" >> "+ a_id);
				response = HttpApiCalling.appointmentDelete(url_patientlist,ApplicationActivity.getToken(),appid);
				System.out.println("delete_list details" + response[0] + "   "
						+ response[1]);

				if(response[1] != null){
					if(response[0].equalsIgnoreCase("200")){
						try{
							//first array

//						JSONArray topArray = new JSONArray(response[1].toString());
//						System.out.println("MAIN ACTIVITY JSON Array"+topArray);
//						for(int i=0;i<topArray.length();i++){
//							//Create object
//							JSONObject outerObj = topArray.getJSONObject(i);
//							
//							String strdate = outerObj.optString("date","");
//							
//							
//							System.out.println("MAIN ACTIVITY Date > "+strdate);
//							
//							
//							items.add(new SectionItem(strdate));
//							JSONArray appointArray = outerObj.getJSONArray("appointments");
//							for(int j=0;j<appointArray.length();j++){
//							System.out.println("MAIN ACTIVITY appointArray > "+appointArray.length());
//								JSONObject innerObj = appointArray.getJSONObject(j);
//								
//								System.out.println("MAIN ACTIVITY innerObj > "+innerObj.toString());
//
//								
//								
//								items.add(new EntryItem(innerObj.optString("id",""),
//														innerObj.optString("patient",""),
//														innerObj.optString("category",""),
//														innerObj.optString("start_time",""),
//														innerObj.optString("end_time","")
//										
//										));				
//							}	
//							
//							System.out.println("MAIN ACTIVITY item Obj size > "+items.size());
//						}	
					
						
						}catch(Exception e){
							e.printStackTrace();
						}
//						System.out.println("MAIN ACTIVITY item Obj size2 > "+items.size());
			}//response is 200
			if(response[0].equalsIgnoreCase("400")){		
			
				try {
					JSONObject objResult = new JSONObject(response[1]);
					
						
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}//response is 400
			
			if(response[0].equalsIgnoreCase("204")){		
				
				try {
					
					//success
//					JSONObject objResult = new JSONObject(response[1]);
					
						
				} catch (Exception e) {
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
				
				
				if(response[0] != null){
					if(response[0].equalsIgnoreCase("200")){
						
								
					}
					if(response[0].equalsIgnoreCase("400")){
						System.out.println(LOG_TAG+" >> "+response[0]);
						
					}
					
					if(response[0].equalsIgnoreCase("204")){
						System.out.println(LOG_TAG+" >> "+response[0]);
						//success
//						adapter.notifyDataSetChanged();
//						listview.invalidate();
//						new AppointmentListAsync().execute();
						mActivity.pushFragments(AppConstants.TAB_A, new DeleteMessageFragment("Appointment deleted sucessfully"), true, true);
						
					}
					if(response[0].equalsIgnoreCase("401")){
						System.out.println(LOG_TAG+" >> "+response[0]);
//						Toast.makeText(getActivity(), "id Not found", Toast.LENGTH_LONG).show();
					}
					if(response[0].equalsIgnoreCase("404")){
						System.out.println(LOG_TAG+" >> "+response[0]);
						mActivity.pushFragments(AppConstants.TAB_A, new DeleteMessageFragment("Appointment not deleted, Please try again."), true, true);     
					}
					
					
				}	
		
			}
		} 
		
}


/*list all appointments
 * [
 
 {
     "date": "15-11-2013",
     "appointments": [
         {
             "id": 33,
             "patient": "Rohan Tare",
             "category": "Allery",
             "start_time": "07:00 AM",
             "end_time": "07:10 AM",
             "gender": "Male",
             "image": "boyimage.jpg"
         },
         {
             "id": 34,
             "patient": "Smita Kargaonkar",
             "category": "Allery",
             "start_time": "07:10 AM",
             "end_time": "07:20 AM",
             "gender": "Female",
             "image": "images/Penguins_1.jpg"
         },
         {
             "id": 35,
             "patient": "Yogesh Neman",
             "category": "Allery",
             "start_time": "07:20 AM",
             "end_time": "07:30 AM",
             "gender": "Male",
             "image": "boyimage.jpg"
         }
     ]
 }
]*/