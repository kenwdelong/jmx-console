package com.bc.web.jmxconsole;


public class TypeConverter
{
	// Surely this can't be necessary...
	public static Object convertObjectValue(String type, String value)
	{
		if(type.equals("java.lang.String"))
		{
			return value;
		}
		else if(type.equals("int") || type.equals("java.lang.Integer"))
		{
			return Integer.valueOf(value);
		}
		else if(type.equals("boolean") || type.equals("java.lang.Boolean"))
		{
			return Boolean.valueOf(value);
		}
		else if(type.equals("long") || type.equals("java.lang.Long"))
		{
			return Long.valueOf(value);
		}
		else if(type.equals("float") || type.equals("java.lang.Float"))
		{
			return Float.valueOf(value);
		}
		else if(type.equals("double") || type.equals("java.lang.Double"))
		{
			return Double.valueOf(value);
		}
		else
		{
			System.out.println("Cannot convert " + type);
			return null;
		}
	}

}
