package com.todo.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

import com.todo.service.DbConnect;

public class TodoList {
	Connection conn;
	//private List<TodoItem> list;
	
	public void importData(String filename) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String line;
			String sql = "insert into list (title, memo, category, current_date, due_date)"
					+" values (?,?,?,?,?);";
			String sql2 = "insert into category (category) values (?);";
			
			int records = 0;
			while((line=br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(line,"##");
				String category_id = st.nextToken();
				String title = st.nextToken();
				String description = st.nextToken();
				String due_date = st.nextToken();
				String current_date = st.nextToken();
				
				String category = categoryName(Integer.parseInt(category_id));
				
				PreparedStatement pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, title);
				pstmt.setString(2, description);
				pstmt.setString(3, category);
				pstmt.setString(4, current_date);
				pstmt.setString(5, due_date);
				
				if(!isDuplicateCategory(category)) {
					pstmt = conn.prepareStatement(sql2);
					pstmt.setInt(1, Integer.parseInt(category_id));	
				}
				
				int count = pstmt.executeUpdate();
				if(count>0) records++;
				pstmt.close();
			}
			System.out.println(records + " records read!!");
			br.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public TodoList() {
		this.conn = DbConnect.getConnection();
	}

	
	public int addItem(TodoItem t) {
		String sql = "insert into list (title, memo, category_id, current_date, due_date)"
				+ " values (?,?,?,?,?);";
		String sql2 = "insert into category (category) values (?);";
		
		int category_id = categoryNumber(t.getCategory());
		String category = String.valueOf(category_id);
		
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, category);
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			
			if(!isDuplicateCategory(category)) {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, category_id);	
			}
			
			count = pstmt.executeUpdate();
			pstmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	private int categoryNumber(String category) {
		switch(category) {
		case "과제" : return 1;
		case "시험" : return 2;
		case "집안일" : return 3;
		case "약속" : return 4;
		case "기타" : return 5;
		default : return 0;
		}
	}
	
	private String categoryName(int category_id) {
		switch(category_id) {
		case 1: return "과제";
		case 2: return "시험";
		case 3: return "집안일";
		case 4: return "약속";
		default : return "기타";
		}
	}

	public int updateItem(TodoItem t) {
		String sql = "update list set title=?, memo=?, category=?, current_date=?, due_date=?"
				+ " where id=?;";
		
		String sql2 = "insert into category (category) values (?);";
		
		int category_id = categoryNumber(t.getCategory());
		String category = categoryName(category_id);
		
		PreparedStatement pstmt;
		int count = 0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, t.getTitle());
			pstmt.setString(2, t.getDesc());
			pstmt.setString(3, category);
			pstmt.setString(4, t.getCurrent_date());
			pstmt.setString(5, t.getDue_date());
			pstmt.setInt(6, t.getId());
			
			if(!isDuplicateCategory(category)) {
				pstmt = conn.prepareStatement(sql2);
				pstmt.setInt(1, category_id);	
			}
			
			count = pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public int deleteItem(int index) {
		String sql = "delete from list where id=?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count= pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int completeItem(int index) {
		String sql = "update list set is_completed=1 where id = ?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count= pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int importItem(int index) {
		String sql = "update list set is_important=1 where id = ?;";
		PreparedStatement pstmt;
		int count=0;
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, index);
			count= pstmt.executeUpdate();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}
	
	public int getCount() {
		Statement stmt;
		int count=0;
		try {
			stmt = conn.createStatement();
			String sql = "SELECT count(id) FROM list;";
			ResultSet rs = stmt.executeQuery(sql);
			rs.next();
			count=rs.getInt("count(id)");
			stmt.close();
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public ArrayList<TodoItem> getOrderedList(String orderby, int ordering){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;
		
		try {
			stmt=conn.createStatement();
			String sql="SELECT * FROM list ORDER BY "+orderby;
			
			if(ordering==0) sql +=" desc";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int category_id = rs.getInt("category_id");
				String category = categoryName(category_id);
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_complete = rs.getInt("is_completed");
				int is_important = rs.getInt("is_important");
				TodoItem t = new TodoItem(title,description, category, due_date,is_complete,is_important);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	

	public ArrayList<TodoItem> getList(){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		Statement stmt;

		try {
			stmt = conn.createStatement();
			String sql = "SELECT * FROM list";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int category_id = rs.getInt("category_id");
				String category = categoryName(category_id);
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_complete = rs.getInt("is_completed");
				int is_important = rs.getInt("is_important");
				TodoItem t = new TodoItem(title,description, category, due_date,is_complete,is_important);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getList(String keyword){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
		keyword= "%"+keyword+"%";
		
		try {
			
			String sql = "SELECT * FROM list WHERE title like ? or memo like ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, keyword);
			pstmt.setString(2, keyword);
			ResultSet rs = pstmt.executeQuery();
			
			
			while(rs.next()) {
				int id = rs.getInt("id");
				int category_id = rs.getInt("category_id");
				String category = categoryName(category_id);
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_complete = rs.getInt("is_completed");
				int is_important = rs.getInt("is_important");
				TodoItem t = new TodoItem(title,description, category, due_date,is_complete,is_important);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getCategories(){
		ArrayList<String> list = new ArrayList<String>();
		Statement stmt;
		try {
			stmt=conn.createStatement();
			String sql = "SELECT DISTINCT category FROM category";
			ResultSet rs = stmt.executeQuery(sql);
			
			while(rs.next()) {
				
				String category = rs.getString("category");
				
				list.add(category);
			}
			stmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<TodoItem> getListCategory(String keyword){
		ArrayList<TodoItem> list = new ArrayList<TodoItem>();
		PreparedStatement pstmt;
	
		try {
			int category_id = categoryNumber(keyword);
			String sql = "SELECT * FROM list WHERE category_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, category_id);
			
			ResultSet rs = pstmt.executeQuery();
			
			while(rs.next()) {
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String description = rs.getString("memo");
				String category = keyword;
				String due_date = rs.getString("due_date");
				String current_date = rs.getString("current_date");
				int is_complete = rs.getInt("is_completed");
				int is_important = rs.getInt("is_important");
				TodoItem t = new TodoItem(title,description, category, due_date,is_complete,is_important);
				t.setId(id);
				t.setCurrent_date(current_date);
				list.add(t);
			}
			pstmt.close();
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	public Boolean isDuplicate(String title) {
		
		PreparedStatement pstmt;
		
		try {
			
			String sql = "SELECT * FROM list WHERE title = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, title);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs!=null) return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public Boolean isDuplicateCategory(String category) {
		
		PreparedStatement pstmt;
		
		try {
			
			String sql = "SELECT * FROM category WHERE category = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, category);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs!=null) return true;
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
/*
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
	
		
	public TodoItem accessItem(Integer a) {
		TodoItem t = list.get(a);
		return t;
	}
*/
	
}

