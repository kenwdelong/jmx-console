package test;

public interface HelloMBean
{
	public int getCount();
	public void setCount(int c);
	
	public String getMessage();
	public void setMessage(String s);
	
	public boolean isLittleBool();
	public void setLittleBool(boolean b);
	
	public Boolean getBigBool();
	public void setBigBool(Boolean b);

	public Integer getBigCount();
	public void setBigCount(Integer c);
	
	public String increment(int amt);
	
}
