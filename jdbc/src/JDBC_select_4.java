import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;



public class JDBC_select_4 {
	public static void main(String rags[]) throws Exception {
		Connection con=DBconn.getConn();
		Statement stmt=con.createStatement();
		ResultSet rs=stmt.executeQuery("select * from tasks where regid=(select regid from register where email='"+rags[0]+"' and pass='"+rags[1]+"')");
		while(rs.next()) {
			System.out.print(rs.getInt(1)+" ");
			System.out.print(rs.getString(2)+" ");
			System.out.print(rs.getString(3)+" ");
			System.out.println(rs.getInt(4));			
		}
		rs.close(); stmt.close(); con.close();
	}
}
