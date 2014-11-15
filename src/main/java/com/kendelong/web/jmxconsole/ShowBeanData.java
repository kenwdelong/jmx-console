package com.kendelong.web.jmxconsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ShowBeanData
 */
public class ShowBeanData extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String beanName = request.getParameter("beanName");
		ObjectInstance objectInstance;
		MBeanInfo mbeanInfo;
		List<MBeanAttribute> attributes = new ArrayList<MBeanAttribute>();
		List<MBeanOperationInfo> operations = new ArrayList<MBeanOperationInfo>();

		try
		{
			MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
			ObjectName oname = new ObjectName(beanName);
			objectInstance = server.queryMBeans(oname, null).iterator().next();
			mbeanInfo = server.getMBeanInfo(oname);
			
			for(MBeanAttributeInfo attrInfo: mbeanInfo.getAttributes())
			{
				String attrName = attrInfo.getName();
				Object value = server.getAttribute(oname, attrName);
				MBeanAttribute mba = new MBeanAttribute(attrInfo, value);
				attributes.add(mba);
			}
			Collections.sort(attributes);
			
			for(MBeanOperationInfo opInfo : mbeanInfo.getOperations())
			{
				operations.add(opInfo);
			}
			Collections.sort(operations, new OperationInfoComparator());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw new ServletException(e);
		}
		
		request.setAttribute("instance", objectInstance);
		request.setAttribute("info", mbeanInfo);
		request.setAttribute("attributes", attributes);
		request.setAttribute("operations", operations);
		request.getRequestDispatcher("showBeanData.jsp").forward(request, response);
	}

}
