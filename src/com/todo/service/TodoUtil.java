package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList l) {
		
		String title, category, desc, due_date;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n"
				+ "[항목 추가]\n"
				+ "제목 > ");
		
		title = sc.nextLine();
		
		if (l.isDuplicate(title)) {
			System.out.println("-- 제목이 중복됩니다 --");
			return;
		}
		
		System.out.print("카테고리 > ");
		category = sc.nextLine().trim();
		
		System.out.print("내용 > ");
		desc = sc.nextLine().trim();
		
		System.out.print("마감일 > ");
		due_date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(title, desc, category, due_date,0);
		if(l.addItem(t)>0) System.out.println("-- 추가되었습니다 --");
	}
	
	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("\n"
				+ "[항목 삭제]\n"
				+ "삭제할 항목의 번호를 입력하세요 > ");
		int index = sc.nextInt();
		if(l.deleteItem(index)>0) System.out.println("-- 삭제되었습니다 --");
	}


	public static void updateItem(TodoList l) {
		
		String new_title, new_desc, new_category, new_due_date;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n"
				+ "[항목 수정]\n"
				+ "수정할 항목의 번호을 입력하세요 > ");
		int index = sc.nextInt();
		/*
		if (!l.isThereNumber(index)) {
			System.out.println("-- 해당 항목이 없습니다 --");
			return;
		}
		*/
		sc.nextLine();
		System.out.print("새 제목 > ");
		new_title = sc.nextLine().trim();
		/*
		String new_title = sc.nextLine().trim();
		if (l.isDuplicateTitle(new_title)) {
			System.out.println("-- 제목이 중복됩니다 --");
			return;
		}
		*/
		System.out.print("새 카테고리 > ");
		new_category = sc.nextLine().trim();
		
		System.out.print("새 내용 > ");
		new_desc = sc.nextLine().trim();
		
		System.out.print("새 마감일 > ");
		new_due_date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(new_title, new_desc, new_category, new_due_date,0);
		t.setId(index);
		if(l.updateItem(t)>0) System.out.println("-- 수정되었습니다 --");
	}

	public static void compItem(TodoList l, int index) {
		
		if(l.completeItem(index)>0) System.out.println("-- 완료 체크하였습니다. --");
	}
	
	public static void findList(TodoList l, String keyword) {
		int count=0;
		for(TodoItem item: l.getList(keyword)) {
			System.out.println(item.toString(item.getIs_complete()));
			count++;
		}
		System.out.printf("-- 총 %d개의 항목을 찾았습니다. --\n", count);
	}
	
	public static void listAll(TodoList l, String orderby, int ordering) {
		System.out.printf("[전체 목록, 총 %d개]\n", l.getCount());
		for (TodoItem item : l.getOrderedList(orderby, ordering)) {
			
			System.out.println(item.toString(item.getIs_complete()));
		}
	}
	
	public static void listComp(TodoList l) {
		int count = 0;
		for (TodoItem item : l.getList()) {
			if(item.getIs_complete()==1) {
				count++;
				System.out.println(item.toString(1));
			}
		}
		 System.out.printf("\n총 %d개의 항목이 완료되었습니다.\n",count);
		
	}
	
	public static void listCate(TodoList l) {
		int count = 0;
		for(String item : l.getCategories()) {
			System.out.print(item +" ");
			count++;
			
		}
		 System.out.printf("\n총 %d개의 카테고리가 등록되어 있습니다.\n",count);
	}
	
	public static void findItem(String keyWord, TodoList l) {
		//title, desc 검색
		int count=0;
		for(TodoItem t : l.getList(keyWord)) {
			System.out.println(t.toString());
			count++;
		}
		System.out.printf("총 %d개의 항목을 찾았습니다.\n", count);
	}
	
	public static void findCate(String keyWord, TodoList l) { 
		int count=0;
		for(TodoItem item : l.getListCategory(keyWord)) {
			System.out.println(item.toString(item.getIs_complete()));
			count++;
		}
		System.out.printf("총 %d개의 항목을 찾았습니다.\n", count);
	}
/*	
	public static void saveList(TodoList l, String filename) {
		try {
			Writer w = new FileWriter("todolist.txt");
			for(TodoItem item : l.getList()) {
				w.write(item.toSaveString()+"\n");
			}
			w.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
*/		
	public static void loadList(TodoList l, String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("todolist.txt"));
			String oneline;
			while((oneline = br.readLine())!=null) {
				StringTokenizer st = new StringTokenizer(oneline, "##");
				String cate = st.nextToken();
				String title = st.nextToken();
				String desc = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				TodoItem t = new TodoItem(title, cate, desc, due_date, current_date,0);
				l.addItem(t);
			}
			br.close();
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		catch (NoSuchElementException e) {
		}
	}
	
}
