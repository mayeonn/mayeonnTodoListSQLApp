package com.todo.service;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;

import com.todo.dao.TodoItem;
import com.todo.dao.TodoList;

public class TodoUtil {
	
	public static void createItem(TodoList list) {
		
		String title, cate, desc, date;
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n"
				+ "[항목 추가]\n"
				+ "제목 > ");
		
		title = sc.nextLine();
		if (list.isDuplicateTitle(title)) {
			System.out.println("-- 제목이 중복됩니다 --");
			return;
		}
		//sc.nextLine();
		System.out.print("카테고리 > ");
		cate = sc.nextLine().trim();
		
		System.out.print("내용 > ");
		desc = sc.nextLine().trim();
		
		System.out.print("마감일 > ");
		date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(title, cate, desc, date);
		list.addItem(t);
		System.out.println("-- 추가되었습니다 --");
	}
	
	public static void deleteItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		System.out.print("\n"
				+ "[항목 삭제]\n"
				+ "삭제할 항목의 번호를 입력하세요 > ");
		int index = sc.nextInt();
		l.deleteItem((index));
	}


	public static void updateItem(TodoList l) {
		
		Scanner sc = new Scanner(System.in);
		
		System.out.print("\n"
				+ "[항목 수정]\n"
				+ "수정할 항목의 번호을 입력하세요 > ");
		int index = sc.nextInt();
		if (!l.isThereNumber(index)) {
			System.out.println("-- 해당 항목이 없습니다 --");
			return;
		}
		
		sc.nextLine();
		System.out.print("새 제목 > ");
		String new_title = sc.nextLine().trim();
		if (l.isDuplicateTitle(new_title)) {
			System.out.println("-- 제목이 중복됩니다 --");
			return;
		}
		
		System.out.print("새 카테고리 > ");
		String new_cate = sc.nextLine().trim();
		
		System.out.print("새 내용 > ");
		String new_desc = sc.nextLine().trim();
		
		System.out.print("새 마감일 > ");
		String new_date = sc.nextLine().trim();
		
		TodoItem t = new TodoItem(new_title, new_cate, new_desc, new_date);
		l.deleteItem(index);
		l.addItem(t);
		
		System.out.println("-- 수정되었습니다 --");
	}

	public static void listAll(TodoList l) {
		int i=1;
		System.out.printf("[전체 목록, 총 %d개]\n", l.getSize());
		for (TodoItem item : l.getList()) {
			System.out.printf(i+". ");
			System.out.println(item.toString());
			i++;
		}
	}
	
	public static void listCate(TodoList l) {
		//key: category value: title
		HashMap<String,String> cate = new HashMap<String,String>();
		for(TodoItem item : l.getList()) {
			cate.put(item.getCategory(), item.getTitle());
		}
		
		Iterator <String> categories = cate.keySet().iterator(); 
		 do{
			String key = categories.next();
			System.out.print(key);
			if(categories.hasNext())
				System.out.print(" / ");
		}while(categories.hasNext());
		 
		 System.out.printf("\n총 %d개의 카테고리가 등록되어 있습니다.\n", cate.size());
	}
	
	public static void findItem(String keyWord, TodoList l) {
		//title, desc 검색
		Integer i=0, j=0;
		for(TodoItem t : l.getList()) {
			i++;
			if (t.isDuplicateTitle(keyWord) || t.isDuplicateDesc(keyWord)) {
				j++;
				System.out.printf(i+". ");
				System.out.println(t.toString());
			}
		}
		System.out.printf("총 %d개의 항목을 찾았습니다.\n", j);
	}
	
	public static void findCate(String keyWord, TodoList l) {
		Integer i=0, j=0;
		for(TodoItem t : l.getList()) {
			i++;
			if (t.isDuplicateCate(keyWord)) {
				j++;
				System.out.printf(i+". ");
				System.out.println(t.toString());
			}
		}
		System.out.printf("총 %d개의 항목을 찾았습니다.\n", j);
	}
	
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
				
				TodoItem t = new TodoItem(title, cate, desc, due_date, current_date);
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
