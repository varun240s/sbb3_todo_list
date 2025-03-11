import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBC_Conn_1 {
	public static void main(String[] args) {
		try {
			// loading JDBC driver
			Class.forName("com.mysql.jdbc.Driver");
			
			// Establishing Connection to DB 
			Connection con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sbb3_todo", "root", "");
			
			System.out.println(con);
			
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}	
	}
}

