package test;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class Loader implements ServletContextListener
{

	@Override
	public void contextInitialized(ServletContextEvent sce)
	{
		try
		{
			MBeanServer server = MBeanServerFactory.findMBeanServer(null).get(0);
			Hello h = new Hello();
			ObjectName oname = new ObjectName("jmxtest:service=hello");
			server.registerMBean(h, oname);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce)
	{
	}

}
