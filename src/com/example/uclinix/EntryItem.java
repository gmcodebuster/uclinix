package com.example.uclinix;

public class EntryItem implements Item{

	
//	public final String title;
//	public final String subtitle;
	public final String category;
	public final String starttime;
	public final String endtime;
	public final String id;
	public final String patient;
	public final String stdate;
	public final String imgUrl;
	public final String gender;
	
//	public EntryItem(String title,String subtitle){
//	
//		this.title = title;
//		this.subtitle = subtitle;
//	}
	
	public EntryItem(String id,String patient,String category,String starttime,String endtime,String stdate,String imgUrl,String gender){
		
		this.id = id;
		this.patient = patient;
		this.category = category;		
		this.starttime = starttime;
		this.endtime = endtime;
		this.stdate = stdate;
		this.imgUrl = imgUrl;
		this.gender = gender;

	}
	
	@Override
	public boolean isSection() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public String getPatient(){
		return patient;
	}
	
	public String getCategory(){
		return category;
	}

	public String getStarttime(){
		return starttime;
	}
	
	public String getEndtime(){
		return endtime;
	}
	
	public String getStdate(){
		return stdate;
	}
	
	public String getimgUrl(){
		return imgUrl;
	}
	
	public String getGender(){
		return gender;
	}
}
