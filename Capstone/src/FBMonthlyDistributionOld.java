import java.sql.Date;

public class FBMonthlyDistributionOld
{
	private FBCustomerOld customer;
	private FBAgencyOld agency;
	private FBAgencyRepOld agencyRep;
	private Date date;

	public FBMonthlyDistributionOld(FBCustomerOld aCustomer, FBAgencyOld aAgency, FBAgencyRepOld aRep, Date aDate)
	{
		setCustomer(aCustomer);
		setAgency(aAgency);
		setAgencyRep(aRep);
		setDate(aDate);
	}

	public FBCustomerOld getCustomer()
	{
		return customer;
	}

	public void setCustomer(FBCustomerOld aCustomer)
	{
		customer = aCustomer;
	}

	public FBAgencyOld getAgency()
	{
		return agency;
	}

	public void setAgency(FBAgencyOld aAgency)
	{
		agency = aAgency;
	}

	public FBAgencyRepOld getAgencyRep()
	{
		return agencyRep;
	}

	public void setAgencyRep(FBAgencyRepOld aAgencyRep)
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
