package test;

public class Hello implements HelloMBean
{
	private int count;
	private String message;
	private boolean littleBool;
	private Boolean bigBool;
	private Integer bigCount;
	
	@Override
	public int getCount()
	{
		return count;
	}

	@Override
	public void setCount(int c)
	{
		count = c;
	}

	@Override
	public String getMessage()
	{
		return message;
	}

	@Override
	public void setMessage(String s)
	{
		message = s;
	}

	@Override
	public boolean isLittleBool()
	{
		return littleBool;
	}

	@Override
	public void setLittleBool(boolean b)
	{
		littleBool = b;
	}

	@Override
	public Boolean getBigBool()
	{
		return bigBool;
	}

	@Override
	public void setBigBool(Boolean b)
	{
		bigBool = b;
	}

	@Override
	public Integer getBigCount()
	{
		return bigCount;
	}

	@Override
	public void setBigCount(Integer c)
	{
		bigCount = c;
	}

	@Override
	public String increment(int amt)
	{
		count += amt;
		return "It worked";
	}

}
