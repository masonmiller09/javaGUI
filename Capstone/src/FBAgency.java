import java.util.ArrayList;


public class FBAgency
{
	private int agencyID;
	private int accountNum;
	private String agencyName;
	private ArrayList<FBAgencyRep> agencyReps = new ArrayList<FBAgencyRep>();
	
	public FBAgency(int aAccountNum, String aAgencyName)
	{
		accountNum = aAccountNum;
		agencyName = aAgencyName;
	}
	
	public FBAgency(int aAccountNum, String aAgencyName, ArrayList<FBAgencyRep> aAgencyReps)
	{
		accountNum = aAccountNum;
		agencyName = aAgencyName;
		agencyReps = aAgencyReps;
	}
	public int getAgencyID()
	{
		return agencyID;
	}
	public void setAgencyID(int aAgencyID)
	{
		agencyID = aAgencyID;
	}
	public int getAccountNum()
	{
		return accountNum;
	}
	public void setAccountNum(int aAccountNum)
	{
		accountNum = aAccountNum;
	}
	public String getAgencyName()
	{
		return agencyName;
	}
	public void setAgencyName(String aAgencyName)
	{
		agencyName = aAgencyName;
	}
	public ArrayList<FBAgencyRep> getAgencyReps()
	{
		return agencyReps;
	}
	public void setAgencyReps(ArrayList<FBAgencyRep> aAgencyReps)
	{
		agencyReps = aAgencyReps;
	}
	public void addAgencyRep(FBAgencyRep aAgencyRep)
	{
		agencyReps.add(aAgencyRep);
	}
}
