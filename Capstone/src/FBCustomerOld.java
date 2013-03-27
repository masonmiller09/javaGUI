
public class FBCustomerOld
{
	private int customerID;
	private String lastName;
	private String firstName;
	private Address houseAddress = new Address(" ", " ", 0);
	private long phoneNumber;
	private FamilyMembers numOfFamily = new FamilyMembers(1,0,0);
	private USDA_Qualifications qualifications = new USDA_Qualifications(0,0,0,0);
	private HouseholdIncome income = new HouseholdIncome(0,0,0,0);
	private int offender = 0;
	
	public FBCustomerOld(int aCustomerID, String aLastName, String aFirstName, Address aAddress, long aPhoneNumber, FamilyMembers aFamily, USDA_Qualifications aQualifications, HouseholdIncome aHHIncome)
	{
		customerID = aCustomerID;
		setLastName(aLastName);
		setFirstName(aFirstName);
		houseAddress = aAddress;
		setPhoneNumber(aPhoneNumber);
		numOfFamily = aFamily;
		qualifications = aQualifications;
		income = aHHIncome;
		offender = 0;
	}
	public FBCustomerOld(int aCustomerID, String aLastName, String aFirstName)
	{
		customerID = aCustomerID;
		setLastName(aLastName);
		setFirstName(aFirstName);
	}
	public void setAddress(String aStreetAddress, String aCity, int aZipCode)
	{
		houseAddress.setAddress(aStreetAddress, aCity, aZipCode);
	}
	public void setAddress(String aStreetAddress, int aApartmentNumber, String aCity, int aZipCode)
	{
		houseAddress.setAddress(aStreetAddress, aApartmentNumber, aCity, aZipCode);
	}
	public int getCustomerID()
	{
		return customerID;
	}
	public void setCustomerID(int aCustomerID)
	{
		customerID = aCustomerID;
	}
	public String getLastName()
	{
		return lastName;
	}
	public void setLastName(String aLastName)
	{
		lastName = aLastName;
	}
	public String getFirstName()
	{
		return firstName;
	}
	public void setFirstName(String aFirstName)
	{
		firstName = aFirstName;
	}
	public String getStreetAddress()
	{
		return houseAddress.getStreetAddress();
	}
	public void setStreetAddress(String aStreetAddress)
	{
		//System.out.println(aStreetAddress);
		houseAddress.setStreetAddress(aStreetAddress);
	}
	public int getApartmentNumber()
	{
		return houseAddress.getApartmentNumber();
	}
	public void setApartmentNumber(int aApartmentNumber)
	{
		houseAddress.setApartmentNumber(aApartmentNumber);
	}
	public String getCity()
	{
		return houseAddress.getCity();
	}
	public void setCity(String aCity)
	{
		houseAddress.setCity(aCity);
	}
	public int getZipCode()
	{
		return houseAddress.getZipCode();
	}
	public void setZipCode(int aZipCode)
	{
		houseAddress.setZipCode(aZipCode);
	}
	public long getPhoneNumber()
	{
		return phoneNumber;
	}
	public void setPhoneNumber(long aPhoneNumber)
	{
		phoneNumber = aPhoneNumber;
	}
	public int getNumOfChildren()
	{
		return numOfFamily.getNumberOfChildren();
	}
	public void setNumberOfChildren(int numChildren)
	{
		numOfFamily.setNumberOfChildren(numChildren);
	}
	public int getNumOfAdults()
	{
		return numOfFamily.getNumberOfAdults();
	}
	public void setNumberOfAdults(int numAdults)
	{
		numOfFamily.setNumberOfAdults(numAdults);
	}
	public int getNumOfSeniors()
	{
		return numOfFamily.getNumberOfSeniors();
	}
	public void setNumberOfSeniors(int numSeniors)
	{
		numOfFamily.setNumberOfSeniors(numSeniors);
	}
	public int getFamilyTotal()
	{
		return numOfFamily.getFamilyTotal();
	}
	public int isQualificationsFoodStamps()
	{
		return qualifications.isFoodStamps();
	}
	public void setQualificationsFoodStamps(int aFoodStamps)
	{
		qualifications.setFoodStamps(aFoodStamps);
	}
	public int isQualificationsTANF()
	{
		return qualifications.isTANF();
	}
	public void setQualificationsTANF(int aTANF)
	{
		qualifications.setTANF(aTANF);
	}
	public int isQualificationsSSI()
	{
		return qualifications.isSSI();
	}
	public void setQualificationsSSI(int aSSI)
	{
		qualifications.setSSI(aSSI);
	}
	public int isQualificationsMedicaid()
	{
		return qualifications.isMedicaid();
	}
	public void setQualificationsMedicaid(int aMedicaid)
	{
		qualifications.setMedicaid(aMedicaid);
	}
	public int getIncome()
	{
		return income.getIncome();
	}
	public void setIncome(int aIncome)
	{
		income.setIncome(aIncome);
	}
	public int isIncomeWeekly()
	{
		return income.isWeekly();
	}
	public void setIncomeWeekly(int aWeekly)
	{
		income.setWeekly(aWeekly);
	}
	public int isIncomeMonthly()
	{
		return income.isMonthly();
	}
	public void setIncomeMonthly(int aMonthly)
	{
		income.setMonthly(aMonthly);
	}
	public int isIncomeYearly()
	{
		return income.isYearly();
	}
	public void setIncomeYearly(int aYearly)
	{
		income.setYearly(aYearly);
	}
	public int isOffender()
	{
		return offender;
	}
	public void setOffender(int aOffender)
	{
		offender = aOffender;
	}
}
