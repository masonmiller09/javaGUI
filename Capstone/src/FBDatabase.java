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
	private String urlR = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
	private String dbNameR = "feastdb";
	private String driverR = "com.mysql.jdbc.Driver";
	private String userNameR = "root";
	private String passwordR = "lampa";
	public Connection connR = null;
	private Statement stmtR = null;
	private ResultSet rsR = null;
	//Local datebase info and variables
	private String driverL = "org.sqlite.JDBC";
	private String urlL = "jdbc:sqlite:";
	private String dbNameL = "feast.db";
	public Connection connL = null;
	private Statement stmtL = null;
	private PreparedStatement prepL = null;
	private ResultSet rsL = null;
	
	private String directoryPath = "C:/Windows/Temp\\Feast";
	private String logFileName = "DBUploadedLog.txt";
	public boolean isConnected = false;
	private BufferedWriter output = null;
	private BufferedReader input = null;
	private final long CONVERTTOMINUTES = 10000;   //Should be 60000 to be minutes
	private long timerMinutes = 1;
	private ArrayList<String> queries = new ArrayList<String>();

	public FBDatabase()
	{
		canConnect();
		connect();
		localConnect();
		try
		{
			updateLocalDataBase();
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		final FBDatabase database = new FBDatabase();
		//database.createDirectory();
	//	database.createConnectionTimer();
		database.canConnect();
		database.connect();
		database.localConnect();
		try
		{
			database.updateLocalDataBase();
			/*FBCustomer test = new FBCustomer(database.connR, database.connL, database.isConnected, "Miller", "Mason", "1000 University Place" , "-1", "Newport News", 23606, 7033333333L, 0,
					2, 1, 3, 1, 0, 0, 0, 20000, 0, 0, 1, 0);
			// Get a statement from the connection
			String temp = test.getCreateCustomerQuery();
			System.out.println(test.getCreateCustomerQuery());
			System.out.println(test.setFirstName("John"));
			System.out.println(test.setLastName("Nettles"));
			
			database.stmtL = database.connL.createStatement();
			database.stmtL.executeUpdate(temp);
			database.stmtL.close();
			
			System.out.println(test.setNum_Children(4));
			System.out.println(test.getNum_Children());
			System.out.println(test.getTotal_Household());*/
			
			//FBAgency test = new FBAgency(database.connR, database.connL, database.isConnected, "444", "Test");
			java.util.Date dt = new java.util.Date();
			java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String currentTime = sdf.format(dt);
			FBMonthlyDistribution test1 = new FBMonthlyDistribution(database.connR, database.connL, database.isConnected, 1, 1, 2, currentTime);
			//test.setAcctNum("1234");
			//test.setAgencyName("test3234");
			System.out.println("<html>Some text <font color='red'>some text in red</font></html>");
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		//database.localPrintNames();
		database.disconnect();
		database.localDisconnect();
	}
	
	public void createConnectionTimer()
	{
		Timer t = new Timer();
		t.scheduleAtFixedRate(new TimerTask()
		{
			public void run()
			{
				System.out.println("Test: " + canConnect());
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
			readIn();
			updateDataBase();
			clearQue();
		}
		catch(Exception e)
		{
			// e.printStackTrace();
		}
		return isConnected;
	}
	
	public void readIn() throws IOException
	{
		File dir = new File(directoryPath);
		File file = new File(dir, logFileName);
		input = new BufferedReader(new FileReader(file));

		while(input.ready())
		{
			String s = input.readLine();
			queries.add(s);
		}
		input.close();
	}

	public void updateDataBase() throws SQLException
	{
		String temp;
		for(int i = 0; i < queries.size(); i++)
		{
			temp = queries.get(i);
			stmtR.executeUpdate(temp);
		}
	}
	
	public void updateLocalDataBase() throws SQLException
	{
		if(isConnected)
		{
			//Updates Customer Table from Online Database
			Statement stmtL = connL.createStatement();
			stmtL.executeUpdate("drop table if exists fb_customer;");
			stmtL.executeUpdate("create table fb_customer (Customer_ID, Last_Name, First_Name, Street_Address, Apartment_Number, City, Zip_Code, Phone_Number, Number_Children, Number_Adults, Number_Seniors, Total_Household, FoodStamps_SNAP, TANF, SSI, Medicaid, HH_Income, Inc_Weekly, Inc_Monthly, Inc_Yearly, Offender);");
			String sql = "select * from fb_customer";
			rsR = stmtR.executeQuery(sql);
			PreparedStatement prepL = connL.prepareStatement("insert into fb_customer values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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
			prepL.executeBatch();
			connL.setAutoCommit(true);
			
			//Updates Agency table from online Database
			stmtL = connL.createStatement();
			stmtL.executeUpdate("drop table if exists fb_agency;");
			stmtL.executeUpdate("create table fb_agency (Agency_ID, Acct_Num, Agency_Name);");
			sql = "select * from fb_agency";
			rsR = stmtR.executeQuery(sql);
			prepL = connL.prepareStatement("insert into fb_agency values (?,?,?);");
			while(rsR.next())
			{
				prepL.setInt(1, rsR.getInt("Agency_ID"));
				prepL.setString(2, rsR.getString("Acct_Num"));
				prepL.setString(3, rsR.getString("Agency_Name"));
				prepL.addBatch();
			}
			connL.setAutoCommit(false);
			prepL.executeBatch();
			connL.setAutoCommit(true);
			
			//Updates AgencyRep table from online Database
			stmtL = connL.createStatement();
			stmtL.executeUpdate("drop table if exists fb_agencyRep;");
			stmtL.executeUpdate("create table fb_agencyRep (AgencyRep_ID, Agency_ID, Rep_LName, Rep_FName);");
			sql = "select * from fb_agencyRep";
			rsR = stmtR.executeQuery(sql);
			prepL = connL.prepareStatement("insert into fb_agencyRep values (?,?,?,?);");
			while(rsR.next())
			{
				prepL.setInt(1, rsR.getInt("AgencyRep_ID"));
				prepL.setInt(2, rsR.getInt("Agency_ID"));
				prepL.setString(3, rsR.getString("Rep_LName"));
				prepL.setString(4, rsR.getString("Rep_FName"));
				prepL.addBatch();
			}
			connL.setAutoCommit(false);
			prepL.executeBatch();
			connL.setAutoCommit(true);
			
			//Updates MonthlyDistribution table from online Database
			stmtL = connL.createStatement();
			stmtL.executeUpdate("drop table if exists fb_monthlyDist;");
			stmtL.executeUpdate("create table fb_monthlyDist (Distribution_ID, Customer_ID, Agency_ID, AgencyRep_ID, theDate);");
			sql = "select * from fb_monthlyDist";
			rsR = stmtR.executeQuery(sql);
			prepL = connL.prepareStatement("insert into fb_monthlyDist values (?,?,?,?,?);");
			while(rsR.next())
			{
				prepL.setInt(1, rsR.getInt("Distribution_ID"));
				prepL.setInt(2, rsR.getInt("Customer_ID"));
				prepL.setString(3, rsR.getString("Agency_ID"));
				prepL.setString(4, rsR.getString("AgencyRep_ID"));
				prepL.setString(5, rsR.getString("theDate"));
				prepL.addBatch();
			}
			connL.setAutoCommit(false);
			prepL.executeBatch();
			connL.setAutoCommit(true);
		}
	}
	
	public void clearQue() throws IOException
    {
		File dir = new File(directoryPath);
		File actualFile = new File(dir, logFileName);
		output = new BufferedWriter(new FileWriter(actualFile,false));
		output.write("");
		output.close();
    }

	public void connect()
	{
		if(isConnected)
		{
			try
			{
				Class.forName(driverR).newInstance();
				connR = DriverManager.getConnection(urlR + dbNameR, userNameR, passwordR);
				stmtR = connR.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
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
			//stmtL = connL.createStatement();
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
				String sql = "select * from fb_customer";
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
			ResultSet rsL = stmtL.executeQuery("select * from fb_customer;");
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

	public void addNewCustomer(FBCustomerOld aCustomer)
	{
		if(isConnected)
		{
			try
			{
				/*String sql = "select * from fb_customer";
				rsR = stmtR.executeQuery(sql);
				rsR.moveToInsertRow();
				rsR.updateString("Last_Name", aCustomer.getLastName());
				rsR.updateString("First_Name", aCustomer.getFirstName());
				rsR.updateString("Street_Address", aCustomer.getStreetAddress());
				if(aCustomer.getApartmentNumber() > 0)
					rsR.updateInt("Apartment_Number", aCustomer.getApartmentNumber());
				else
					rsR.updateInt("Apartment_Number", 0);
				rsR.updateString("City", aCustomer.getCity());
				rsR.updateInt("Zip_Code", aCustomer.getZipCode());
				rsR.updateLong("Phone_Number", aCustomer.getPhoneNumber());
				rsR.updateInt("Number_Children", aCustomer.getNumOfChildren());
				rsR.updateInt("Number_Adults", aCustomer.getNumOfAdults());
				rsR.updateInt("Number_Seniors", aCustomer.getNumOfSeniors());
				rsR.updateInt("Total_Household", aCustomer.getFamilyTotal());
				rsR.updateInt("FoodStamps_Snap", aCustomer.isQualificationsFoodStamps());
				rsR.updateInt("TANF", aCustomer.isQualificationsTANF());
				rsR.updateInt("SSI", aCustomer.isQualificationsSSI());
				rsR.updateInt("Medicaid", aCustomer.isQualificationsMedicaid());
				rsR.updateInt("HH_Income", aCustomer.getIncome());
				rsR.updateInt("Inc_Weekly", aCustomer.isIncomeWeekly());
				rsR.updateInt("Inc_Monthly", aCustomer.isIncomeMonthly());
				rsR.updateInt("Inc_Yearly", aCustomer.isIncomeYearly());
				rsR.updateInt("Offender", aCustomer.isOffender());
				rsR.insertRow();*/
				String insertCustQuery = "INSERT INTO fb_customer(Last_Name,First_Name,Street_Address,Apartment_Number,City,"
						+ "Zip_Code,Phone_Number,Number_Children,Number_Adults,Number_Seniors,Total_Household,FoodStamps_Snap,TANF,SSI,Medicaid,HH_Income,"
						+ "Inc_Weekly,Inc_Monthly,Inc_Yearly,Offender) VALUES (";
				String temp = insertCustQuery + "'" + aCustomer.getLastName() + "','" + aCustomer.getFirstName() + "','"
						+ aCustomer.getStreetAddress() + "','" + aCustomer.getApartmentNumber() + "','"
						+ aCustomer.getCity() + "','" + aCustomer.getZipCode() + "','"
						+ aCustomer.getPhoneNumber() + "','" + aCustomer.getNumOfChildren() + "','"
						+ aCustomer.getNumOfAdults() + "','" + aCustomer.getNumOfSeniors() + "','"
						+ aCustomer.getFamilyTotal() + "','" + aCustomer.isQualificationsFoodStamps() + "','"
						+ aCustomer.isQualificationsTANF() + "','"+ aCustomer.isQualificationsSSI() + "','"
						+ aCustomer.isQualificationsMedicaid() + "','" + aCustomer.getIncome() + "','"
						+ aCustomer.isIncomeWeekly() + "','" + aCustomer.isIncomeMonthly() + "','"
						+ aCustomer.isIncomeYearly() + "','" + aCustomer.isOffender() + "');";
				String sql = temp;
				stmtR.executeUpdate(sql);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			addNewCustomerDisconnected(aCustomer);
		}
	}
	
	public void addNewCustomerDisconnected(FBCustomerOld aCustomer)
	{
		String insertCustQuery = "INSERT INTO fb_customer(Last_Name,First_Name,Street_Address,Apartment_Number,City,"
				+ "Zip_Code,Phone_Number,Number_Children,Number_Adults,Number_Seniors,Total_Household,FoodStamps_Snap,TANF,SSI,Medicaid,HH_Income,"
				+ "Inc_Weekly,Inc_Monthly,Inc_Yearly,Offender) VALUES (";
		String temp = insertCustQuery + "'" + aCustomer.getLastName() + "','" + aCustomer.getFirstName() + "','"
				+ aCustomer.getStreetAddress() + "','" + aCustomer.getApartmentNumber() + "','"
				+ aCustomer.getCity() + "','" + aCustomer.getZipCode() + "','"
				+ aCustomer.getPhoneNumber() + "','" + aCustomer.getNumOfChildren() + "','"
				+ aCustomer.getNumOfAdults() + "','" + aCustomer.getNumOfSeniors() + "','"
				+ aCustomer.getFamilyTotal() + "','" + aCustomer.isQualificationsFoodStamps() + "','"
				+ aCustomer.isQualificationsTANF() + "','"+ aCustomer.isQualificationsSSI() + "','"
				+ aCustomer.isQualificationsMedicaid() + "','" + aCustomer.getIncome() + "','"
				+ aCustomer.isIncomeWeekly() + "','" + aCustomer.isIncomeMonthly() + "','"
				+ aCustomer.isIncomeYearly() + "','" + aCustomer.isOffender() + "');";
		String dirName = directoryPath;
		try
		{
			File dir = new File(dirName);
			File actualFile = new File(dir, logFileName);
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

	public FBCustomerOld searchCustomerByID(int customerID)
	{
		FBCustomerOld costumer = new FBCustomerOld(1, " ", " ");
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_customer WHERE Customer_ID=" + customerID;
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
	
	public FBAgencyOld searchAgencyByID(int agencyID)
	{
		FBAgencyOld agency = new FBAgencyOld(0, " ");
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_agency WHERE Agency_ID=" + agencyID;
				rsR = stmtR.executeQuery(sql);
				rsR.next();
				agency.setAgencyID(rsR.getInt("Agency_ID"));
				agency.setAccountNum(rsR.getInt("Acct_Num"));
				agency.setAgencyName(rsR.getString("Agency_Name"));
				sql = "SELECT * FROM fb_agencyRep WHERE Agency_ID=" + agencyID;
				while(rsR.next())
				{
					FBAgencyRepOld agencyRep = new FBAgencyRepOld(0, " ", " ");
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

	public void getDBCustomers(int customerID)
	{

	}

	public void addNewAgency(FBAgencyOld aAgency)
	{
		if(isConnected)
		{
			try
			{
				ArrayList<FBAgencyRepOld> agencyReps = aAgency.getAgencyReps();
				String insertAgencyQuery = "INSERT INTO fb_agency(Acct_Num,Agency_Name) VALUES (";
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
					String insertAgencyRepQuery = "INSERT INTO fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
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
	public void addNewAgencyDisconnected(FBAgencyOld aAgency)
	{
		try
		{
			String insertAgencyQuery = "INSERT INTO fb_agency(Acct_Num,Agency_Name) VALUES (";
			String temp = insertAgencyQuery + "'" + aAgency.getAccountNum() + "','" + aAgency.getAgencyName() + "');";
			File dir = new File(directoryPath);
			File actualFile = new File(dir, logFileName);
			output = new BufferedWriter(new FileWriter(actualFile,true));
			output.write(temp);
			output.newLine();
			ArrayList<FBAgencyRepOld> agencyReps = aAgency.getAgencyReps();
			for(int i=0; i < agencyReps.size(); i++)
			{
				agencyReps.get(i).setAgencyID(0);
				String insertAgencyRepQuery = "INSERT INTO fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
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
	
	public void addNewAgencyRep(FBAgencyRepOld aAgencyRep)
	{
		if(isConnected)
		{
			try
			{
				String insertAgencyRepQuery = "INSERT INTO fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
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
	
	public void addNewAgencyRepDisconnected(FBAgencyRepOld aAgencyRep)
	{
		try
		{
			String insertAgencyRepQuery = "INSERT INTO fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
			String temp = insertAgencyRepQuery + "'" + aAgencyRep.getAgencyID() + "','" + aAgencyRep.getLastName() + "','" + aAgencyRep.getFirstName() + "');";
			File dir = new File(directoryPath);
			File actualFile = new File(dir, logFileName);
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
	
	public void addNewMonthlyDistribution(FBMonthlyDistributionOld aDistribution)
	{
		if(isConnected)
		{
			try
			{
				String insertMonthlyDistributionQuery = "INSERT INTO fb_monthlyDist(Customer_ID, Agency_ID,AgencyRep_ID,theDate) VALUES (";
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