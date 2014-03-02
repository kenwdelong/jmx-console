package com.bc.web.jmxconsole;

import java.util.Comparator;

import javax.management.MBeanOperationInfo;

public class OperationInfoComparator implements Comparator<MBeanOperationInfo>
{

	@Override
	public int compare(MBeanOperationInfo o1, MBeanOperationInfo o2)
	{
		return o1.getName().toLowerCase().compareTo(o2.getName().toLowerCase());
	}

}
