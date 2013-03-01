
public class FamilyMembers
{
	private int numChildren = 0;
	private int numAdults = 0;
	private int numSeniors = 0;
	private int numTotalFamily = 0;
	
	public FamilyMembers(int children, int adults, int seniors)
	{
		numChildren = children;
		numAdults = adults;
		numSeniors = seniors;
		numTotalFamily = numChildren + numAdults + numSeniors;
	}
	public int getNumberOfChildren()
	{
		return numChildren;
	}
	public int getNumberOfAdults()
	{
		return numAdults;
	}
	public int getNumberOfSeniors()
	{
		return numSeniors;
	}
	public int getFamilyTotal()
	{
		return numTotalFamily;
	}
	public void setNumberOfChildren(int children)
	{
		numChildren = children;
	}
	public void setNumberOfAdults(int adults)
	{
		numAdults = adults;
	}
	public void setNumberOfSeniors(int seniors)
	{
		numSeniors = seniors;
	}
}
