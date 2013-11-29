package com.example.uclinix;

import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class ChangePasswordFragment extends BaseFragment {
	EditText edt_oldpass,edt_newpass;
	Button btnChng,btnCan;
	
	AlertDialog alert;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.frag_changepassword, container, false);
		
		((AppMainFragmentActivity) getActivity()).setActionBarTitle("Change Password");
		edt_oldpass = (EditText) view.findViewById(R.id.editOldPass);
		edt_newpass = (EditText) view.findViewById(R.id.editNewPass);		
		
		btnChng =  (Button) view.findViewById(R.id.btnChange);
		btnCan =  (Button) view.findViewById(R.id.btnCan);
		
		btnChng.setOnClickListener(change_listener);
		btnCan.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((AppMainFragmentActivity) getActivity()).popFragments();
			}
		});
		
		return view;
	}//End of onCreate View
	

	
	private OnClickListener change_listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			System.out.println("edt_oldpass >> "+edt_oldpass.getText().toString());
			System.out.println("edt_newpass >> "+edt_newpass.getText().toString());
			
						
			if (edt_oldpass.getText().toString().equals("")
					|| edt_oldpass.getText().toString().equals("null")) {
				
				edt_oldpass.setError("Please enter username");
				return;
			}
			if (edt_newpass.getText().toString().equals("")
					|| edt_newpass.getText().toString().equals("null")) {
				
				edt_newpass.setError("Please enter username");
				return;
			}
			
			//Call Asynk task
		
				
				
				String chngpass_url = mActivity.getResources().getString(R.string.url_base)+mActivity.getResources().getString(R.string.url_chngpassword);
				try{
				new ChangeAsyncTak().execute(chngpass_url,
											ApplicationActivity.getToken(),
											edt_oldpass.getText().toString(),
											edt_newpass.getText().toString()
											);
				}catch(Exception e){
					alertMessage("Password does not changed.");
				}
			
			
		}
	};
	
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
 
	
	
	private class ChangeAsyncTak extends AsyncTask<String, Void, String>{
		ProgressDialog pdialog = new ProgressDialog(mActivity);
		String[] response = new String[2];
		String msg; 
		int status;
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
			
			String chp_url = params[0];
			String token = params[1];
			String oldpass = params[2];
			String newpass =params[3];
			
			System.out.println("Change password "+ "URL >> "+params[0]);
			response = HttpApiCalling.chngPass(chp_url,token,oldpass,newpass);
			System.out.println("change password " + response[0] + "   "
					+ response[1]);
			
			if(response[1] != null){
				if(response[0].equalsIgnoreCase("200")){
			try{
			JSONObject objResult = new JSONObject(response[1]);
			System.out.println("Change PAssword >> "+objResult.length());
			
			JSONObject res_obj = objResult.getJSONObject("response");
			status = res_obj.optInt("status",0);
			msg = res_obj.optString("msg", "");
			
			System.out.println("Change PAssword status>> "+status);
			
			System.out.println("Change PAssword msg>> "+msg);
			
			}catch(Exception e){
				
				e.printStackTrace();
			}
			
		}//End of 200
				
				
				
		}//Emd of null
			return null;
		}
		
		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		

			if (pdialog.isShowing()) {
				pdialog.dismiss();
			
				
				
				
				if(response[1] != null){
					if(response[0].equalsIgnoreCase("200")){
						if(status == 0){
						//failure
							
							System.out.println("Invalid old password");
							alertMessage("Password does not changed.");
						
						}else if(status == 1){
							//sucess
							System.out.println("Password change successfully");
							alertMessage("Password change successfully.");
							
							((AppMainFragmentActivity) getActivity()).popFragments();
						}
						 
						 
					}
					if(response[0].equalsIgnoreCase("400")){
//						System.out.println(LOG_TAG+" >> "+s_msg);
						
					}
				}	
			}
		}
	}
	
}//End of class


/*{
    "response": {
        "status": 1,
        "msg": "Password change successfully"
    }
}



{
    "response": {
        "status": 0,
        "msg": "Invalid old password"
    }
}*/