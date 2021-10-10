package com.todo.dao;

import java.util.Date;

public class TodoItem {
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;


    public TodoItem(String title, String cate, String desc, String date){
        this.title=title;
        this.category=cate;
        this.desc=desc;
        this.due_date=date;
        this.current_date=new Date().toString();
        
    }
    
    public TodoItem(String title, String cate, String desc, String date, String currentdate) {
    	this.title=title;
        this.category=cate;
        this.desc=desc;
        this.due_date=date;
        this.current_date=currentdate;
    }
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getCurrent_date() {
        return current_date;
    }

    public void setCurrent_date(String current_date) {
        this.current_date = current_date;
    }

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDue_date() {
		return due_date;
	}

	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	
	public Boolean isDuplicateTitle(String t) {
		if (this.title.contains(t)) return true;
		else return false;
	}
	
	public Boolean isDuplicateDesc(String d) {
		if (this.desc.contains(d)) return true;
		else return false;
	}
	
	public Boolean isDuplicateCate(String c) {
		if (this.category.contains(c)) return true;
		else return false;
	}

	@Override
	public String toString() {
		return "[" + category + "] " + title +  " - " + desc + " - " + due_date + " - " + current_date;
				}
	
	public String toSaveString() {
		return category +"##"+ title +"##"+ desc +"##"+ due_date +"##"+ current_date ;
	}
    
}
