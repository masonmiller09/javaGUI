import java.sql.*;


public class FBMonthlyDistribution
{
	private int distribution_ID;
	private boolean isConnected = false;
	private Connection connR = null;
	private ResultSet rsR = null;
	private Statement stmtR = null;
	private Connection connL = null;
	private ResultSet rsL = null;
	private Statement stmtL = null;
	
	public FBMonthlyDistribution(Connection aConnR, Connection aConnL, boolean aIsConnected, int aCustomerID, int aAgencyID, int aAgencyRepID, String date) throws SQLException
	{
		connR = aConnR;
		connL = aConnL;
		isConnected = aIsConnected;
		if(isConnected)
		{
			PreparedStatement prepR = connR.prepareStatement("insert into fb_monthlyDist values (?, ?, ?, ?, ?);");
			String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = (SELECT MAX(Distribution_ID) FROM fb_monthlyDist);";
			stmtR = connR.createStatement();
			rsR = stmtR.executeQuery(sql);
			if(rsR.next())
			{
				distribution_ID = rsR.getInt("Agency_ID") + 1;
			}
			else
			{
				distribution_ID = 1;
			}
			prepR.setInt(1, distribution_ID);
			prepR.setInt(2, aCustomerID);
			prepR.setInt(3, aAgencyID);
			prepR.setInt(4, aAgencyRepID);
			prepR.setString(5, date);
			prepR.addBatch();
			connR.setAutoCommit(false);
			prepR.executeBatch();
			connR.setAutoCommit(true);
		}
		else
		{
			PreparedStatement prepL = connL.prepareStatement("insert into fb_monthlyDist values (?, ?, ?, ?, ?);");
			String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = (SELECT MAX(Distribution_ID) FROM fb_monthlyDist);";
			stmtL = connL.createStatement();
			rsL = stmtL.executeQuery(sql);
			if(rsL.next())
			{
				distribution_ID = rsL.getInt("Agency_ID") + 1;
			}
			else
			{
				distribution_ID = 1;
			}
			prepL.setInt(1, distribution_ID);
			prepL.setInt(2, aCustomerID);
			prepL.setInt(3, aAgencyID);
			prepL.setInt(4, aAgencyRepID);
			prepL.setString(5, date);
			prepL.addBatch();
			connL.setAutoCommit(false);
			prepL.executeBatch();
			connL.setAutoCommit(true);
		}
	}
	public FBMonthlyDistribution(Connection aConnR, Connection aConnL, boolean aIsConnected, int aDistributionID) throws SQLException
	{
		distribution_ID = aDistributionID;
		connR = aConnR;
		connL = aConnL;
		isConnected = aIsConnected;
	}
	
	/*public String getCreateMonthlyDistQuery()
	{
		String insertCustQuery = "INSERT INTO fb_monthlyDist(Distribution_ID, Customer_ID,Agency_ID,AgencyRep_ID,theDate) VALUES (";
		String temp = insertCustQuery + "'" + agency_ID + "','" + getAcctNum() + "','" + getAgencyName() + "');";
		return temp;
	}*/
	
	public int getCustomerID()
	{
		int customerID = -1;
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_MonthlyDistribution WHERE Distribution_ID = " + distribution_ID;
				stmtR = connR.createStatement();
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				customerID = rsR.getInt("Customer_ID");
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = " + distribution_ID;
				stmtL = connL.createStatement();
				rsL = stmtL.executeQuery(sql);
				rsL.next();
				customerID = rsL.getInt("Customer_ID");
				stmtL.close();
				rsL.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return customerID;
	}
	public String setCustomerID(int aCustomerID)
	{
		if(isConnected)
		{
			try
			{
				connR.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET Customer_ID= '" + aCustomerID + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connR.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				connL.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET Customer_ID= '" + aCustomerID + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connL.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public int getAgencyID()
	{
		int agencyID = -1;
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_MonthlyDistribution WHERE Distribution_ID = " + distribution_ID;
				stmtR = connR.createStatement();
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				agencyID = rsR.getInt("Agency_ID");
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = " + distribution_ID;
				stmtL = connL.createStatement();
				rsL = stmtL.executeQuery(sql);
				rsL.next();
				agencyID = rsL.getInt("Agency_ID");
				stmtL.close();
				rsL.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return agencyID;
	}
	public String setAgencyID(int aAgencyID)
	{
		if(isConnected)
		{
			try
			{
				connR.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET Agency_ID= '" + aAgencyID + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connR.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				connL.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET Agency_ID= '" + aAgencyID + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connL.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public int getAgencyRepID()
	{
		int agencyRepID = -1;
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_MonthlyDistribution WHERE Distribution_ID = " + distribution_ID;
				stmtR = connR.createStatement();
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				agencyRepID = rsR.getInt("AgencyRep_ID");
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = " + distribution_ID;
				stmtL = connL.createStatement();
				rsL = stmtL.executeQuery(sql);
				rsL.next();
				agencyRepID = rsL.getInt("AgencyRep_ID");
				stmtL.close();
				rsL.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return agencyRepID;
	}
	public String setAgencyRepID(int aAgencyRepID)
	{
		if(isConnected)
		{
			try
			{
				connR.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET AgencyRep_ID= '" + aAgencyRepID + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connR.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				connL.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET AgencyRep_ID= '" + aAgencyRepID + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connL.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}
	
	public Date getDate()
	{
		Date date = null;
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_MonthlyDistribution WHERE Distribution_ID = " + distribution_ID;
				stmtR = connR.createStatement();
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				date = rsR.getDate("theDate");
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = " + distribution_ID;
				stmtL = connL.createStatement();
				rsL = stmtL.executeQuery(sql);
				rsL.next();
				date = rsL.getDate("theDate");
				stmtL.close();
				rsL.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return date;
	}
	public String setDate(Date date)
	{
		if(isConnected)
		{
			try
			{
				connR.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET theDate= '" + date + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connR.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			try
			{
				connL.setAutoCommit(true);
				String sql = "UPDATE fb_monthlyDist SET theDate= '" + date + "' WHERE Distribution_ID=" + distribution_ID;
				PreparedStatement st = connL.prepareStatement(sql);
				st.execute();
				return sql;
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return "";
	}
}
