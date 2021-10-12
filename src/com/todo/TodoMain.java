package com.todo;

import java.util.Scanner;

import com.todo.dao.TodoList;
import com.todo.menu.Menu;
import com.todo.service.TodoUtil;

public class TodoMain {
	
	public static void start() { 
	
		Scanner sc = new Scanner(System.in);
		TodoList l = new TodoList();
		String searchKeyword="";
		//boolean isList = false;
		boolean quit = false;
		
		TodoUtil.loadList(l, "todolist.txt");
		Menu.displaymenu();
		do {
			Menu.prompt();
			//isList = false;
			String choice = sc.nextLine();
			if(choice.contains("find")) {
				Integer index = choice.indexOf(" ");
				searchKeyword = choice.substring(index+1);
				
				if(choice.contains("cate")) choice = "find_cate";
				else choice = "find";
			}
			
			switch (choice) {

			case "add":
				TodoUtil.createItem(l);
				break;
			
			case "del":
				TodoUtil.deleteItem(l);
				break;
				
			case "edit":
				TodoUtil.updateItem(l);
				break;
				
			case "ls":
				TodoUtil.listAll(l,"current_date",1);
				break;
				
			case "ls_cate":
				TodoUtil.listCate(l);
				break;

			case "ls_name_asc":
				System.out.println("-- 제목순으로 정렬하였습니다 --");
				TodoUtil.listAll(l, "title", 1);
				break;

			case "ls_name_desc":
				TodoUtil.listAll(l, "title", 0);
				System.out.println("-- 제목역순으로 정렬하였습니다 --");
				break;
				
			case "ls_date":
				TodoUtil.listAll(l, "due_date", 1);
				System.out.println("-- 날짜순으로 정렬하였습니다 --");
				break;
				
			case "ls_date_desc":
				TodoUtil.listAll(l, "due_date", 0);
				System.out.println("-- 날짜역순으로 정렬하였습니다 --");
				break;
				
			case "find":
				TodoUtil.findItem(searchKeyword, l);
				break;
				
			case "find_cate":
				TodoUtil.findCate(searchKeyword, l);
				break;
			
			case "help":
				Menu.displaymenu();
				break;
				
			case "exit":
				quit = true;
				System.out.println("-- 종료됩니다 --");
				break;
			
			
				
			default:
				System.out.println("-- 명령어가 정확하지 않습니다 (도움: help) --");
				break;
			}
			
			//if(isList) TodoUtil.listAll(l);
		} while (!quit);
		
		
	}
}
