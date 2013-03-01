
public class FBAgencyRep
{
	private int agencyRepID;
	private int agencyID;
	private String lastName;
	private String firstName;
	
	public FBAgencyRep(String aLastName, String aFirstName)
	{
		lastName = aLastName;
		firstName = aFirstName;
	}
	
	public FBAgencyRep(int aAgencyID, String aLastName, String aFirstName)
	{
		agencyID = aAgencyID;
		lastName = aLastName;
		firstName = aFirstName;
	}

	public int getAgencyID()
	{
		return agencyID;
	}

	public void setAgencyID(int aAgencyID)
	{
		agencyID = aAgencyID;
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
	public void setAgencyRepID(int aID)
	{
		agencyRepID = aID;
	}
}
