
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

public class JDBC_insert_3 {

	public static void main(String[] args) throws Exception {
		Connection con=DBconn.getConn();
		Statement stmt=con.createStatement();
		PreparedStatement pstmt1=con.prepareStatement("INSERT INTO tasks VALUES (?,?,?,?,?)");
		PreparedStatement pstmt2=con.prepareStatement("INSERT INTO taskid_pks VALUES (?,?)");
		PreparedStatement pstmt3=con.prepareStatement("UPDATE taskid_pks SET taskid=? WHERE regid=?");
		
		Scanner sc=new Scanner(System.in);
		System.out.println("Enter TaskName");
		String taskName=sc.nextLine();
		System.out.println("Enter TaskDate");
		String taskDate=sc.nextLine();
		System.out.println("Enter TaskStatus");
		int taskStatus=sc.nextInt();
		System.out.println("Enter RegID");
		int regId=sc.nextInt();
		
		boolean isNew=true;
		int taskId=0;
		ResultSet rs=
		stmt.executeQuery("select taskid from taskid_pks where regid="+regId);
		if(rs.next()) {
			taskId=rs.getInt(1);
			isNew=false;
		}
		taskId++;
		
		con.setAutoCommit(false);
		int i,j=0;
		pstmt1.setInt(1,taskId);
		pstmt1.setString(2,taskName);
		pstmt1.setString(3,taskDate);
		pstmt1.setInt(4, taskStatus);
		pstmt1.setInt(5,  regId);
		i=pstmt1.executeUpdate();
		
		if(isNew==true) {
			pstmt2.setInt(1,regId);
			pstmt2.setInt(2,taskId);
			j=pstmt2.executeUpdate();
		} else {
			pstmt3.setInt(1,taskId);
			pstmt3.setInt(2,regId);
			j=pstmt3.executeUpdate();
		}
		
		if(i==1 && j==1) {
			con.commit();
			System.out.println("TX Success");
		} else {
			con.rollback();
			System.out.println("TX Failed");
		}

		rs.close();pstmt3.close();pstmt2.close();
		pstmt1.close();stmt.close();con.close();
	}
}

