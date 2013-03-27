import java.sql.*;

public class FBAgency
{
	private int agency_ID;
	private boolean isConnected = false;
	private Connection connR = null;
	private ResultSet rsR = null;
	private Statement stmtR = null;
	private Connection connL = null;
	private ResultSet rsL = null;
	private Statement stmtL = null;
	
	public FBAgency(Connection aConnR, Connection aConnL, boolean aIsConnected, String aAcct_Num, String aAgency_Name) throws SQLException 
	{
		connR = aConnR;
		connL = aConnL;
		isConnected = aIsConnected;
		if(isConnected)
		{
			PreparedStatement prepR = connR.prepareStatement("insert into fb_agency values (?, ?, ?);");
			String sql = "select * from fb_agency";
			stmtR = connR.createStatement();
			rsR = stmtR.executeQuery(sql);
			rsR.last();
			agency_ID = rsR.getInt("Agency_ID") + 1;
			prepR.setInt(1, agency_ID);
			prepR.setString(2, aAcct_Num);
			prepR.setString(3, aAgency_Name);
			prepR.addBatch();
			connR.setAutoCommit(false);
			prepR.executeBatch();
			connR.setAutoCommit(true);
		}
		else
		{
			PreparedStatement prepL = connL.prepareStatement("insert into fb_agency values (?, ?, ?);");
			String sql = "SELECT * FROM fb_agency WHERE Agency_ID = (SELECT MAX(Agency_ID) FROM fb_agency);";
			stmtL = connL.createStatement();
			rsL = stmtL.executeQuery(sql);
			if(rsL.next())
			{
				agency_ID = rsL.getInt("Agency_ID") + 1;
			}
			else
			{
				agency_ID = 1;
			}
			prepL.setInt(1, agency_ID);
			prepL.setString(2, aAcct_Num);
			prepL.setString(3, aAgency_Name);
			prepL.addBatch();
			connL.setAutoCommit(false);
			prepL.executeBatch();
			connL.setAutoCommit(true);
		}
	}
	public FBAgency(Connection aConnR, Connection aConnL, boolean aIsConnected, int aAgencyID) throws SQLException
	{
		agency_ID = aAgencyID;
		connR = aConnR;
		connL = aConnL;
		isConnected = aIsConnected;
	}
	
	public String getCreateAgencyQuery()
	{
		String insertCustQuery = "INSERT INTO fb_agency(Agency_ID, Acct_Num,Agency_Name) VALUES (";
		String temp = insertCustQuery + "'" + agency_ID + "','" + getAcctNum() + "','" + getAgencyName() + "');";
		return temp;
	}
	
	public String getAcctNum()
	{
		String acctNum = "";
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_agency WHERE Agency_ID = " + agency_ID;
				stmtR = connR.createStatement();
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				acctNum = rsR.getString("Acct_Num");
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
				String sql = "SELECT * FROM fb_agency WHERE Agency_ID = " + agency_ID;
				stmtL = connL.createStatement();
				rsL = stmtL.executeQuery(sql);
				rsL.next();
				acctNum = rsL.getString("Acct_Num");
				stmtL.close();
				rsL.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return acctNum;
	}

	public String setAcctNum(String aAcctNum)
	{
		if(isConnected)
		{
			try
			{
				connR.setAutoCommit(true);
				String sql = "UPDATE fb_agency SET Acct_Num= '" + aAcctNum + "' WHERE Agency_ID=" + agency_ID;
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
				String sql = "UPDATE fb_agency SET Acct_Num= '" + aAcctNum + "' WHERE Agency_ID=" + agency_ID;
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
	public String getAgencyName()
	{
		String agencyName = "";
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_agency WHERE Agency_ID = " + agency_ID;
				stmtR = connR.createStatement();
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				agencyName = rsR.getString("Agency_Name");
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
				String sql = "SELECT * FROM fb_agency WHERE Agency_ID = " + agency_ID;
				stmtL = connL.createStatement();
				rsL = stmtL.executeQuery(sql);
				rsL.next();
				agencyName = rsL.getString("Agency_Name");
				stmtL.close();
				rsL.close();
			}
			catch(SQLException e)
			{
				e.printStackTrace();
			}
		}
		return agencyName;
	}

	public String setAgencyName(String aAgencyName)
	{
		if(isConnected)
		{
			try
			{
				connR.setAutoCommit(true);
				String sql = "UPDATE fb_agency SET Agency_Name= '" + aAgencyName + "' WHERE Agency_ID=" + agency_ID;
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
				String sql = "UPDATE fb_agency SET Agency_Name= '" + aAgencyName + "' WHERE Agency_ID=" + agency_ID;
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
