
public class Address
{
	private String streetAddress;
	private int apartmentNumber = -1;
	private String city;
	private int zipCode;
	
	public Address(String aStreetAddress, String aCity, int aZipCode)
	{
		streetAddress = aStreetAddress;
		city = aCity;
		zipCode = aZipCode;
	}
	public void setAddress(String aStreetAddress, String aCity, int aZipCode)
	{
		streetAddress = aStreetAddress;
		apartmentNumber = -1;
		city = aCity;
		zipCode = aZipCode;
	}
	public void setAddress(String aStreetAddress, int aApartmentNumber, String aCity, int aZipCode)
	{
		streetAddress = aStreetAddress;
		apartmentNumber = aApartmentNumber;
		city = aCity;
		zipCode = aZipCode;
	}
	public int getApartmentNumber()
	{
		return apartmentNumber;
	}
	public void setApartmentNumber(int aApartmentNumber)
	{
		apartmentNumber = aApartmentNumber;
	}
	public void setStreetAddress(String aStreetAddress)
	{
		streetAddress = aStreetAddress;
	}
	public String getStreetAddress()
	{
		return streetAddress;
	}
	public void setCity(String aCity)
	{
		city = aCity;
	}
	public String getCity()
	{
		return city;
	}
	public void setZipCode(int aZipCode)
	{
		zipCode = aZipCode;
	}
	public int getZipCode()
	{
		return zipCode;
	}
}
