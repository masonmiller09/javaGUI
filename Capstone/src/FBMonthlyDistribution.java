import java.sql.*;

public class FBMonthlyDistribution
{
	private int distribution_ID;
	private Connection connL = null;
	private ResultSet rsL = null;
	private Statement stmtL = null;

	public FBMonthlyDistribution(Connection aConnR, Connection aConnL,
			boolean aIsConnected, int aCustomerID, int aAgencyID,
			int aAgencyRepID, String date) throws SQLException
	{
		connL = aConnL;
		PreparedStatement prepL = connL
				.prepareStatement("insert into fb_monthlyDist values (?, ?, ?, ?, ?);");
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

	public FBMonthlyDistribution(Connection aConnL, boolean aIsConnected,
			int aDistributionID) throws SQLException
	{
		distribution_ID = aDistributionID;
		connL = aConnL;
	}

	/*
	 * public String getCreateMonthlyDistQuery() { String insertCustQuery =
	 * "INSERT INTO fb_monthlyDist(Distribution_ID, Customer_ID,Agency_ID,AgencyRep_ID,theDate) VALUES ("
	 * ; String temp = insertCustQuery + "'" + agency_ID + "','" + getAcctNum()
	 * + "','" + getAgencyName() + "');"; return temp; }
	 */

	public int getCustomerID()
	{
		int customerID = -1;
		try
		{
			String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = "
					+ distribution_ID;
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
		return customerID;
	}

	public String setCustomerID(int aCustomerID)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE fb_monthlyDist SET Customer_ID= '"
					+ aCustomerID + "' WHERE Distribution_ID="
					+ distribution_ID;
			PreparedStatement st = connL.prepareStatement(sql);
			st.execute();
			return sql;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public int getAgencyID()
	{
		int agencyID = -1;
		try
		{
			String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = "
					+ distribution_ID;
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
		return agencyID;
	}

	public String setAgencyID(int aAgencyID)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE fb_monthlyDist SET Agency_ID= '" + aAgencyID
					+ "' WHERE Distribution_ID=" + distribution_ID;
			PreparedStatement st = connL.prepareStatement(sql);
			st.execute();
			return sql;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public int getAgencyRepID()
	{
		int agencyRepID = -1;
		try
		{
			String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = "
					+ distribution_ID;
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
		return agencyRepID;
	}

	public String setAgencyRepID(int aAgencyRepID)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE fb_monthlyDist SET AgencyRep_ID= '"
					+ aAgencyRepID + "' WHERE Distribution_ID="
					+ distribution_ID;
			PreparedStatement st = connL.prepareStatement(sql);
			st.execute();
			return sql;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}

	public Date getDate()
	{
		Date date = null;
		try
		{
			String sql = "SELECT * FROM fb_monthlyDist WHERE Distribution_ID = "
					+ distribution_ID;
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
		return date;
	}

	public String setDate(Date date)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE fb_monthlyDist SET theDate= '" + date
					+ "' WHERE Distribution_ID=" + distribution_ID;
			PreparedStatement st = connL.prepareStatement(sql);
			st.execute();
			return sql;
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return "";
	}
}
