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
	private String url = "jdbc:mysql://lampa.vf.cnu.edu:3306/";
	private String dbName = "feastdb";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "lampa";
	private Connection conn = null;
	private Statement stmt = null;
	private ResultSet rs = null;
	private String directoryPath = "C:/Windows/Temp\\Feast";
	private String logFileName = "DBUploadedLog.txt";
	private boolean isConnected = false;
	private BufferedWriter output = null;
	private BufferedReader input = null;
	private final long CONVERTTOMINUTES = 60000;
	private long timerMinutes = 1;
	private ArrayList<String> queries = new ArrayList<String>();

	public static void main(String[] args)
	{
		final FBDatabase database = new FBDatabase();
		database.createDirectory();
		database.createConnectionTimer();
		database.canConnect();
		System.out.println("MySQL Connect Example.");
		Address address = new Address("1 University Place", "Newport News", 23606);
		FamilyMembers family = new FamilyMembers(2, 0, 1);
		USDA_Qualifications qualifications = new USDA_Qualifications(1, 0, 1, 0);
		HouseholdIncome income = new HouseholdIncome(30000, 0, 0, 1);
		FBCustomer customer = new FBCustomer(1, "Lewis", "James", address, 7579991010L, family, qualifications, income);
		database.connect();
		System.out.println("Print all names in customer table");
		database.printNames();
		System.out.println("Add a new customer");
		database.addNewCustomer(customer);
		System.out.println("Print all names in customer table with new customer");
		database.printNames();
		System.out.println("Search for customer with CustomerID = 3");
		FBCustomer customer1 = database.searchCustomerByID(3);
		System.out.println("Customer with CustomerID = 3 is");
		System.out.println("Phone Number" + customer1.getPhoneNumber());
		System.out.println(customer1.getFirstName() + " " + customer1.getLastName());
		FBAgency agency = new FBAgency(1, "Church");
		FBAgencyRep rep1 = new FBAgencyRep("Miller", "Mason");
		FBAgencyRep rep2 = new FBAgencyRep("Hayes", "Matt");
		agency.addAgencyRep(rep1);
		agency.addAgencyRep(rep2);
		database.addNewAgency(agency);
		FBAgency agency1 = database.searchAgencyByID(1);
		//Date date = new Date();
		System.out.println(agency1.getAgencyName());
		ArrayList<FBAgencyRep> agencyReps = agency1.getAgencyReps();
		for(int i = 0; i < agencyReps.size(); i++)
		{
			System.out.println(agencyReps.get(i).getFirstName());
		}
		database.disconnect();
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
			Class.forName(driver).newInstance();
			conn = DriverManager.getConnection(url + dbName, userName, password);
			stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
			stmt.executeUpdate(temp);
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
				Class.forName(driver).newInstance();
				conn = DriverManager.getConnection(url + dbName, userName, password);
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
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
				conn.close();
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
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
				rs = stmt.executeQuery(sql);
				while(rs.next())
				{
					String last_Name = rs.getString("Last_Name");
					String first_Name = rs.getString("First_Name");
					System.out.println(rs.getInt("Customer_ID") + " " + first_Name + " " + last_Name);
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void addNewCustomer(FBCustomer aCustomer)
	{
		if(isConnected)
		{
			try
			{
				/*String sql = "select * from fb_customer";
				rs = stmt.executeQuery(sql);
				rs.moveToInsertRow();
				rs.updateString("Last_Name", aCustomer.getLastName());
				rs.updateString("First_Name", aCustomer.getFirstName());
				rs.updateString("Street_Address", aCustomer.getStreetAddress());
				if(aCustomer.getApartmentNumber() > 0)
					rs.updateInt("Apartment_Number", aCustomer.getApartmentNumber());
				else
					rs.updateInt("Apartment_Number", 0);
				rs.updateString("City", aCustomer.getCity());
				rs.updateInt("Zip_Code", aCustomer.getZipCode());
				rs.updateLong("Phone_Number", aCustomer.getPhoneNumber());
				rs.updateInt("Number_Children", aCustomer.getNumOfChildren());
				rs.updateInt("Number_Adults", aCustomer.getNumOfAdults());
				rs.updateInt("Number_Seniors", aCustomer.getNumOfSeniors());
				rs.updateInt("Total_Household", aCustomer.getFamilyTotal());
				rs.updateInt("FoodStamps_Snap", aCustomer.isQualificationsFoodStamps());
				rs.updateInt("TANF", aCustomer.isQualificationsTANF());
				rs.updateInt("SSI", aCustomer.isQualificationsSSI());
				rs.updateInt("Medicaid", aCustomer.isQualificationsMedicaid());
				rs.updateInt("HH_Income", aCustomer.getIncome());
				rs.updateInt("Inc_Weekly", aCustomer.isIncomeWeekly());
				rs.updateInt("Inc_Monthly", aCustomer.isIncomeMonthly());
				rs.updateInt("Inc_Yearly", aCustomer.isIncomeYearly());
				rs.updateInt("Offender", aCustomer.isOffender());
				rs.insertRow();*/
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
				stmt.executeUpdate(sql);
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
	
	public void addNewCustomerDisconnected(FBCustomer aCustomer)
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

	public FBCustomer searchCustomerByID(int customerID)
	{
		FBCustomer costumer = new FBCustomer(1, " ", " ");
		if(isConnected)
		{
			try
			{
				String sql = "SELECT * FROM fb_customer WHERE Customer_ID=" + customerID;
				rs = stmt.executeQuery(sql);
				rs.next();
				costumer.setCustomerID(rs.getInt("Customer_ID"));
				costumer.setLastName(rs.getString("Last_Name"));
				costumer.setFirstName(rs.getString("First_Name"));
				costumer.setStreetAddress(rs.getString("Street_Address"));
				costumer.setApartmentNumber(rs.getInt("Apartment_Number"));
				costumer.setCity(rs.getString("City"));
				costumer.setZipCode(rs.getInt("Zip_Code"));
				costumer.setPhoneNumber(rs.getLong("Phone_Number"));
				costumer.setNumberOfChildren(rs.getInt("Number_Children"));
				costumer.setNumberOfAdults(rs.getInt("Number_Adults"));
				costumer.setNumberOfSeniors(rs.getInt("Number_Seniors"));
				costumer.setQualificationsFoodStamps(rs.getInt("FoodStamps_Snap"));
				costumer.setQualificationsTANF(rs.getInt("TANF"));
				costumer.setQualificationsSSI(rs.getInt("SSI"));
				costumer.setQualificationsMedicaid(rs.getInt("Medicaid"));
				costumer.setIncome(rs.getInt("HH_Income"));
				costumer.setIncomeWeekly(rs.getInt("Inc_Weekly"));
				costumer.setIncomeMonthly(rs.getInt("Inc_Monthly"));
				costumer.setIncomeYearly(rs.getInt("Inc_Yearly"));
				costumer.setOffender(rs.getInt("Offender"));
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
				String sql = "SELECT * FROM fb_agency WHERE Agency_ID=" + agencyID;
				rs = stmt.executeQuery(sql);
				rs.next();
				agency.setAgencyID(rs.getInt("Agency_ID"));
				agency.setAccountNum(rs.getInt("Acct_Num"));
				agency.setAgencyName(rs.getString("Agency_Name"));
				sql = "SELECT * FROM fb_agencyRep WHERE Agency_ID=" + agencyID;
				while(rs.next())
				{
					FBAgencyRep agencyRep = new FBAgencyRep(0, " ", " ");
					agencyRep.setAgencyRepID(rs.getInt("AgencyRep_ID"));
					agencyRep.setAgencyID(rs.getInt("Agency_ID"));
					agencyRep.setLastName(rs.getString("Rep_LName"));
					agencyRep.setFirstName(rs.getString("Rep_FName"));
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

	public void addNewAgency(FBAgency aAgency)
	{
		if(isConnected)
		{
			try
			{
				ArrayList<FBAgencyRep> agencyReps = aAgency.getAgencyReps();
				String insertAgencyQuery = "INSERT INTO fb_agency(Acct_Num,Agency_Name) VALUES (";
				String temp = insertAgencyQuery + "'" + aAgency.getAccountNum() + "','" + aAgency.getAgencyName() + "')";
				stmt.executeUpdate(temp, Statement.RETURN_GENERATED_KEYS);
				rs = stmt.getGeneratedKeys();
				int agencyID = 0;
				if(rs.next())
				{
					agencyID = rs.getInt(1);
				}
				for(int i=0; i < agencyReps.size(); i++)
				{
					agencyReps.get(i).setAgencyID(agencyID);
					String insertAgencyRepQuery = "INSERT INTO fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
					temp = insertAgencyRepQuery + "'" + agencyReps.get(i).getAgencyID() + "','" + agencyReps.get(i).getLastName() + "','"
							+ agencyReps.get(i).getFirstName() +"')";
					stmt.executeUpdate(temp);
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
			String insertAgencyQuery = "INSERT INTO fb_agency(Acct_Num,Agency_Name) VALUES (";
			String temp = insertAgencyQuery + "'" + aAgency.getAccountNum() + "','" + aAgency.getAgencyName() + "');";
			File dir = new File(directoryPath);
			File actualFile = new File(dir, logFileName);
			output = new BufferedWriter(new FileWriter(actualFile,true));
			output.write(temp);
			output.newLine();
			ArrayList<FBAgencyRep> agencyReps = aAgency.getAgencyReps();
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
	
	public void addNewAgencyRep(FBAgencyRep aAgencyRep)
	{
		if(isConnected)
		{
			try
			{
				String insertAgencyRepQuery = "INSERT INTO fb_agencyRep(Agency_ID, Rep_LName,Rep_FName) VALUES (";
				String temp = insertAgencyRepQuery + "'" + aAgencyRep.getAgencyID() + "','" + aAgencyRep.getLastName() + "','" + aAgencyRep.getFirstName() + "')";
				stmt.executeUpdate(temp);
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
	
	public void addNewMonthlyDistribution(FBMonthlyDistribution aDistribution)
	{
		if(isConnected)
		{
			try
			{
				String insertMonthlyDistributionQuery = "INSERT INTO fb_monthlyDist(Customer_ID, Agency_ID,AgencyRep_ID,theDate) VALUES (";
				String temp = insertMonthlyDistributionQuery + "'" + aDistribution.getCustomer().getCustomerID() + "','"
						+ aDistribution.getAgency().getAgencyID() + "','" + aDistribution.getAgencyRep().getAgencyID() + "','"
						+ aDistribution.getDate() + "')";
				stmt.executeUpdate(temp);
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