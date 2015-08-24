package com.kendelong.jmxconsole.web.controller;

import java.util.ArrayList;
import java.util.List;

public class OperationData implements Comparable<OperationData>
{
	private String name;
	private String description;
	private List<OperationParameterData> parameters = new ArrayList<>();
	
	public OperationData(String name, String description, List<OperationParameterData> params)
	{
		super();
		this.name = name;
		this.description = description;
		this.parameters = params;
	}

	public String getName()
	{
		return name;
	}

	public String getDescription()
	{
		return description;
	}

	public List<OperationParameterData> getParameters()
	{
		return parameters;
	}

	@Override
	public int compareTo(OperationData o)
	{
		return this.getName().compareTo(o.getName());
	}
	
	public void addParameter(OperationParameterData p)
	{
		parameters.add(p);
	}
	
	
}
