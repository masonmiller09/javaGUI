import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FBDatabase
{
	//Online datebase info and variables
	private String urlR = "jdbc:mysql://phpmyadmin.37west.com:3306/";
	private String dbNameR = "com_37west_hrfoodbank";
	private String driverR = "com.mysql.jdbc.Driver";
	private String userNameR = "cnu";
	private String passwordR = "CnU2401^";
	public Connection connR = null;
	private Statement stmtR = null;
	private ResultSet rsR = null;
	//Local datebase info and variables
	private String driverL = "org.sqlite.JDBC";
	private String urlL = "jdbc:sqlite:";
	private String dbNameL = "com_37west_hrfoodbank.db";
	public Connection connL = null;
	private Statement stmtL = null;
	private PreparedStatement prepL = null;
	private ResultSet rsL = null;
	
	private String directoryPath = "C:/Windows/Temp\\Feast";
	boolean isConnected = false;
	private BufferedWriter output = null;
	private BufferedReader input = null;
	private final long CONVERTTOMINUTES = 10000;
	private long timerMinutes = 1;
	private ArrayList<String> queries = new ArrayList<String>();
//ADDITION-------------------------
	static permLog log;
	static queryQue que;
	boolean timer = true;
	Timer t;
//----------------------
	
	public FBDatabase(){
	    log = new permLog();
        que = new queryQue();
        que.queFileExist();
        log.LogFileExist();
//--------------------------------------------      
        createDirectory();
        localConnect();
        createConnectionTimer();
        canConnect();
	}
	
	public void createConnectionTimer()
	{
	    t = new Timer();
		t.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{
			    System.out.println("Timer boolean = " + timer);
			    if(timer){
			    System.out.println("Test: " + canConnect());
			    }
			    else{
			        
			        t.cancel();
			    }
			}
		}, CONVERTTOMINUTES*timerMinutes, CONVERTTOMINUTES*timerMinutes);
	}
	
	public boolean canConnect()
	{
		isConnected = false;
		try
		{
			Class.forName(driverR).newInstance();
			connR = DriverManager.getConnection(urlR + dbNameR, userNameR, passwordR);
			stmtR = connR.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			isConnected = true;
			System.out.println("should be updated");
			readIn();
			updateDataBase();
			updateLocalDataBase();
		}
		catch(Exception e)
		{
			 //e.printStackTrace();
		}
		return isConnected;
	}
	
	public void readIn() throws IOException
	{
		File dir = new File(directoryPath);
		File file = new File(dir, que.que);
		file.createNewFile();
		input = new BufferedReader(new FileReader(file));
		while(input.ready())
		{
			String s = input.readLine();
			queries.add(s);
			System.out.println(s);
		}
		que.clearQue();
		input.close();
	}

	public void updateDataBase() throws SQLException
	{
		String temp;
		System.out.println("queries size: " + queries.size());
		for(int i = 0; i < queries.size(); i++)
		{
			temp = queries.get(i);
			    stmtR = connR.createStatement();
			    stmtR.execute( temp );
			    log.logQuery( "Internet Database: " + temp );
		}
	}
	
	public void createLocalDataBases() 
	{
	    

        try
        {
            Statement stmtL = connL.createStatement();
            stmtL.executeUpdate("drop table if exists jos_fb_customer;");
            stmtL.executeUpdate("drop table if exists jos_fb_agency;");
            stmtL.executeUpdate("drop table if exists jos_fb_monthlydist;");
            stmtL.executeUpdate("drop table if exists jos_fb_agencyRep;");
            stmtL.executeUpdate("create table jos_fb_customer (Customer_ID INTEGER PRIMARY KEY AUTOINCREMENT, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number, Number_Children, Number_Adults, Number_Seniors, Total_Household, FoodStamps_SNAP, TANF, SSI, Medicaid, HH_Income, Inc_Weekly, Inc_Monthly, Inc_Yearly, Offender);");
            stmtL.executeUpdate("create table jos_fb_agency (Acct_Num VARCHAR(255) PRIMARY KEY, Agency_Name);");
            stmtL.executeUpdate("create table jos_fb_agencyRep (AgencyRep_ID INTEGER PRIMARY KEY AUTOINCREMENT, Acct_Num, Rep_LName, Rep_FName, FOREIGN KEY(Acct_Num) REFERENCES jos_fb_agency(Acct_Num));");
            System.out.println("Before");
            stmtL.executeUpdate("create table jos_fb_monthlydist (Customer_ID, Acct_Num, AgencyRep_ID, theDate," +
                    " FOREIGN KEY(Customer_ID) REFERENCES jos_fb_customer(Customer_ID), FOREIGN KEY(Acct_Num) REFERENCES jos_fb_agency(Acct_Num)," +
                    " FOREIGN KEY(AgencyRep_ID) REFERENCES jos_fb_agencyRep(AgencyRep_ID) PRIMARY KEY(theDate, Acct_Num, Customer_ID));");
            System.out.println("After");
        }
        catch ( SQLException e )
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        //stmtL.executeUpdate("create table jos_fb_que (query_Type, Table_Name, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number, Number_Children, Number_Adults, Number_Seniors, Total_Household, FoodStamps_SNAP, TANF, SSI, Medicaid, HH_Income, Inc_Weekly, Inc_Monthly, Inc_Yearly, Offender);");
        
	}
	
	public void updateLocalDataBase() throws SQLException
	{		
	        createLocalDataBases();
			String sql = "select * from jos_fb_customer";
			rsR = stmtR.executeQuery(sql);
			PreparedStatement prepL = connL.prepareStatement("insert into jos_fb_customer values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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
			connL.setAutoCommit(false);
			System.out.println(prepL.toString());
			prepL.executeBatch();
			connL.setAutoCommit(true);
	}

	public void disconnect()
	{
		if(isConnected)
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
	
	public void localConnect()
	{
		try
		{
			Class.forName(driverL);
			connL = DriverManager.getConnection(urlL + dbNameL);
			stmtL = connL.createStatement();
			System.out.println("Local Connection: " + connL.toString());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public void localDisconnect()
	{
		try
		{
			connL.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void createDirectory()
	{
		File f = new File(directoryPath);
		try
		{
			if(f.mkdir())
			{
				System.out.println("Directory Created");
			}
			else
			{
				System.out.println("Directory is not created");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	public void printNames()
	{
		if(isConnected)
		{
			try
			{
				String sql = "select * from jos_fb_customer";
				rsR = stmtR.executeQuery(sql);
				while(rsR.next())
				{
					String last_Name = rsR.getString("Last_Name");
					String first_Name = rsR.getString("First_Name");
					System.out.println(rsR.getInt("Customer_ID") + " " + first_Name + " " + last_Name);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	public void localPrintNames()
	{
		try
		{
			stmtL = connL.createStatement();
			ResultSet rsL = stmtL.executeQuery("select * from jos_fb_customer;");
			while(rsL.next())
			{
				System.out.println("ID = " + rsL.getString("Customer_ID"));
				System.out.println("Name = " + rsL.getString("First_Name") + " " + rsL.getString("Last_Name"));
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}

	
	public FBCustomer searchCustomerByID(int customerID)
	{
		FBCustomer costumer = new FBCustomer(1, " ", " ");
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM jos_fb_customer WHERE Customer_ID=" + customerID;
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				costumer.setCustomerID(rsR.getInt("Customer_ID"));
				costumer.setLastName(rsR.getString("Last_Name"));
				costumer.setFirstName(rsR.getString("First_Name"));
				costumer.setStreetAddress(rsR.getString("Street_Address"));
				costumer.setApartmentNumber(rsR.getInt("Apartment_Number"));
				costumer.setCity(rsR.getString("City"));
				costumer.setZipCode(rsR.getInt("Zip_Code"));
				costumer.setPhoneNumber(rsR.getLong("Phone_Number"));
				costumer.setNumberOfChildren(rsR.getInt("Number_Children"));
				costumer.setNumberOfAdults(rsR.getInt("Number_Adults"));
				costumer.setNumberOfSeniors(rsR.getInt("Number_Seniors"));
				costumer.setQualificationsFoodStamps(rsR.getInt("FoodStamps_Snap"));
				costumer.setQualificationsTANF(rsR.getInt("TANF"));
				costumer.setQualificationsSSI(rsR.getInt("SSI"));
				costumer.setQualificationsMedicaid(rsR.getInt("Medicaid"));
				costumer.setIncome(rsR.getInt("HH_Income"));
				costumer.setIncomeWeekly(rsR.getInt("Inc_Weekly"));
				costumer.setIncomeMonthly(rsR.getInt("Inc_Monthly"));
				costumer.setIncomeYearly(rsR.getInt("Inc_Yearly"));
				costumer.setOffender(rsR.getInt("Offender"));
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return costumer;
	}
	
	public FBAgency searchAgencyByID(int agencyID)
	{
		FBAgency agency = new FBAgency(0, " ");
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM jos_fb_agency WHERE Agency_ID=" + agencyID;
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				agency.setAgencyID(rsR.getInt("Agency_ID"));
				agency.setAccountNum(rsR.getInt("Acct_Num"));
				agency.setAgencyName(rsR.getString("Agency_Name"));
				sql = "SELECT * FROM jos_fb_agencyRep WHERE Agency_ID=" + agencyID;
				while(rsR.next())
				{
					FBAgencyRep agencyRep = new FBAgencyRep(0, " ", " ");
					agencyRep.setAgencyRepID(rsR.getInt("AgencyRep_ID"));
					agencyRep.setAgencyID(rsR.getInt("Agency_ID"));
					agencyRep.setLastName(rsR.getString("Rep_LName"));
					agencyRep.setFirstName(rsR.getString("Rep_FName"));
					agency.addAgencyRep(agencyRep);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		return agency;
	}

	public void addNewAgency(FBAgency aAgency)
	{
		if(isConnected)
		{
			try
			{
				ArrayList<FBAgencyRep> agencyReps = aAgency.getAgencyReps();
				String insertAgencyQuery = "INSERT INTO jos_fb_agency(Acct_Num,Agency_Name) VALUES (";
				String temp = insertAgencyQuery + "'" + aAgency.getAccountNum() + "','" + aAgency.getAgencyName() + "')";
				stmtR.executeUpdate(temp, Statement.RETURN_GENERATED_KEYS);
				rsR = stmtR.getGeneratedKeys();
				int agencyID = 0;
				if(rsR.next())
				{
					agencyID = rsR.getInt(1);
				}
				for(int i=0; i < agencyReps.size(); i++)
				{
					agencyReps.get(i).setAgencyID(agencyID);
					String insertAgencyRepQuery = "INSERT INTO jos_fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
					temp = insertAgencyRepQuery + "'" + agencyReps.get(i).getAgencyID() + "','" + agencyReps.get(i).getLastName() + "','"
							+ agencyReps.get(i).getFirstName() +"')";
					stmtR.executeUpdate(temp);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			addNewAgencyDisconnected(aAgency);
		}
	}
	public void addNewAgencyDisconnected(FBAgency aAgency)
	{
		try
		{
			String insertAgencyQuery = "INSERT INTO jos_fb_agency(Acct_Num,Agency_Name) VALUES (";
			String temp = insertAgencyQuery + "'" + aAgency.getAccountNum() + "','" + aAgency.getAgencyName() + "');";
			File dir = new File(directoryPath);
			File actualFile = new File(dir, que.que);
			output = new BufferedWriter(new FileWriter(actualFile,true));
			output.write(temp);
			output.newLine();
			ArrayList<FBAgencyRep> agencyReps = aAgency.getAgencyReps();
			for(int i=0; i < agencyReps.size(); i++)
			{
				agencyReps.get(i).setAgencyID(0);
				String insertAgencyRepQuery = "INSERT INTO jos_fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
				temp = insertAgencyRepQuery + "'" + agencyReps.get(i).getAgencyID() + "','" + agencyReps.get(i).getLastName() + "','"
						+ agencyReps.get(i).getFirstName() +"');";
				output.write(temp);
				output.newLine();
			}
			output.close();
		}
		catch(Exception e)
		{
			System.out.println("Could not create file");
		}
	}
	
	public void addNewAgencyRep(FBAgencyRep aAgencyRep)
	{
		if(isConnected)
		{
			try
			{
				String insertAgencyRepQuery = "INSERT INTO jos_fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
				String temp = insertAgencyRepQuery + "'" + aAgencyRep.getAgencyID() + "','" + aAgencyRep.getLastName() + "','" + aAgencyRep.getFirstName() + "')";
				stmtR.executeUpdate(temp);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			addNewAgencyRepDisconnected(aAgencyRep);
		}
	}
	
	public void addNewAgencyRepDisconnected(FBAgencyRep aAgencyRep)
	{
		try
		{
			String insertAgencyRepQuery = "INSERT INTO jos_fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
			String temp = insertAgencyRepQuery + "'" + aAgencyRep.getAgencyID() + "','" + aAgencyRep.getLastName() + "','" + aAgencyRep.getFirstName() + "');";
			File dir = new File(directoryPath);
			File actualFile = new File(dir, que.que);
			output = new BufferedWriter(new FileWriter(actualFile,true));
			output.write(temp);
			output.newLine();
			output.close();
		}
		catch(Exception e)
		{
			System.out.println("Could not create file");
		}
	}
	
	public void addNewMonthlyDistribution(FBMonthlyDistribution aDistribution)
	{
		if(isConnected)
		{
			try
			{
				String insertMonthlyDistributionQuery = "INSERT INTO jos_fb_monthlyDist(Customer_ID, Agency_ID,AgencyRep_ID,theDate) VALUES (";
				String temp = insertMonthlyDistributionQuery + "'" + aDistribution.getCustomer().getCustomerID() + "','"
						+ aDistribution.getAgency().getAgencyID() + "','" + aDistribution.getAgencyRep().getAgencyID() + "','"
						+ aDistribution.getDate() + "')";
				stmtR.executeUpdate(temp);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}
	
	public boolean isConnected()
	{
		return isConnected;
	}

	public void setConnected(boolean aConnected)
	{
		isConnected = aConnected;
	}
}
