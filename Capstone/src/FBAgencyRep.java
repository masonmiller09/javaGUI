import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class FBAgencyRep
{
	private int agencyRep_ID;
	private Connection connL = null;
	private ResultSet rsL = null;
	private Statement stmtL = null;

	public FBAgencyRep(Connection aConnL, String aAcct_Num, String aRep_LName, String aRep_FName) throws SQLException
	{
		connL = aConnL;
		PreparedStatement prepL = connL
				.prepareStatement("insert into jos_fb_agencyrep values (?, ?, ?, ?);");
		String sql = "SELECT * FROM jos_fb_agencyrep WHERE AgencyRep_ID = (SELECT MAX(AgencyRep_ID) FROM jos_fb_agencyrep);";
		stmtL = connL.createStatement();
		rsL = stmtL.executeQuery(sql);
		if(rsL.next())
		{
			agencyRep_ID = rsL.getInt("AgencyRep_ID") + 1;
		}
		else
		{
			agencyRep_ID = 1;
		}
		prepL.setInt(1, agencyRep_ID);
		prepL.setString(2, aAcct_Num);
		prepL.setString(3, aRep_LName);
		prepL.setString(4, aRep_FName);
		prepL.addBatch();
		connL.setAutoCommit(false);
		prepL.executeBatch();
		connL.setAutoCommit(true);
	}
	public FBAgencyRep(Connection aConnL, int aRep_ID) throws SQLException
	{
		agencyRep_ID = aRep_ID;
		connL = aConnL;
	}
	
	public String getCreateAgencyQuery()
	{
		String insertCustQuery = "INSERT INTO jos_fb_agencyrep(AgencyRep_ID,Acct_Num,Rep_LName,Rep_FName) VALUES (";
		String temp = insertCustQuery + "'" + agencyRep_ID + "','" + getAcct_Num() + "','" + getLastName() + "','" + getFirstName() + "');";
		return temp;
	}
	
	public String getAcct_Num()
	{
		String acct_Num = "";
		try
		{
			String sql = "SELECT * FROM jos_fb_agencyrep WHERE AgencyRep_ID = " + agencyRep_ID;
			stmtL = connL.createStatement();
			rsL = stmtL.executeQuery(sql);
			rsL.next();
			acct_Num = rsL.getString("Acct_Num");
			stmtL.close();
			rsL.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return acct_Num;
	}

	public String getFirstName()
	{
		String firstName = "";
		try
		{
			String sql = "SELECT * FROM jos_fb_agencyrep WHERE AgencyRep_ID = " + agencyRep_ID;
			stmtL = connL.createStatement();
			rsL = stmtL.executeQuery(sql);
			rsL.next();
			firstName = rsL.getString("Rep_FName");
			stmtL.close();
			rsL.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return firstName;
	}
	
	public String getLastName()
	{
		String lastName = "";
		try
		{
			String sql = "SELECT * FROM jos_fb_agencyrep WHERE AgencyRep_ID = " + agencyRep_ID;
			stmtL = connL.createStatement();
			rsL = stmtL.executeQuery(sql);
			rsL.next();
			lastName = rsL.getString("Rep_LName");
			stmtL.close();
			rsL.close();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		return lastName;
	}

	public String setAcctNum(String aAcctNum)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE jos_fb_agencyrep SET Acct_Num = '" + aAcctNum
					+ "' WHERE AgencyRep_ID = " + agencyRep_ID;
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
	
	public String setFirstName(String aFirstName)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE jos_fb_agencyrep SET Rep_FName = '" + aFirstName
					+ "' WHERE AgencyRep_ID = " + agencyRep_ID;
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
	
	public String setLastName(String aLastName)
	{
		try
		{
			connL.setAutoCommit(true);
			String sql = "UPDATE jos_fb_agencyrep SET Rep_LName = '" + aLastName
					+ "' WHERE AgencyRep_ID = " + agencyRep_ID;
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
	
	public int getRepID()
	{
		return agencyRep_ID;
	}
}
