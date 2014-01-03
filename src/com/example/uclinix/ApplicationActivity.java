package com.example.uclinix;

import java.io.File;

import org.acra.annotation.ReportsCrashes;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap.CompressFormat;
import android.location.LocationManager;
import android.os.Build;
import android.os.StrictMode;
import android.util.Config;
import android.widget.Toast;
import org.acra.*;
import org.acra.annotation.*;

@ReportsCrashes(
	    formKey = "", //required for backward compatibility but not used
	    mailTo = "gaurav.mehta@fafadiatech.com", //in case of sending Crash to an email address, by default it's blank implicitly  
	    logcatArguments = { "-t", "10000", "-v", "long", "ActivityManager:I", "MyApp:D", "*:S" },	    		
	    /*customize data to send in crash report */
	    customReportContent = {
	    ReportField.APP_VERSION_CODE,
	    ReportField.APP_VERSION_NAME,
	    ReportField.ANDROID_VERSION,
	    ReportField.PHONE_MODEL,
	    ReportField.CUSTOM_DATA,
	    ReportField.STACK_TRACE,
	    ReportField.LOGCAT }, 
	    mode = ReportingInteractionMode.TOAST,
	    resToastText = R.string.crash_toast_text,
	    formUri = "http://www.backendofyourchoice.com/reportpath")   //put backend path on which you want to send crash report
public class ApplicationActivity extends Application {
	
	
	Context ctx;	
	private final String SAMPLE_DB_NAME = "TheftData";
	private final String SAMPLE_TABLE_NAME = "UserDetails";
	private final String LOGIN_TABLE = "login";
	
	String createLogin = "CREATE TABLE IF NOT EXISTS " + LOGIN_TABLE + " (id INTEGER, login TEXT );";
	public static final String PREFERENCE_LOGIN = "MyLoginPreferences";
	
	private static ConnectionDetector cd = null;
	
	private static String strToken = null;
	private static String strDoctorId = null;
	private static String strUserRole = null;
	
	private static boolean isLogin = false;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	@SuppressWarnings("unused")	
	@Override
	public void onCreate() {
		
		/*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
			StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
		}*/
				
		// The following line triggers the initialization of ACRA
        ACRA.init(this);
        
		super.onCreate();
		
		ctx = this;	
		initImageLoader(getApplicationContext());
		SharedPreferences LoginSettings = getApplicationContext().getSharedPreferences(PREFERENCE_LOGIN, MODE_PRIVATE);  
		SharedPreferences.Editor prefEditor = LoginSettings.edit();  
		prefEditor.putString("UserName", " ");  
		prefEditor.putBoolean("isLogin", false);
		
		prefEditor.putString("longitude", "");
		prefEditor.putString("latitude", "");
		
		prefEditor.commit();  
		
	}
	
	// creating objects of connectionDetector
		 public static ConnectionDetector getConnDeteInstance(Context ct) {
		        if (cd == null) {
		        	cd = new ConnectionDetector(ct);
		        }
		        return cd;
		    }
		 
		 /**
			 * Function to display simple Alert Dialog
			 * @param context - application context
			 * @param title - alert dialog title
			 * @param message - alert message
			 * @param status - success/failure (used to set icon)
			 * */
			@SuppressWarnings("deprecation")
			public static void showAlertDialog(Context context, String title, String message, Boolean status) {
				AlertDialog alertDialog = new AlertDialog.Builder(context).create();

				// Setting Dialog Title
				alertDialog.setTitle(title);

				// Setting Dialog Message
				alertDialog.setMessage(message);
				
				// Setting alert dialog icon
				alertDialog.setIcon((status) ? R.drawable.success : R.drawable.fail);

				// Setting OK Button
				alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
					}
				});

				// Showing Alert Message
				alertDialog.show();
			}
			
			
			public static String getToken(){
				return strToken;
			}
			
			public static void setToken(String token){
				strToken = token;
			}	
			
			public static String getDoctorId(){
				return strDoctorId;
			}
			
			public static void setDoctorId(String doctorid){
				strDoctorId = doctorid;
			}			
			
			public static String getUserRole(){
				return strUserRole;
			}
			
			public static void setUserRole(String userrole){
				strUserRole = userrole;
			}		
			
			public static boolean isLogin(){
				return isLogin;
			}
			
			public static void setLogin(boolean login){
				isLogin = login;
			}	
			
			public static void initImageLoader(Context context) {
				// This configuration tuning is custom. You can tune every option, you may tune some of them,
				// or you can create default configuration by
				//  ImageLoaderConfiguration.createDefault(this);
				// method.
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
						.threadPriority(Thread.NORM_PRIORITY - 2)
						.denyCacheImageMultipleSizesInMemory()
						.discCacheFileNameGenerator(new Md5FileNameGenerator())
						.tasksProcessingOrder(QueueProcessingType.LIFO)
						.writeDebugLogs() // Remove for release app
						.build();
				// Initialize ImageLoader with configuration.
				ImageLoader.getInstance().init(config);
				
				
				
			}
			
			
}
