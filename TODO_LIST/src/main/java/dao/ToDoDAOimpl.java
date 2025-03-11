package dao;



import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import beans.Register;
import beans.Task;
import factory.DBconn;

public class ToDoDAOimpl implements ToDoDAO {
	
	static ToDoDAO toDoDAO;
	Connection con;
	Statement stmt;
	PreparedStatement pstmt1,pstmt2,pstmt3,pstmt4;
	ResultSet rs;
	private ToDoDAOimpl() {
		try {
			con = DBconn.getConn();
			stmt = con.createStatement();
			pstmt1 = con.prepareStatement("INSERT INTO register VALUES(?,?,?,?,?,?,?)");
			pstmt2 = con.prepareStatement("INSERT INTO tasks VALUES (?,?,?,?,?)");
			pstmt2 = con.prepareStatement("INSERT INTO taskid_pks VALUES(?,?)");
			pstmt4 = con.prepareStatement("UPDATE taskid_pks SET taskid = ? WHERE regid =?");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	// factory method that returns singleton object of this class
	public static ToDoDAO getInstance() {
		if(toDoDAO==null) {
			toDoDAO = new ToDoDAOimpl();
		}
		return toDoDAO;
	}

	@Override
	public int register(Register register) {
		// TODO Auto-generated method stub
		int regId = 0;
		try {
			rs = stmt.executeQuery("select max(regid) from register");
			if(rs.next()) {
				regId = rs.getInt(1);
				
			}
			regId++;
			
			pstmt1.setInt(1,  regId);
			pstmt1.setString(2, register.getFname());
			pstmt1.setString(3, register.getLname());
			pstmt1.setString(4, register.getEmail());
			pstmt1.setString(5, register.getPass());
			pstmt1.setLong(6, register.getMobile());
			pstmt1.setString(7, register.getAddress());
			int i = pstmt1.executeUpdate();
			if(i==1) {
				System.out.println("record inserted into register table : ");
				
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int login(String email, String pass) {
		// TODO Auto-generated method stub
		boolean flag = false;
		int regId = 0;
		try{
			rs = stmt.executeQuery("select regid from register where email =' "+email+"' and pass='"+pass+"'");
			if(rs.next()) {
				regId = rs.getInt(1);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return regId;
	}

	@Override
	public List<Task> findTaskByRegId(int regId) {
		List<Task> taskList = new ArrayList<Task>();
		try{
			rs=stmt.executeQuery("select * from tasks where regid="+regId);
			
			while(rs.next()) {
				Task task = new Task(rs.getInt(1),rs.getString(2),rs.getString(3),rs.getInt(4),rs.getInt(5));
				taskList.add(task);
			};
		} catch(Exception e) {
			e.printStackTrace();
		};
		// TODO Auto-generated method stub
		return taskList;
	}

	@Override
	public int addTask(int regID, Task task) {
		// TODO Auto-generated method stub
		int taskId=0;
		boolean isNew=true;
		int i,j=0;
		try{
			rs=stmt.executeQuery("select taskid from taskid_pks where rigid="+regID);
			if(rs.next()) {
				taskId = rs.getInt(1);
			}
			taskId++;
			con.setAutoCommit(false);
			pstmt2.setInt(1, taskId);
			pstmt2.setString(2, task.getTaskName());
			pstmt2.setString(3, task.getTaskDate());
			pstmt2.setInt(4, task.getTaskStatus());
			pstmt2.setInt(5, task.getRegId());
			i=pstmt2.executeUpdate();
			
			if(isNew) {
				pstmt3.setInt(1, regID);
				pstmt3.setInt(2, taskId);
				j = pstmt3.executeUpdate();
			}else {
				pstmt4.setInt(1, taskId);
				pstmt4.setInt(2, regID);
				j= pstmt4.executeUpdate();
				
			}
			if(i==1 && j<=1) {
				con.commit();
				System.out.println("TX success, task added");
			}
			else {
				con.rollback();
				System.out.println("TX failed");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return taskId;
	}
	 

	@Override
	public boolean markTaskCompleted(int taskId, int regId) {
		boolean flag = false;
		try {
			int i = stmt.executeUpdate("update tasks set taskStatus=3 where "+
					"regid="+regId+"and taskid="+taskId);
			if(i==1) {
				flag=true;
				System.out.println("TX success, taskStatus completed");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		return flag;
	}
	public String getFLNameByRegID(int regId) {
		String flname="";
		try {
			rs = stmt.executeQuery("select fname,lname from register where regId="+regId);
			if(rs.next()) {
				String fname=rs.getString(1);
				String lname=rs.getString(2);
				flname=fname+""+lname;
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		return flname;
	}

}
