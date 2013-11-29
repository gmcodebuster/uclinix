package com.example.uclinix;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;

import com.example.multipart.MultipartEntity;
import com.example.multipart.content.FileBody;
import com.example.multipart.content.StringBody;

import android.util.Log;

public class HttpApiCalling {

	private static DefaultHttpClient client;
	/*
	 * public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
	 * .compile(".+@.+\\.[a-z]+");
	 */

	public final static Pattern EMAIL_ADDRESS_PATTERN = Pattern
			.compile("[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");

	static boolean checkEmail(String email) {
		return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
	}

	static boolean checkPostCode(String postcode) {
		return POSTCODE_PATTERN.matcher(postcode).matches();
	}

	public final static Pattern POSTCODE_PATTERN = Pattern
			.compile("[A-Z]{1,2}[0-9R][0-9A-Z]? [0-9][ABD-HJLNP-UW-Z]{2}");

	public synchronized static DefaultHttpClient getThreadSafeClient() {

		if (client != null)
			return client;

		client = new DefaultHttpClient();

		ClientConnectionManager mgr = client.getConnectionManager();

		HttpParams params = client.getParams();
		client = new DefaultHttpClient(new ThreadSafeClientConnManager(params,
				mgr.getSchemeRegistry()), params);

		return client;
	}

	private static StringBuilder inputStreamToString(InputStream is) {

		String line = "";
		StringBuilder total = new StringBuilder();
		try {
			// Wrap a BufferedReader around the InputStream
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					"UTF-8"), (100 * 1024));// ,(100 * 1024)

			// Read response until the end

			while ((line = rd.readLine()) != null) {
				total.append(line);
			}
			rd.close();

			Log.d("Inside i.s.>>", total.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return total;
	}
	
	
	
	public static String[] LoginUser(String url,String username, String password,String devType,String devId) {

		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(url);
		System.out.println("Login url >>"+url);

		try {
			// Add your data
//Use Form data otherwise it gives http 400 error
//			if any of the field value is wrong then you will get following error
//			{
//			    "non_field_errors": [
//			        "Unable to login with provided credentials."
//			    ]
//			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("username",username));
			nameValuePairs.add(new BasicNameValuePair("password",password));
			nameValuePairs.add(new BasicNameValuePair("device_type", devType));
			nameValuePairs.add(new BasicNameValuePair("device_id", devId));
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			client = getThreadSafeClient();
			response = client.execute(httppost);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;

	}
	
	
//	post.setHeader("Authorization",getB64Auth());
	public static String[] getPatentList(String url,String token) {	
		
		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Token "+token);
		

		try {

			client = getThreadSafeClient();
			response = client.execute(httpget);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;

	}

	
	public static String[] appointmentList(String url,String token) {

//		String url = "http://192.168.2.28:8000/api2/appointment_list/";
		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Token "+token);
		

		try {

			client = getThreadSafeClient();
			response = client.execute(httpget);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;

	}
	
	
	public static String[] getcategoryList(String url,String token) {	
		
		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		HttpGet httpget = new HttpGet(url);
		httpget.setHeader("Authorization", "Token "+token);
		

		try {

			client = getThreadSafeClient();
			response = client.execute(httpget);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;

	}

//Add Appointment
//	public static String[] AddAppointment(String url,String token,String patient, String category,String starttime,String endtime,String paid,String paidon,String doctor) {
	public static String[] AddAppointment(String url,String token,String patient, String category,String starttime,String endtime,String doctor) {

		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Authorization", "Token "+token);
		
		System.out.println("AddAppointment url >>"+url);

		try {
			// Add your data
//Use Form data otherwise it gives http 400 error
//			if any of the field value is wrong then you will get following error
//			{
//			    "non_field_errors": [
//			        "Unable to login with provided credentials."
//			    ]
//			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);
			nameValuePairs.add(new BasicNameValuePair("patient",patient));
			nameValuePairs.add(new BasicNameValuePair("category",category));
			nameValuePairs.add(new BasicNameValuePair("start_time", starttime));
			nameValuePairs.add(new BasicNameValuePair("end_time", endtime));
//			nameValuePairs.add(new BasicNameValuePair("paid", paid));
//			nameValuePairs.add(new BasicNameValuePair("paidon", paidon));
			nameValuePairs.add(new BasicNameValuePair("doctor", doctor));
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			client = getThreadSafeClient();
			response = client.execute(httppost);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;
		
	}
	
	
	//Edit Appointment
//	public static String[] AddAppointment(String url,String token,String patient, String category,String starttime,String endtime,String paid,String paidon,String doctor) {
	public static String[] EditAppointment(String url,String token,String patient,String doctor, String category,String starttime,String endtime,String app_id) {

		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		HttpPut httppost = new HttpPut(url);
		httppost.setHeader("Authorization", "Token "+token);
		
		System.out.println("AddAppointment url >>"+url);

		try {
			// Add your data
//Use Form data otherwise it gives http 400 error
//			if any of the field value is wrong then you will get following error
//			{
//			    "non_field_errors": [
//			        "Unable to login with provided credentials."
//			    ]
//			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(6);
			nameValuePairs.add(new BasicNameValuePair("patient",patient));
			nameValuePairs.add(new BasicNameValuePair("doctor", doctor));
			nameValuePairs.add(new BasicNameValuePair("category",category));
			nameValuePairs.add(new BasicNameValuePair("start_time", starttime));
			nameValuePairs.add(new BasicNameValuePair("end_time", endtime));
			nameValuePairs.add(new BasicNameValuePair("id", app_id));
//			nameValuePairs.add(new BasicNameValuePair("paidon", paidon));
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			client = getThreadSafeClient();
			response = client.execute(httppost);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;
		
	}

	public static String[] appointmentDelete(String url,String token,String appid) {

//		String url = "http://192.168.2.28:8000/api2/appointment_delete/";
		
		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		System.out.println("APP _ ID >>> "+appid);
		
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		HttpDelete httpdelete = new HttpDelete(url);
		httpdelete.setHeader("Authorization", "Token "+token);
		

		try {

			client = getThreadSafeClient();
			response = client.execute(httpdelete);

			res_code = response.getStatusLine().getStatusCode();

//			response_string[1] = inputStreamToString(
//					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;

	}
	
	

	// =================== Profile Upload post method
	// =======================================================
	public static String[] AddNewPatient(String url, String token,String username,String pass,
											String f_name, String l_name, String email, String addr,
													String l_line, String m_no, String dob, String gender,
														 String attachment) {
		int res_code;
		File file = null;
		String[] response_string = new String[2];
		HttpResponse response;
		// Create a new HttpClient and Post Header
		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);
		//
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Authorization", "Token "+token);
		
		System.out.println("File address >> " + attachment);
		if (attachment == null) {

		} else {

			file = new File(attachment);// "/sdcard/ed4b42187205df405ddd7953d2356648.jpg"
			String fname = file.getAbsoluteFile().getName().toString();

			System.out.println("File path >>" + file.getAbsoluteFile());
			System.out.println("File name >>" + fname);
		}

		MultipartEntity mpEntity = new MultipartEntity();
		/*
		 * ContentBody cbFile = new FileBody(file, "image/jpeg");
		 * 
		 * FileBody cbFile1 = new FileBody(file, "image/jpeg");
		 * mpEntity.addPart("attachment", cbFile);
		 */

		// mp.addPart("business_photo", new FileBody(new File(fn),
		// "image/jpg"));

		// FilePart photo = new FilePart("icon", new ByteArrayPartSource(
		// filename, imageData));
		// File file= new
		// File(Environment.getExternalStorageDirectory().getPath()+"/ed4b42187205df405ddd7953d2356648.jpg");//file
		// path
		// File file= new File(attachment);
		// FileBody bin = new FileBody(file);

		try {

			mpEntity.addPart("username", new StringBody(username));
			mpEntity.addPart("first_name", new StringBody(f_name));
			mpEntity.addPart("last_name", new StringBody(l_name));
			mpEntity.addPart("email", new StringBody(email));
			mpEntity.addPart("address", new StringBody(addr));

			mpEntity.addPart("landline", new StringBody(l_line));
			mpEntity.addPart("mobile", new StringBody(m_no));
			mpEntity.addPart("dob", new StringBody(dob));
			mpEntity.addPart("gender", new StringBody(gender));
			mpEntity.addPart("password", new StringBody(pass));

			if (file != null) {
				mpEntity.addPart("image", new FileBody(file, "image/jpg"));
			}

			// Execute HTTP Post Request
			client = getThreadSafeClient();

			httppost.setEntity(mpEntity);
			// httppost.setEntity(reqEntity);

			response = client.execute(httppost);

			res_code = response.getStatusLine().getStatusCode();

			Log.v("539 Status Line ", response.getStatusLine().toString());
			Log.d("540 res_code", "res_code " + res_code);
			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

			HttpEntity responseEntity = response.getEntity();

			Log.v("Response Entity", responseEntity.toString());
			Log.v("Response Code>>", response_string[0]);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.v("Exception", "IO exception" + e);
		}

		/*
		 * try { // Add your data List<NameValuePair> nameValuePairs = new
		 * ArrayList<NameValuePair>(4); nameValuePairs.add(new
		 * BasicNameValuePair
		 * ("token",token));//"b6a98dc3941ea41b1ed2429802029765"
		 * nameValuePairs.add(new BasicNameValuePair("secret",secret));//
		 * "ce805254-1f34-11e2-8d12-12313d338045"
		 * 
		 * 
		 * nameValuePairs.add(new BasicNameValuePair("profile_id",profile_id));
		 * nameValuePairs.add(new BasicNameValuePair("name",name));
		 * nameValuePairs.add(new BasicNameValuePair("bio",bio));
		 * 
		 * nameValuePairs.add(new BasicNameValuePair("email",email));
		 * nameValuePairs.add(new BasicNameValuePair("location",location));
		 * nameValuePairs.add(new BasicNameValuePair("gender",gender));
		 * 
		 * nameValuePairs.add(new BasicNameValuePair("dob",dob));
		 * nameValuePairs.add(new
		 * BasicNameValuePair("home_zipcode",home_zipcode));
		 * nameValuePairs.add(new BasicNameValuePair("attachment",attachment));
		 * 
		 * httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		 * 
		 * httppost.setHeader("Accept", "
		 *//* "); *//*
					 * remove comment from here
					 * httppost.setHeader("Content-type",
					 * "text/html");//application/json
					 * 
					 * // Execute HTTP Post Request
					 * 
					 * client = getThreadSafeClient(); response =
					 * client.execute(httppost);
					 * 
					 * res_code = response.getStatusLine().getStatusCode();
					 * Log.v("539 Status Line ",
					 * response.getStatusLine().toString());
					 * Log.d("540 res_code","res_code "+res_code);
					 * response_string[1] =
					 * inputStreamToString(response.getEntity
					 * ().getContent()).toString(); response_string[0] =
					 * ""+res_code;
					 * 
					 * 
					 * 
					 * HttpEntity responseEntity = response.getEntity();
					 * Log.v("Response Entity", responseEntity.toString());
					 * Log.v("Response Code>>", response_string[0]);
					 * 
					 * 
					 * } catch (Exception e) { Log.v("Exception",
					 * "IO exception"+e);
					 * 
					 * }
					 */

		return response_string;
	}
	
	
	//Edit Patient
//	
	public static String[] EditPatient(String url,String token,String username,String first_name, String last_name,String email,String address,String landline,String mobile,String dob,String gender,String id) {

		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		HttpPut httppost = new HttpPut(url);
		httppost.setHeader("Authorization", "Token "+token);
		
		System.out.println("AddAppointment url >>"+url);

		try {
			// Add your data
//Use Form data otherwise it gives http 400 error
//			if any of the field value is wrong then you will get following error
//			{
//			    "non_field_errors": [
//			        "Unable to login with provided credentials."
//			    ]
//			}
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(10);
			nameValuePairs.add(new BasicNameValuePair("username",username));
			nameValuePairs.add(new BasicNameValuePair("first_name", first_name));
			nameValuePairs.add(new BasicNameValuePair("last_name",last_name));
			nameValuePairs.add(new BasicNameValuePair("email", email));
			nameValuePairs.add(new BasicNameValuePair("address", address));
			nameValuePairs.add(new BasicNameValuePair("landline", landline));
			nameValuePairs.add(new BasicNameValuePair("mobile", mobile));
			nameValuePairs.add(new BasicNameValuePair("dob", dob));
			nameValuePairs.add(new BasicNameValuePair("gender", gender));
			nameValuePairs.add(new BasicNameValuePair("id", id));
			
			
			
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			client = getThreadSafeClient();
			response = client.execute(httppost);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;
		
	}
	
	
	//Change password
//	
	public static String[] chngPass(String url,String token,String oldpass,String newpass) {

		int res_code;
		String[] response_string = new String[2];
		HttpResponse response;

		HttpParams params = new BasicHttpParams();
		params.setParameter(CoreProtocolPNames.PROTOCOL_VERSION,
				HttpVersion.HTTP_1_1);

		HttpPut httpput = new HttpPut(url);
		httpput.setHeader("Authorization", "Token "+token);
		
		System.out.println("Change password url >>"+url);

		try {
			
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("new_password", newpass));
			nameValuePairs.add(new BasicNameValuePair("old_password",oldpass));
				
			
			httpput.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			// Execute HTTP Post Request

			client = getThreadSafeClient();
			response = client.execute(httpput);

			res_code = response.getStatusLine().getStatusCode();

			response_string[1] = inputStreamToString(
					response.getEntity().getContent()).toString();
			response_string[0] = "" + res_code;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response_string;
		
	}
	
	
/*	HttpParams httpParameters = new BasicHttpParams();
	HttpConnectionParams.setConnectionTimeout(httpParameters, CONNECTION_TIMEOUT);
	HttpConnectionParams.setSoTimeout(httpParameters, WAIT_RESPONSE_TIMEOUT);
	HttpConnectionParams.setTcpNoDelay(httpParameters, true);

	client = new DefaultHttpClient(httpParameters);*/


}
