package com.example.uclinix;

public class PatientData {

	public final String id;
	public final String username;
	public final String email;
	public final String address;
	public final String landline;
	public final String mobile;
	public final String dob;
	public final String gender;
	public final String image;
	public final String patient_name;
	
	
//	{
//        "id": 14,
//        "username": "patient10",
//        "email": "rohan@fafadiatech.com",
//        "address": "asd",
//        "landline": "3210654987",
//        "mobile": "9632014587",
//        "dob": "1985-09-10",
//        "gender": "Male",
//        "image": "../images/18_2.jpg",
//        "patient_name": "R S"
//    }
	
	public PatientData(String id,String username,String email,String address,String landline,String mobile,String dob,String gender,String image,String patient_name){
		this.id= id;
		this.username = username;
		this.email = email;
		this.address = address;
		this.landline = landline;
		this.mobile = mobile;
		this.dob = dob;
		this.gender = gender;
		this.image= image;
		this.patient_name = patient_name;		
	}
	
	public PatientData(String id,String image,String patient_name,String gender){
		this.id= id;
		this.username = "";
		this.email = "";
		this.address = "";
		this.landline = "";
		this.mobile = "";
		this.dob = "";
		this.gender = gender;
		this.image= image;
		this.patient_name = patient_name;		
	}
	public String getPatientId(){
		return id;
	}	
	
	public String getPatientUsername(){
		return username;
	}
	
	public String getPatientEmail(){
		return email;
	}
	
	public String getPatientAddress(){
		return address;
	}
	
	public String getPatientLandline(){
		return landline;		
	}
	
	public String getPatientMobile(){
		return mobile;
	}
	
	public String getPatientDob(){
		return dob;
	}
	
	public String getPatientGender(){
		return gender;
	}
	
	public String getPatientImage(){
		return image;
	}
	
	public String getPatientName(){
		return patient_name;
	}
}
