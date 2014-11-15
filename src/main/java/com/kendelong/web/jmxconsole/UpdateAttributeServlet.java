package com.kendelong.web.jmxconsole;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Servlet implementation class UpdateAttributeServlet
 */
public class UpdateAttributeServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException
	{
		String beanName = null;
		try
		{
			MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
			beanName = request.getParameter("beanName");
			ObjectName oname = new ObjectName(beanName);
			MBeanInfo mbeanInfo = server.getMBeanInfo(oname);
			Map<String, MBeanAttributeInfo> attrInfoMap = new HashMap<String, MBeanAttributeInfo>();
			for(MBeanAttributeInfo ai : mbeanInfo.getAttributes())
			{
				attrInfoMap.put(ai.getName(), ai);
			}
			
			Enumeration<String> paramNames = request.getParameterNames();
			while(paramNames.hasMoreElements())
			{
				String name = paramNames.nextElement();
				if(name.startsWith("attr_"))
				{
					String attributeName = name.split("_")[1];
					String value = request.getParameterValues(name)[0];
					Object objectValue = TypeConverter.convertObjectValue(attrInfoMap.get(attributeName).getType(), value);
					Attribute attribute = new Attribute(attributeName, objectValue);
					try
					{
						server.setAttribute(oname, attribute);
					}
					catch(Exception e)
					{
						System.out.println("Couldn't set " + attributeName);
					}
				}
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		String parm = URLEncoder.encode(beanName, "UTF-8");
		response.sendRedirect("/jmx-console/showBean?beanName=" + parm);
	}


}
