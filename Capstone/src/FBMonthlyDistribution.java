import java.sql.*;

public class FBMonthlyDistribution
{
    private int customer_ID;
    private String acct_num;
    private int agencyRep_ID;
    private String date;

    private Connection connL = null;
    private ResultSet rsL = null;
    private Statement stmtL = null;
    queryQue que;

    public FBMonthlyDistribution( Connection aConnL,
            boolean aIsConnected, int aCustomerID, String aAcctNum,
            int aAgencyRepID, String adate, queryQue q) throws SQLException
    {
        customer_ID = aCustomerID;
        acct_num = aAcctNum;
        agencyRep_ID = aAgencyRepID;
        date = adate;
        connL = aConnL;
        stmtL = connL.createStatement();

        que = q;
        PreparedStatement prepL = connL
                .prepareStatement("insert into jos_fb_monthlydist values (?, ?, ?, ?);");
     
        prepL.setInt(1, customer_ID);
        prepL.setString(2, acct_num);
        prepL.setInt(3, agencyRep_ID);
        prepL.setString(4, date);
        System.out.println(prepL.toString());
        prepL.addBatch();
        connL.setAutoCommit(false);
        prepL.executeBatch();
        connL.setAutoCommit(true);
    }

    public FBMonthlyDistribution(Connection aConnL, boolean aIsConnected,
            int aCustomerID, String aAcctNum, String adate) throws SQLException
    {
        customer_ID = aCustomerID;
        acct_num = aAcctNum;
        date = adate;

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
        return customer_ID;

    }

    public String setCustomerID(int aCustomerID)
    {
        try
        {
            connL.setAutoCommit(true);
            String sql = "UPDATE jos_fb_monthlydist SET Customer_ID= '"
                    + aCustomerID + "' WHERE Customer_ID = "
                    + customer_ID + " AND Acct_Num = '" + acct_num + "' AND theDate = '" + date + "';";

            PreparedStatement st = connL.prepareStatement(sql);
            st.execute();
            customer_ID = aCustomerID;

            return sql;
        }
        catch(SQLException e)
        {
            e.printStackTrace();
        }
        return "";
    }

    public String getAcctNum()
    {
       
        return acct_num;
    }

    public String setAcctNum(String aAcctnum)
    {
        try
        {
            connL.setAutoCommit(true);
            String sql = "UPDATE jos_fb_monthlydist SET Acct_Num = '" + aAcctnum
                    + "' WHERE Customer_ID =" + customer_ID + " AND theDate = '" + date + "' AND Acct_Num = '" + acct_num + "';";

            PreparedStatement st = connL.prepareStatement(sql);
            st.execute();
            acct_num = aAcctnum;

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
            String sql = "SELECT * FROM jos_fb_monthlydist WHERE Customer_ID = "
                    + customer_ID + " AND Acct_Num = '" + acct_num + "' AND theDate = '" + date + "';";

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
            String sql = "UPDATE jos_fb_monthlydist SET AgencyRep_ID= '"
                    + aAgencyRepID + "' WHERE Customer_ID = "
                    + customer_ID + " AND Acct_Num = '" + acct_num + "' AND theDate = '" + date + "';";

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

    public String getDate()
    {
        return date;
    }

    public String setDate(String adate)
    {
        try
        {
            connL.setAutoCommit(true);
            String sql = "UPDATE jos_fb_monthlydist SET theDate= '" + adate
                    + "' WHERE Customer_ID=" + customer_ID + " AND Acct_Num = '" + acct_num + "' AND theDate = '" + date + "';";

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
    public String createQuery(){
        return "INSERT into jos_fb_monthlydist(Customer_ID, Acct_Num, AgencyRep_ID, theDate) VALUES (" + getCustomerID() + ", '" + getAcctNum()
        + "', " + getAgencyRepID() + ", '" + getDate() + "');";
    }
}

   
