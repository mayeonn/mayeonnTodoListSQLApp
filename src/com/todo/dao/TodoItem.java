package com.todo.dao;

import java.util.Date;

public class TodoItem {
	private int id;
    private String title;
    private String desc;
    private String current_date;
    private String category;
    private String due_date;
    private Integer is_complete;
    private Integer is_important;

    public TodoItem(String title, String desc, String cate, String date,int is_complete, int is_important){
        this.title=title;
        this.category=cate;
        this.desc=desc;
        this.due_date=date;
        this.current_date=new Date().toString();
        this.is_complete=is_complete;
        this.is_important=is_important;
    }
    
	public Integer getIs_complete() {
		return is_complete;
		}

	public void setIs_complete(Integer is_complete) {
		this.is_complete = is_complete;
	}
   

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	

	public TodoItem(String title,  String desc, String cate, String date, String currentdate, Integer is_completed) {
    	this.title=title;
        this.category=cate;
        this.desc=desc;
        this.due_date=date;
        this.current_date=currentdate;
        this.is_complete=is_completed;
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

	public String toString(int completed) {
		String comp="";	
		if(completed==1) {
			comp="[V]";
		}
		
		return "[" + category + "] " + title +  comp +" - " + desc + " - " + due_date + " - " + current_date;
				}
	
	public String toSaveString() {
		return category +"##"+ title +"##"+ desc +"##"+ due_date +"##"+ current_date ;
	}

	public Integer getIs_important() {
		return is_important;
	}

	public void setIs_important(Integer is_important) {
		this.is_important = is_important;
	}
    
}
