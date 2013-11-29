package com.example.uclinix;

/**
 * Item definition including the section.
 */
public class SectionListItem {
    public Object item;
    public String name;
    public String category;
    public String time;
    public String section;

    public SectionListItem(final Object item,final String category, final String time,final String section) {
        super();
        this.item = item;
     
        this.category = category;
        this.time = time;
        this.section =section;
    }

    @Override
    public String toString() {
        return item.toString();
    }
    
   

}
