package com.kendelong.jmxconsole.web.controller;

public class OperationParameterData implements Comparable<OperationParameterData>
{
	private String name;
	private String type;
	private String description;

	public OperationParameterData(String name, String type, String description)
	{
		super();
		this.name = name;
		this.type = type;
		this.description = description;
	}

	public String getName()
	{
		return name;
	}

	public String getType()
	{
		return type;
	}

	public String getDescription()
	{
		return description;
	}

	@Override
	public int compareTo(OperationParameterData o)
	{
		return this.getName().compareTo(o.getName());
	}

}
