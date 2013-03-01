import java.sql.*;

   public class Connect
   {
	   public static void main(String[] args)
	   {
		   System.out.println("MySQL Connect Example.");
		   Connection conn = null;
		   String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
		   String dbName = "feastdb";
		   String driver = "com.mysql.jdbc.Driver";
		   String userName = "root"; 
		   String password = "lampa";
		   Statement stmt = null;
		   ResultSet rs = null;
		   try
		   {
			   Class.forName(driver).newInstance();
			   conn = DriverManager.getConnection(url+dbName,userName,password);
			   stmt = conn.createStatement();
			   String sql = "select * from fb_customer";
			   rs = stmt.executeQuery(sql);
			   while(rs.next())
			   {
				   String last_Name = rs.getString("Last_Name");
				   String first_Name = rs.getString("First_Name");
				   
				   System.out.println(first_Name + " " + last_Name);
			   }
			   System.out.println("Connected to the database");
			   conn.close();
			   System.out.println("Disconnected from database");
		   }
		   catch (Exception e)
		   {
			   e.printStackTrace();
		   }
	   }
   }