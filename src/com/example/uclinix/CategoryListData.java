package com.example.uclinix;

public class CategoryListData {
	public final String id;
	public final String category;
	
	public CategoryListData(String id,String category){
		this.id= id;
		this.category = category;
			
	}
	
	public String getCategoryId(){
		return id;
	}	
	
	public String getCategoryName(){
		return category;
	}
}
