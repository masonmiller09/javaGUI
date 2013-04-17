import java.sql.*;

public class FBAgency
{
    private String acct_Num;
    private Connection connL = null;
    private ResultSet rsL = null;
    private Statement stmtL = null;

    public FBAgency(Connection aConnL, String aAcct_Num, String aAgency_Name)
            throws SQLException
    {
        connL = aConnL;
        PreparedStatement prepL = connL
                .prepareStatement("insert into fb_agency values (?, ?);");
        prepL.setString(1, aAcct_Num);
        prepL.setString(2, aAgency_Name);
        prepL.addBatch();
        connL.setAutoCommit(false);
        prepL.executeBatch();
        connL.setAutoCommit(true);
    }

    public FBAgency(Connection aConnL, String aAcct_Num) throws SQLException
    {
        acct_Num = aAcct_Num;
        connL = aConnL;
    }

    public String getCreateAgencyQuery()
    {
        String insertCustQuery = "INSERT INTO fb_agency(Acct_Num,Agency_Name) VALUES (";
        String temp = insertCustQuery + "'" + acct_Num + "','"
                + getAgencyName() + "');";
        return temp;
    }

    public String getAcctNum()
    {
        return acct_Num;
    }

    public String setAcctNum(String aAcctNum)
    {
        try
        {
            connL.setAutoCommit(true);
            String sql = "UPDATE fb_agency SET Acct_Num= '" + aAcctNum
                    + "' WHERE Acct_Num=" + acct_Num;
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

    public String getAgencyName()
    {
        String agencyName = "";
        try
        {
            String sql = "SELECT * FROM fb_agency WHERE Acct_Num = " + acct_Num;
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
        return agencyName;
    }

    public String setAgencyName(String aAgencyName)
    {
        try
        {
            connL.setAutoCommit(true);
            String sql = "UPDATE fb_agency SET Agency_Name= '" + aAgencyName
                    + "' WHERE Acct_Num = " + acct_Num;
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
