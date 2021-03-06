import java.math.BigInteger;
import java.sql.*;


// INSERT INTO
// jos_fb_customer(Customer_ID,Last_Name,First_Name,Street_Address,Apartment_Number,City,"
// +
// "Zip_Code,Phone_Number,Number_Children,Number_Adults,Number_Seniors,FoodStamps_Snap,TANF,SSI,Medicaid,HH_Income,"
// + "Inc_Weekly,Inc_Monthly,Inc_Yearly,Offender) VALUES

public class FBcustomerRS
{
    private int customer_ID;

    private boolean isConnected = false;

    // private Connection connR = null;
    private ResultSet rsR = null;

    private Statement stmtR = null;

    private Connection connL = null;

    private ResultSet rsL = null;

    private Statement stmtL = null;
    
    public queryQue que;


    // creates new customer
    public FBcustomerRS(
        Connection aConnL,
        boolean aIsConnected,
        String aLastName,
        String aFirstName,
        String aStreetAddress,
        String aApartmentNumber,
        String aCity,
        int aZipCode,
        long aPhoneNumber,
        int numChildren,
        int numAdults,
        int numSeniors,
        int totalHouseHold,
        int aFoodStamps,
        int aTANF,
        int aSSI,
        int aMedicaid,
        int aHHIncome,
        int aIncWeekly,
        int aIncMonthly,
        int aIncYearly,
        int aOffender, queryQue q ) throws SQLException
    {
        // connR = aConnR;
        connL = aConnL;
        isConnected = aIsConnected;
        que = q;
        PreparedStatement prepL = connL.prepareStatement( "insert into jos_fb_customer values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" );
        String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = (SELECT MAX(Customer_ID) FROM jos_fb_customer);";
        stmtL = connL.createStatement();
        rsL = stmtL.executeQuery( sql );
        if ( rsL.next() )
        {
            customer_ID = rsL.getInt( "Customer_ID" ) + 1;
        }
        else
        {
            customer_ID = 1;
        }
        prepL.setInt( 1, customer_ID );
        prepL.setString( 2, aLastName );
        prepL.setString( 3, aFirstName );
        prepL.setString( 4, aStreetAddress );
        prepL.setString( 5, aApartmentNumber );
        prepL.setString( 6, aCity );
        prepL.setInt( 7, aZipCode );
        prepL.setLong( 8, aPhoneNumber );
        prepL.setInt( 9, numChildren );
        prepL.setInt( 10, numAdults );
        prepL.setInt( 11, numSeniors );
        prepL.setInt( 12, numChildren + numAdults + numSeniors );
        prepL.setInt( 13, aFoodStamps );
        prepL.setInt( 14, aTANF );
        prepL.setInt( 15, aSSI );
        prepL.setInt( 16, aMedicaid );
        prepL.setInt( 17, aHHIncome );
        prepL.setInt( 18, aIncWeekly );
        prepL.setInt( 19, aIncMonthly );
        prepL.setInt( 20, aIncYearly );
        prepL.setInt( 21, aOffender );
        prepL.addBatch();
        // System.out.println("SQL1 " + prepL.toString());
        // System.out.println(prepL.getWarnings());
        connL.setAutoCommit( false );
        prepL.executeBatch();
        connL.setAutoCommit( true );
        prepL.close();
        stmtL.close();
        rsL.close();
    }


    public FBcustomerRS(
        Connection aConnL,
        boolean aIsConnected,
        int aCustomerID ) throws SQLException
    {
        customer_ID = aCustomerID;
        // connR = aConnR;
        connL = aConnL;
        isConnected = aIsConnected;
    }


    public String getCreateCustomerQuery()
    {
        String insertCustQuery = "INSERT INTO jos_fb_customer(Customer_ID, Last_Name,First_Name,Street_Address,Apartment_Number,City,"
            + "Zip_Code,Phone_Number,Number_Children,Number_Adults,Number_Seniors,Total_Household,FoodStamps_Snap,TANF,SSI,Medicaid,HH_Income,"
            + "Inc_Weekly,Inc_Monthly,Inc_Yearly,Offender) VALUES (";
        String temp = insertCustQuery + "'" + customer_ID + "','"
            + getLastName() + "','" + getFirstName() + "','" + getAddress()
            + "','" + getApt_Num() + "','" + getCity() + "','" + getZip_Code()
            + "','" + getPhone_Num() + "','" + getNum_Children() + "','"
            + getNum_Adults() + "','" + getNum_Seniors() + "','"
            + ( getNum_Children() + getNum_Adults() + getNum_Seniors() )
            + "','" + getFoodstamps_Snap() + "','" + getTanf() + "','"
            + getSsi() + "','" + getMedicaid() + "','" + getHh_Income() + "','"
            + getInc_Weekly() + "','" + getInc_Monthly() + "','"
            + getInc_Yearly() + "','" + getOffender() + "');";
        return temp;
    }


    public int getCustomerID()
    {
        return customer_ID;
    }


    public String getFirstName()
    {
        String firstName = "";

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            firstName = rsL.getString( "First_Name" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return firstName;
    }


    public String setFirstName( String aFirstName )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET First_Name= '"
                + aFirstName + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return "";
    }


    public String getLastName()
    {
        String lastName = "";
        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            //rsL.next();
            lastName = rsL.getString( "Last_Name" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }
        return lastName;
    }


    public String setLastName( String aLastName )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Last_Name= '" + aLastName
                + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public String getAddress()
    {
        String address = "";

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            address = rsL.getString( "Street_Address" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return address;
    }


    public String setAddress( String aAddress )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Street_Address= '"
                + aAddress + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getApt_Num()
    {
        int aptNum = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            aptNum = rsL.getInt( "Apartment_Number" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return aptNum;
    }


    public String setApt_Num( String aAptNum )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Apartment_Number= '"
                + aAptNum + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public String getCity()
    {
        String city = "";

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            city = rsL.getString( "City" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return city;
    }


    public String setCity( String aCity )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET City= '" + aCity
                + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getZip_Code()
    {
        int zipCode = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            zipCode = rsL.getInt( "Zip_Code" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return zipCode;
    }


    public String setZip_Code( int aZipCode )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Zip_Code= '" + aZipCode
                + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public long getPhone_Num()
    {
        long phoneNum = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            phoneNum = rsL.getLong( "Phone_Number" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return phoneNum;
    }


    public String setPhone_Num( long aPhoneNum )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Phone_Number= '"
                + aPhoneNum + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getNum_Children()
    {
        int numChildren = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            numChildren = rsL.getInt( "Number_Children" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return numChildren;
    }


    public String setNum_Children( int aNumChildren )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Number_Children= '"
                + aNumChildren + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            setTotal_Household();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getNum_Adults()
    {
        int numAdults = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            numAdults = rsL.getInt( "Number_Adults" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return numAdults;
    }


    public String setNum_Adults( int aNumAdults )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Number_Adults= '"
                + aNumAdults + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            setTotal_Household();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getNum_Seniors()
    {
        int numSeniors = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            numSeniors = rsL.getInt( "Number_Seniors" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return numSeniors;
    }


    public String setNum_Seniors( int aNumSeniors )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Number_Seniors= '"
                + aNumSeniors + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            setTotal_Household();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getTotal_Household()
    {
        int totalHousehold = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID + ";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            totalHousehold = rsL.getInt( "Total_Household" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return totalHousehold;
    }


    public String setTotal_Household()
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Total_Household= '"
                + ( getNum_Children() + getNum_Adults() + getNum_Seniors() )
                + "' WHERE Customer_ID=" + customer_ID + ";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getFoodstamps_Snap()
    {
        int foodStamps = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            foodStamps = rsL.getInt( "FoodStamps_SNAP" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return foodStamps;
    }


    public String setFoodStamps_Snap( int aFoodStamps )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET FoodStamps_SNAP= '"
                + aFoodStamps + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getTanf()
    {
        int TANF = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            TANF = rsL.getInt( "TANF" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return TANF;
    }


    public String setTanf( int aTANF )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET TANF= '" + aTANF
                + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getSsi()
    {
        int SSI = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            SSI = rsL.getInt( "SSI" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return SSI;
    }


    public String setSsi( int aSSI )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET SSI= '" + aSSI
                + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getMedicaid()
    {
        int medicaid = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            medicaid = rsL.getInt( "Medicaid" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return medicaid;
    }


    public String setMedicaid( int aMedicaid )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Medicaid= '" + aMedicaid
                + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getHh_Income()
    {
        int hh_Income = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            hh_Income = rsL.getInt( "HH_Income" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return hh_Income;
    }


    public String setHh_Income( int aHh_Income )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET HH_Income= '" + aHh_Income
                + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getInc_Monthly()
    {
        int incMonthly = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            incMonthly = rsL.getInt( "Inc_Monthly" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return incMonthly;
    }


    public String setInc_Monthly( int aInc_Monthly )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Inc_Monthly= '"
                + aInc_Monthly + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getInc_Weekly()
    {
        int incWeekly = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            incWeekly = rsL.getInt( "Inc_Weekly" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return incWeekly;
    }


    public String setInc_Weekly( int aIncWeekly )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Inc_Weekly= '"
                + aIncWeekly + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getInc_Yearly()
    {
        int incYearly = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            incYearly = rsL.getInt( "Inc_Yearly" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return incYearly;
    }


    public String setInc_Yearly( int aIncYearly )
    {

        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Inc_Yearly= '"
                + aIncYearly + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }


    public int getOffender()
    {
        int offender = 0;

        try
        {
            String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID = "
                + customer_ID+";";
            stmtL = connL.createStatement();
            rsL = stmtL.executeQuery( sql );
            rsL.next();
            offender = rsL.getInt( "Number_Seniors" );
            stmtL.close();
            rsL.close();
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return offender;
    }


    public String setOffender( int aOffender )
    {
        try
        {
            connL.setAutoCommit( true );
            String sql = "UPDATE jos_fb_customer SET Offender= '" + aOffender
                + "' WHERE Customer_ID=" + customer_ID+";";
            que.addToQue( sql );
            PreparedStatement st = connL.prepareStatement( sql );
            st.execute();
            return sql;
        }
        catch ( SQLException e )
        {
            e.printStackTrace();
        }

        return "";
    }
    
    
}
