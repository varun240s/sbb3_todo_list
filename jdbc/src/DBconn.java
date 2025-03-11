import java.sql.Connection;
import java.sql.DriverManager;



public class DBconn {
	static Connection con;
	public static Connection getConn() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			// Class.forName("oracle.jdbc.driver.OracleDriver");
			
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/sbb3_todo", "root", "");
			// con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "sbb3", "sbb3");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return con;
	}
}