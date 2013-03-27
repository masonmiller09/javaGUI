import java.util.ArrayList;


public class FBAgencyOld
{
	private int agencyID;
	private int accountNum;
	private String agencyName;
	private ArrayList<FBAgencyRepOld> agencyReps = new ArrayList<FBAgencyRepOld>();
	
	public FBAgencyOld(int aAccountNum, String aAgencyName)
	{
		accountNum = aAccountNum;
		agencyName = aAgencyName;
	}
	
	public FBAgencyOld(int aAccountNum, String aAgencyName, ArrayList<FBAgencyRepOld> aAgencyReps)
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
	public ArrayList<FBAgencyRepOld> getAgencyReps()
	{
		return agencyReps;
	}
	public void setAgencyReps(ArrayList<FBAgencyRepOld> aAgencyReps)
	{
		agencyReps = aAgencyReps;
	}
	public void addAgencyRep(FBAgencyRepOld aAgencyRep)
	{
		agencyReps.add(aAgencyRep);
	}
}
