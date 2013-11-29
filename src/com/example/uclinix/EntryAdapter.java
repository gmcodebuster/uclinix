package com.example.uclinix;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

//public class EntryAdapter extends ArrayAdapter<Item>{
//
//	private Context cont;
//	private ArrayList<Item> items;
//	private LayoutInflater vi;
//	
//	public EntryAdapter(Context context,ArrayList<Item> item){
//		super(context,0,item);
//		this.cont = context;
//		this.items = item;
//		
//		vi = (LayoutInflater) cont.getSystemService(context.LAYOUT_INFLATER_SERVICE);
//	}
//	
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		View v= convertView;
//		final Item i = items.get(position);
//		if(i!=null){
//			if(i.isSection()){
//				SectionItem si = (SectionItem)i;
//				v = vi.inflate(R.layout.list_item_section, null);
//				v.setOnClickListener(null);
//			    v.setOnLongClickListener(null);
//			    v.setLongClickable(false);
//			     
//			    final TextView sectionView = (TextView) v.findViewById(R.id.list_item_section_text);
//			    sectionView.setText(si.getTitle());
//				
//			}else{
//				EntryItem ei = (EntryItem)i;
//				 v = vi.inflate(R.layout.list_item_entry, null);
//				    final TextView title = (TextView)v.findViewById(R.id.list_item_entry_title);
//				    final TextView subtitle = (TextView)v.findViewById(R.id.list_item_entry_summary);
//				     
//				     
//				    if (title != null) 
//				     title.setText(ei.id);//
//				    if(subtitle != null)//subtitle
//				     subtitle.setText(ei.getPatient());//subtitle
//			}
//		}
//		
//		return v;
//	}
//
//}
