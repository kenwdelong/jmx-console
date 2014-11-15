package com.kendelong.web.jmxconsole;

import javax.management.MBeanAttributeInfo;

public class MBeanAttribute implements Comparable<MBeanAttribute>
{
	private MBeanAttributeInfo info;
	private Object value;

	public MBeanAttribute(MBeanAttributeInfo info, Object value)
	{
		super();
		this.info = info;
		this.value = value;
	}

	public MBeanAttributeInfo getInfo()
	{
		return info;
	}

	public void setInfo(MBeanAttributeInfo info)
	{
		this.info = info;
	}

	public Object getValue()
	{
		return value;
	}

	public void setValue(Object value)
	{
		this.value = value;
	}

	@Override
	public int compareTo(MBeanAttribute o)
	{
		return this.info.getName().toLowerCase().compareTo(o.info.getName().toLowerCase());
	}
	
	public String getAccess()
	{
		String access = "";
		if(info.isReadable()) access += "R";
		if(info.isWritable()) access += "W";
		return access;
	}
	
}
