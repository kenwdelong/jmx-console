package com.kendelong.jmxconsole.web.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.management.MBeanServer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

@RestController
@RequestMapping("/admin/jmx")
public class JmxConsoleController
{
	private MbeanDataRetriever dataRetriever;
	
	@Autowired
	private MBeanServer mbeanServer;
	
	@PostConstruct
	public void init()
	{
		dataRetriever = new MbeanDataRetriever(mbeanServer);
	}
	
	@RequestMapping(method = RequestMethod.GET, produces="text/html")
	public String getMainPage() throws IOException
	{
		InputStream in = JmxConsoleController.class.getResourceAsStream("index.html");
		InputStreamReader is = new InputStreamReader(in);
		StringBuilder sb = new StringBuilder();
		BufferedReader br = new BufferedReader(is);
		String read = br.readLine();

		while(read != null)
		{
			sb.append(read).append("\n");
			read = br.readLine();
		}

		return sb.toString();
	}

	@RequestMapping(value = "/beans", method = RequestMethod.GET, produces="application/json")
	public Object listAllBeans()
	{
		return dataRetriever.listBeans();
	}
	
	@RequestMapping(value = "/bean/{domain}/{bean}", method = RequestMethod.GET, produces="application/json")
	public Object getBeanData(@PathVariable String domain, @PathVariable String bean) throws Exception
	{
		return dataRetriever.getDataForBean(domain, unescapeBeanName(bean));
	}

	@RequestMapping(value = "/bean/{domain}/{bean}", method = RequestMethod.POST)
	public Object updateBeanData(@PathVariable String domain, @PathVariable String bean, @RequestBody ModelMap model) throws Exception
	{
		dataRetriever.updateBeanData(domain, unescapeBeanName(bean), model);
		return "";
	}
	
	@RequestMapping(value = "/bean/{domain}/{bean}/{operation}", method = RequestMethod.POST, produces="text/plain")
	public String invokeOperation(@PathVariable String domain, @PathVariable String bean, @PathVariable String operation, @RequestBody ModelMap model) throws Exception
	{
		String result = dataRetriever.invokeOperation(domain, unescapeBeanName(bean), operation, model);
		return result;
	}
	
	private String unescapeBeanName(String encoded)
	{
		return encoded.replaceAll("__", "/").replaceAll("--", ".");
	}
	
}
