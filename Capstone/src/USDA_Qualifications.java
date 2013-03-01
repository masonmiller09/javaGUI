
public class USDA_Qualifications
{
	private int foodStamps = 0;
	private int tANF = 0;
	private int sSI = 0;
	private int medicaid = 0;
	public USDA_Qualifications(int aFoodStamps, int aTANF, int aSSI, int aMedicaid)
	{
		foodStamps = aFoodStamps;
		tANF = aTANF;
		sSI = aSSI;
		medicaid = aMedicaid;
	}
	public int isFoodStamps()
	{
		return foodStamps;
	}
	public void setFoodStamps(int aFoodStamps)
	{
		foodStamps = aFoodStamps;
	}
	public int isTANF()
	{
		return tANF;
	}
	public void setTANF(int aTANF)
	{
		tANF = aTANF;
	}
	public int isSSI()
	{
		return sSI;
	}
	public void setSSI(int aSSI)
	{
		sSI = aSSI;
	}
	public int isMedicaid()
	{
		return medicaid;
	}
	public void setMedicaid(int aMedicaid)
	{
		medicaid = aMedicaid;
	}
}