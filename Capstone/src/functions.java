import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.rmi.NoSuchObjectException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.JFileChooser;


public class functions
{

    // returns Object array of size 21. The index 0 is ignored to match database
    // staring at 1
    static ResultSet rs;

    static Statement stmt;

    static ArrayList<Integer> ids = new ArrayList<Integer>();
    
    static Connection connR;
    
    static Connection connL;
    
    static boolean online = false;
    
    public functions(Connection R, Connection L){
        connR = R;
        connL = L;
    }

    public static Object[] retrieveUser( int ID, boolean isConnect ) throws NoSuchObjectException
    {
        online = isConnect;
        Object returnArray[] = new Object[21];
        int id, zip, numChildren, numAdults, numSeniors, hhIncome;
        long phone;
        String fname, lname, address, apartment, city;
        Boolean stamps, tanf, ssi, medicaid, weekly, monthly, annually, offender;
        String request = new String( "SELECT * FROM jos_fb_customer WHERE Customer_ID =  \""
            + ID + "\"" );
        try
        {
            Statement statement;
            if(online){
                statement = connR.createStatement();
            }
            else{
                statement = connL.createStatement();
            }
            ResultSet result = statement.executeQuery( request );
            System.out.println( "Got results:" );
            if ( !result.isBeforeFirst() )
            {
                System.out.println( "NOT THERE!" );
                throw new NoSuchObjectException( new String( "The object with ID:"
                    + ID + " is not in the database" ) );
            }
            while ( result.next() )
            { // process results one row at a time
                returnArray[1] = id = result.getInt( 1 );
                returnArray[2] = fname = result.getString( 2 );
                returnArray[3] = lname = result.getString( 3 );
                returnArray[4] = address = result.getString( 4 );
                returnArray[5] = apartment = result.getString( 5 );
                returnArray[6] = city = result.getString( 6 );
                returnArray[7] = zip = result.getInt( 7 );
                returnArray[8] = phone = result.getLong( 8 );
                returnArray[9] = numChildren = result.getInt( 9 );
                returnArray[10] = numAdults = result.getInt( 10 );
                returnArray[11] = numSeniors = result.getInt( 11 );
                returnArray[12] = stamps = result.getBoolean( 12 );
                returnArray[13] = tanf = result.getBoolean( 13 );
                returnArray[14] = ssi = result.getBoolean( 14 );
                returnArray[15] = medicaid = result.getBoolean( 15 );
                returnArray[16] = hhIncome = result.getInt( 16 );
                returnArray[17] = weekly = result.getBoolean( 17 );
                returnArray[18] = monthly = result.getBoolean( 18 );
                returnArray[19] = annually = result.getBoolean( 19 );
                returnArray[20] = offender = result.getBoolean( 20 );

                System.out.println( "ID = " + id );
                System.out.println( "Last Name = " + fname );
                System.out.println( "First Name = " + lname );
                System.out.println( "Address = " + address );
                System.out.println( "apartment = " + apartment );
                System.out.println( "city = " + city );
                System.out.println( "zip = " + zip );
                System.out.println( "phone = " + phone );
                System.out.println( "numChildren = " + numChildren );
                System.out.println( "numAdults = " + numAdults );
                System.out.println( "numSeniors = " + numSeniors );
                System.out.println( "stamps = " + stamps );
                System.out.println( "tanf = " + tanf );
                System.out.println( "ssi = " + ssi );
                System.out.println( "medicaid = " + medicaid );
                System.out.println( "Household Income = " + hhIncome );
                System.out.println( "weekly = " + weekly );
                System.out.println( "monthly = " + monthly );
                System.out.println( "annually = " + annually );
                System.out.println( "offender = " + offender );
            }

        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnArray;

    }


    public static void delete( boolean isConnect, int id, queryQue que )
    {
        System.out.println( "Attempting Delete of id: " + id );
        try
        {
            stmt = connL.createStatement();
            String query = "Delete from jos_fb_customer Where Customer_ID = " + id
                + ";";

            stmt.executeUpdate( query );
            if(isConnect){
                stmt = connR.createStatement();
                query = "Delete from jos_fb_customer Where Customer_ID = " + id
                    + ";";

                stmt.executeUpdate( query );
            }
            else
            {
                que.addToQue( query );
            }
            

        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    public static void printableDB( boolean isConnect, String d, Vector v )
    {
        if ( d == "" || d.contains( "C:/" ) )
        {
            d = "C:/Windows/Temp/Feast";
        }
        String custTB = "printableDataBase.txt";
        File dir = new File( d );
        dir.mkdir();
        File file = new File( dir, custTB );
        try
        {
            file.createNewFile();
            FBcustomerRS fdbc;
            if(isConnect){
                stmt = connR.createStatement();
            }
            else{
            stmt = connL.createStatement();
            }
            // System.out.println( file );
            BufferedWriter bw;
            bw = new BufferedWriter( new FileWriter( file, true ) );

            int index = 0;
            int count = 0;
            Vector temp;
            // getCustIds();
            // fdb.customerIDExists( id )

            while ( count < v.size() )
            {
                temp = (Vector)v.get( index );
                // System.out.println( temp );
                fdbc = new FBcustomerRS( connR, connL, isConnect, (Integer)temp.get( 1 ) );
                index++;
                count++;
                bw.write( fdbc.getCustomerID() + "," + fdbc.getLastName() + ","
                    + fdbc.getFirstName() + "," + fdbc.getAddress() + ","
                    + fdbc.getApt_Num() + "," + fdbc.getCity() + ","
                    + fdbc.getZip_Code() + "," + fdbc.getPhone_Num() + ","
                    + fdbc.getNum_Children() + "," + fdbc.getNum_Adults() + ","
                    + fdbc.getNum_Seniors() + "," + fdbc.getFoodstamps_Snap()
                    + "," + fdbc.getTanf() + "," + fdbc.getSsi() + ","
                    + fdbc.getMedicaid() + "," + fdbc.getHh_Income() + ","
                    + fdbc.getInc_Monthly() + "," + fdbc.getInc_Weekly() + ","
                    + fdbc.getInc_Yearly() + "," + fdbc.getOffender() + "\r\n" );
            }
            bw.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }


    public static void updateCustomer( int ID, Object[] array, boolean isConnect, queryQue que )
    {
        String request = new String( "UPDATE `jos_fb_customer` SET `Last_Name` = '"
            + array[2]
            + "', `First_Name` = '"
            + array[3]
            + "', `Street_Address` = '"
            + array[4]
            + "', `City` = '"
            + array[6]
            + "', `Zip_Code` = '"
            + array[7]
            + "', `Phone_Number` = '"
            + array[8]
            + "', `Number_Children` = '"
            + array[9]
            + "', `Number_Adults` = '"
            + array[10]
            + "', `Number_Seniors` = '"
            + array[11]
            + "', `FoodStamps_Snap` = "
            + array[12]
            + ", `TANF` = "
            + array[13]
            + ", `SSI` = "
            + array[14]
            + ", `Medicaid` = "
            + array[15]
            + ", `HH_Income` = '"
            + array[16]
            + "', `Inc_Weekly` = "
            + array[17]
            + ", `Inc_Monthly` = "
            + array[18]
            + ", `Inc_Yearly` = "
            + array[19]
            + ", `Offender` = '0' WHERE `jos_fb_customer`.`Customer_ID` = " + +ID );
       
        try
        {
            Statement statement;
            statement = connL.createStatement();
            statement.executeUpdate( request );
            if(isConnect){
                statement = connR.createStatement();
                statement.executeUpdate( request );
            }
            else{
                que.addToQue( request );
            }
            
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public static Object[] retrieveAgency( int agencyID, boolean isConnect )
        throws NoSuchObjectException
    {
        Object returnArray[] = new Object[4];
        int id;
        String account_num, name;
        String request = new String( "SELECT * FROM jos_fb_agency WHERE Agency_ID =  \""
            + agencyID + "\"" );
        try
        {
            Statement statement;
            if(isConnect){
                statement = connR.createStatement();
            }
            else{
                statement = connL.createStatement();
            }
            ResultSet result = statement.executeQuery( request );
            if ( !result.isBeforeFirst() )
            {
                throw new NoSuchObjectException( new String( "The agency with ID:"
                    + agencyID + " is not in the database" ) );
            }
            while ( result.next() )
            {
                returnArray[1] = id = result.getInt( 1 );
                returnArray[2] = account_num = result.getString( 2 );
                returnArray[3] = name = result.getString( 3 );
            }

        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            System.out.println( "SQL" );
            e.printStackTrace();
        }
        return returnArray;
    }


    public static Object[] retrieveAgent( int agentID, boolean isConnect )
        throws NoSuchObjectException
    {
        Object returnArray[] = new Object[5];
        int agent_id, agency_id;
        String last_name, first_name;
        String request = new String( "SELECT * FROM jos_fb_agencyRep WHERE AgencyRep_ID =  \""
            + agentID + "\"" );
        try
        {
            Statement statement;
            if(isConnect){
                statement = connR.createStatement();
            }
            else{
                statement = connL.createStatement();
            }
            
            ResultSet result = statement.executeQuery( request );
            if ( !result.isBeforeFirst() )
            {
                System.out.println( "failed" );
                throw new NoSuchObjectException( new String( "The agent with ID:"
                    + agentID + " is not in the database" ) );
            }
            while ( result.next() )
            {
                System.out.println( "Made it" );
                returnArray[1] = agent_id = result.getInt( 1 );
                returnArray[2] = agency_id = result.getInt( 2 );
                returnArray[3] = last_name = result.getString( 3 );
                returnArray[4] = first_name = result.getString( 4 );
            }

        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return returnArray;
    }
}
