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

	public FBAgencyRep(Connection aConnL, int aAgency_ID, String aRep_LName, String aRep_FName) throws SQLException
	{
		connL = aConnL;
		PreparedStatement prepL = connL
				.prepareStatement("insert into fb_agencyRep values (?, ?, ?, ?);");
		String sql = "SELECT * FROM fb_agencyRep WHERE AgencyRep_ID = (SELECT MAX(AgencyRep_ID) FROM fb_agencyRep);";
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
		prepL.setInt(2, aAgency_ID);
		prepL.setString(3, aRep_LName);
		prepL.setString(4, aRep_FName);
		prepL.addBatch();
		connL.setAutoCommit(false);
		prepL.executeBatch();
		connL.setAutoCommit(true);
	}
}
