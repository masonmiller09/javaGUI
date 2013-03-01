import java.sql.Date;

public class FBMonthlyDistribution
{
	private FBCustomer customer;
	private FBAgency agency;
	private FBAgencyRep agencyRep;
	private Date date;

	public FBMonthlyDistribution(FBCustomer aCustomer, FBAgency aAgency, FBAgencyRep aRep, Date aDate)
	{
		setCustomer(aCustomer);
		setAgency(aAgency);
		setAgencyRep(aRep);
		setDate(aDate);
	}

	public FBCustomer getCustomer()
	{
		return customer;
	}

	public void setCustomer(FBCustomer aCustomer)
	{
		customer = aCustomer;
	}

	public FBAgency getAgency()
	{
		return agency;
	}

	public void setAgency(FBAgency aAgency)
	{
		agency = aAgency;
	}

	public FBAgencyRep getAgencyRep()
	{
		return agencyRep;
	}

	public void setAgencyRep(FBAgencyRep aAgencyRep)
	{
		agencyRep = aAgencyRep;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date aDate)
	{
		date = aDate;
	}
}
