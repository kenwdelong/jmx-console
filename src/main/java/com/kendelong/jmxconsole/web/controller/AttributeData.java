package com.kendelong.jmxconsole.web.controller;

import javax.management.MBeanAttributeInfo;

public class AttributeData implements Comparable<AttributeData>
{
	private MBeanAttributeInfo info;
	private Object value;
	
	public AttributeData(MBeanAttributeInfo info, Object value)
	{
		super();
		this.info = info;
		this.value = value;
	}
	
	public String getName()
	{
		return info.getName();
	}
	
	public String getValue()
	{
		return value != null ? value.toString() : "";
	}
	
	public String getType()
	{
		return info.getType();
	}
	
	public String getDescription()
	{
		return info.getDescription();
	}
	
	public boolean isReadOnly()
	{
		return !info.isWritable();
	}

	@Override
	public int compareTo(AttributeData o)
	{
		return this.getName().toLowerCase().compareTo(o.getName().toLowerCase());
	}
}
