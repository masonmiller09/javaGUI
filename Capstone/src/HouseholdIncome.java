
public class HouseholdIncome
{
	private int income;
	private int weekly;
	private int monthly;
	private int yearly;
	
	public HouseholdIncome(int aIncome, int aWeekly, int aMonthly, int aYearly)
	{
		setIncome(aIncome);
		setWeekly(aWeekly);
		setMonthly(aMonthly);
		setYearly(aYearly);
	}

	public int getIncome()
	{
		return income;
	}

	public void setIncome(int aIncome)
	{
		income = aIncome;
	}

	public int isWeekly()
	{
		return weekly;
	}

	public void setWeekly(int aWeekly)
	{
		weekly = aWeekly;
	}

	public int isMonthly()
	{
		return monthly;
	}

	public void setMonthly(int aMonthly)
	{
		monthly = aMonthly;
	}

	public int isYearly()
	{
		return yearly;
	}

	public void setYearly(int aYearly)
	{
		yearly = aYearly;
	}
}
