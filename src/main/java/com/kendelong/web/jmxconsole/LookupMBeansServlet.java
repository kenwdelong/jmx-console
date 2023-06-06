package com.kendelong.web.jmxconsole;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LookupMBeansServlet
 */
public class LookupMBeansServlet extends HttpServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
		Set<ObjectInstance> beans = server.queryMBeans(null, null);
		Map<String, List<String>> indexByDomains = new TreeMap<String, List<String>>(new CaseInsensitiveStringComparator());
		for(ObjectInstance bean : beans)
		{
			String domain = bean.getObjectName().getDomain();
			String keyPropertyListString = bean.getObjectName().getKeyPropertyListString();
			List<String> list = indexByDomains.get(domain);
			if(list == null)
			{
				list = new ArrayList<String>();
				indexByDomains.put(domain, list);
			}
			list.add(keyPropertyListString);
		}
		
		for(List<String> propStringList : indexByDomains.values())
		{
			Collections.sort(propStringList);
		}
		request.setAttribute("beans", indexByDomains);
		request.getRequestDispatcher("listBeans.jsp").forward(request, response);
	}
	

}
