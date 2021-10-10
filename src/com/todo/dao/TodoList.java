package com.todo.dao;

import java.util.*;

import com.todo.service.TodoSortByDate;
import com.todo.service.TodoSortByName;

public class TodoList {
	private List<TodoItem> list;

	public TodoList() {
		this.list = new ArrayList<TodoItem>();
	}
	
	public TodoItem accessItem(Integer a) {
		TodoItem t = list.get(a);
		return t;
	}

	public void addItem(TodoItem t) {
		list.add(t);
	}

	public void deleteItem(int t) {
		list.remove(t-1);
	}

	public ArrayList<TodoItem> getList() {
		return new ArrayList<TodoItem>(list);
	}

	public void sortByName() {
		Collections.sort(list, new TodoSortByName());
	}

	public void listAll() {
		System.out.println("\n"
				+ "inside list_All method\n");
		for (TodoItem myitem : list) {
			System.out.println(myitem.getTitle() + myitem.getDesc());
		}
	}
	
	public void reverseList() {
		Collections.reverse(list);
	}

	public void sortByDate() {
		Collections.sort(list, new TodoSortByDate());
	}

	public int indexOf(TodoItem t) {
		return list.indexOf(t);
	}

	public Boolean isThereNumber(Integer a) {	
		if (list.size()<a-1 || a<1) return false;
		return true;
	}
	public Boolean isDuplicateTitle(String title) {
		for (TodoItem item : list) {
			if (title.equals(item.getTitle())) return true;
		}
		return false;
	}
	public Boolean isDuplicateDesc(String desc) {
		for (TodoItem item : list) {
			if (desc.equals(item.getDesc())) return true;
		}
		return false;
	}
	public Integer getSize() {
		return list.size();
	}
}
