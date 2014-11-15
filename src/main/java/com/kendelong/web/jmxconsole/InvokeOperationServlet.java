package com.kendelong.web.jmxconsole;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class InvokeOperationServlet
 */
public class InvokeOperationServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		String beanName = request.getParameter("beanName");
		String encparm = URLEncoder.encode(beanName, "UTF-8");
		try
		{
			MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
			ObjectName oname = new ObjectName(beanName);
			String opName = request.getParameter("opName");
			MBeanInfo mbeanInfo = server.getMBeanInfo(oname);
			MBeanOperationInfo[] opInfos = mbeanInfo.getOperations();
			// Who thought up this API?  Geez
			MBeanOperationInfo opInfo = null;
			for(MBeanOperationInfo i : opInfos)
			{
				if(i.getName().equals(opName))
				{
					opInfo = i;
					break;
				}
			}
			List<Object> params = new ArrayList<Object>();
			List<String> signature = new ArrayList<String>();
			int count = 0;
			String parm = null;
			while((parm = request.getParameter("arg" + count++)) != null)
			{
				MBeanParameterInfo parmInfo = opInfo.getSignature()[count-1];
				String type = parmInfo.getType();
				Object oval = TypeConverter.convertObjectValue(type, parm);
				params.add(oval);
				signature.add(type);
			}
			Object result = server.invoke(oname, opName, params.toArray(), signature.toArray(new String[0]));
			
			request.setAttribute("result", result);
			request.setAttribute("beanName", beanName);
			request.setAttribute("opName", opName);
			request.setAttribute("opParams", params);
			request.setAttribute("beanUrl", "/jmx-console/showBean?beanName=" + encparm);
			request.getRequestDispatcher("operationResult.jsp").forward(request, response);
		}
		catch(Exception e)
		{
			e.printStackTrace();
			response.sendRedirect("/showBean?beanName=" + encparm);
		}
	}

}
