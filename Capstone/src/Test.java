import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Test
{
	private static String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
	private static String dbName = "feastdb";
	private static String driver = "com.mysql.jdbc.Driver";
	private static String userName = "root";
	private static String password = "lampa";
	private static Connection connR = null;
	private static Statement stmtR = null;
	private static ResultSet rsR = null;
	public void connect()
	{
		if(true)
		{
			try
			{
				Class.forName(driver).newInstance();
				connR = DriverManager.getConnection(url + dbName, userName, password);
				stmtR = connR.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void disconnect()
	{
		if(true)
		{
			try
			{
				connR.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) throws Exception
	{
		Class.forName("org.sqlite.JDBC");
		Connection connL = DriverManager.getConnection("jdbc:sqlite:test.db");
		Statement stmtL = connL.createStatement();
		stmtL.executeUpdate("drop table if exists fb_customer;");
		stmtL.executeUpdate("create table fb_customer (Customer_ID, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number, Number_Children, Number_Adults, Number_Seniors, Total_Household, FoodStamps_SNAP, TANF, SSI, Medicaid, HH_Income, Inc_Weekly, Inc_Monthly, Inc_Yearly, Offender);");
		connR = DriverManager.getConnection(url + dbName, userName, password);
		stmtR = connR.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		String sql = "select * from fb_customer";
		rsR = stmtR.executeQuery(sql);
		PreparedStatement prepL = connL.prepareStatement("insert into fb_customer values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
		while(rsR.next())
		{
			prepL.setInt(1, rsR.getInt("Customer_ID"));
			prepL.setString(2, rsR.getString("Last_Name"));
			prepL.setString(3, rsR.getString("First_Name"));
			prepL.setString(4, rsR.getString("Street_Address"));
			prepL.setString(5, rsR.getString("Apartment_Number"));
			prepL.setString(6, rsR.getString("City"));
			prepL.setString(7, rsR.getString("Zip_Code"));
			prepL.setString(8, rsR.getString("Phone_Number"));
			prepL.setInt(9, rsR.getInt("Number_Children"));
			prepL.setInt(10, rsR.getInt("Number_Adults"));
			prepL.setInt(11, rsR.getInt("Number_Seniors"));
			prepL.setInt(12, rsR.getInt("Total_Household"));
			prepL.setInt(13, rsR.getInt("FoodStamps_SNAP"));
			prepL.setInt(14, rsR.getInt("TANF"));
			prepL.setInt(15, rsR.getInt("SSI"));
			prepL.setInt(16, rsR.getInt("Medicaid"));
			prepL.setInt(17, rsR.getInt("HH_Income"));
			prepL.setInt(18, rsR.getInt("Inc_Weekly"));
			prepL.setInt(19, rsR.getInt("Inc_Monthly"));
			prepL.setInt(20, rsR.getInt("Inc_Yearly"));
			prepL.setInt(21, rsR.getInt("Offender"));
			prepL.addBatch();
		}
		disconnect();

		connL.setAutoCommit(false);
		prepL.executeBatch();
		connL.setAutoCommit(true);

		ResultSet rsL = stmtL.executeQuery("select * from fb_customer;");
		while(rsL.next())
		{
			System.out.println("ID = " + rsL.getString("Customer_ID"));
			System.out.println("Name = " + rsL.getString("First_Name") + " " + rsL.getString("Last_Name"));
		}
		rsL.close();
		connL.close();
	}
}