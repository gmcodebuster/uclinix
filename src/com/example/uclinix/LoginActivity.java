package com.example.uclinix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.provider.Settings.Secure;

public class LoginActivity extends FragmentActivity {
	EditText edtLogin,edtPassword;
	Button btnLogin;
	String deviceid,android_id;
	
	// flag for Internet connection status
		Boolean isInternetPresent = false;
		// Connection detector class
		ConnectionDetector cd;
	 Context ctx;
	final String LOG_TAG = "LoginActivity : ";
	private NetworkChangeReceiver receiver;
	
	AlertDialog alert;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		ctx = this;
		// creating connection detector class instance
		cd = ApplicationActivity.getConnDeteInstance(getApplicationContext());
		
		// get Internet status
		isInternetPresent = cd.isConnectingToInternet();
		
		IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
		  receiver = new NetworkChangeReceiver();
		  registerReceiver(receiver, filter);
		  
		  
		if(ApplicationActivity.isLogin()){
			Intent i1 = new Intent(LoginActivity.this,AppMainFragmentActivity.class);
			startActivity(i1);
			LoginActivity.this.finish();
		}
		
		
		setContentView(R.layout.activity_login);
		edtLogin = (EditText) findViewById(R.id.edtEmail);
		edtPassword = (EditText) findViewById(R.id.edtPassword);
		
		btnLogin = (Button) findViewById(R.id.btnLogin);
		
		
		try{
//		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//		deviceid = telephonyManager.getDeviceId();
		
		android_id = Secure.getString(getBaseContext().getContentResolver(),Secure.ANDROID_ID); 
		}catch(Exception e){
			deviceid = "1";
			android_id = ""+ System.currentTimeMillis();
		}
		
		System.out.println(LOG_TAG +"android_id >>"+android_id);
		
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent i1 = new Intent(LoginActivity.this,AppMainFragmentActivity.class);
//				startActivity(i1);
				System.out.println("email >> "+edtLogin.getText().toString()+" pass >>"+edtPassword.getText().toString());
				if(isInternetPresent){
					new LoginAsync().execute(edtLogin.getText().toString().trim(), edtPassword.getText().toString().trim());
					}else{
						ApplicationActivity.showAlertDialog(LoginActivity.this, "No Internet Connection",
								"You don't have internet connection.", false);
					}
				
				
			}
		});
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		// creating connection detector class instance
				cd = ApplicationActivity.getConnDeteInstance(getApplicationContext());
				
				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		// creating connection detector class instance
				cd = ApplicationActivity.getConnDeteInstance(getApplicationContext());
				
				// get Internet status
				isInternetPresent = cd.isConnectingToInternet();
	}
	
	private Context getDialogContext() {
		Context context;
		if (getParent() != null)
			context = getParent();
		else
			context = this;
		return context;
	}
	
	private void alertMessage(String arg1){ 
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
 
	
	class LoginAsync extends AsyncTask<String, Void, String>{
		ProgressDialog pdialog = new ProgressDialog(getDialogContext());
		String[] response = new String[2];
		String s_msg,s_token,susername,spassword,s_did,s_type;
		
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pdialog = ProgressDialog.show(ctx, "", "");
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
			
			susername = params[0];

			spassword = params[1];
			
			String url_login = getDialogContext().getResources().getString(R.string.url_base)+getDialogContext().getResources().getString(R.string.url_doctor_login);
			response = HttpApiCalling.LoginUser(url_login,susername, spassword,"android","qwertyuiopasdfgh");
			System.out.println("login user details" + response[0] + "   "
					+ response[1]);

			if(response[1] != null || response[1].equalsIgnoreCase(null)){
				if(response[0].equalsIgnoreCase("200")){
					try {
						JSONObject objResult = new JSONObject(response[1]);
						s_token = objResult.optString("token","");
						s_did = objResult.optString("id","");
						s_type = objResult.optString("Role","");

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}//response is 200
		if(response[0].equalsIgnoreCase("400")){		
		
			try {
				JSONObject objResult = new JSONObject(response[1]);
				JSONArray error_arr = objResult.getJSONArray("non_field_errors");
				
				s_msg = error_arr.optString(0);
				
		
		
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
			
			if(response[1] != null || response[1].equalsIgnoreCase(null)){
				if(response[0].equalsIgnoreCase("200")){
					System.out.println(LOG_TAG +" >> "+s_token +" >> id > "+s_did);
					ApplicationActivity.setLogin(true);
					ApplicationActivity.setToken("");
					ApplicationActivity.setToken(s_token);
					ApplicationActivity.setDoctorId(s_did);
					ApplicationActivity.setUserRole(s_type);
					Intent i1 = new Intent(LoginActivity.this,AppMainFragmentActivity.class);
					startActivity(i1);
					LoginActivity.this.finish();
				}
				if(response[0].equalsIgnoreCase("400")){
					System.out.println(LOG_TAG+" >> "+s_msg);
					
//					login user details400   {"non_field_errors": ["Unable to login with provided credentials."]}
//					show alert dialog
					alertMessage("Unable to login, The username or password you entered is incorrect..");
					
					
					
				}
			}	
	
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		 Log.v(LOG_TAG, "onDestory");
		super.onDestroy();
	
		unregisterReceiver(receiver);
	}
	
	public class NetworkChangeReceiver extends BroadcastReceiver {
		  
		  @Override
		  public void onReceive(final Context context, final Intent intent) {
		  
		   Log.v(LOG_TAG, "Receieved notification about network status");
		   isNetworkAvailable(context);
		  
		  }
		  
		  
		  private boolean isNetworkAvailable(Context context) {
		   ConnectivityManager connectivity = (ConnectivityManager)
		     context.getSystemService(Context.CONNECTIVITY_SERVICE);
		   if (connectivity != null) {
		    NetworkInfo[] info = connectivity.getAllNetworkInfo();
		    if (info != null) {
		     for (int i = 0; i < info.length; i++) {
		      if (info[i].getState() == NetworkInfo.State.CONNECTED) {
		       if(!isInternetPresent){
		        Log.v(LOG_TAG, "Now you are connected to Internet!");
//		        networkStatus.setText("Now you are connected to Internet!");
		        isInternetPresent = true;
		        //do your processing here ---
		        //if you need to post any data to the server or get status
		        //update from the server
		       }
		       return true;
		      }
		     }
		    }
		   }
		   Log.v(LOG_TAG, "You are not connected to Internet!");
//		   networkStatus.setText("You are not connected to Internet!");
		   isInternetPresent = false;
		   return false;
		  }
		 }
}
