JMX Console
===========

This is simple JMX MBean viewer that can be used in Spring Boot (Version 2) or in the form of a JARred web fragment that can be
added to a WAR file (Version 1).  Building Version 1 results
in a JAR file, simply dropping that JAR file into your WEB-INF/lib directory will expose the JMX beans at the
url /jmx-console.


Bugs
----

There's one weird bug in version 1 I haven't been able to figure out: although the JSPs in META-INF/resources/jmx-console are visible
through the web browser, the master.css file always returns a 404.  If you figure this out, please fork the project
and issue a pull request.  I'd appreciate it!

Notes
-----

I don't make any representation that this is actually very good code.  It's written with bare servlets and jsp with no frameworks or
abstractions. It's just a little thing to visualize JMX Mbeans.

Maven
-----
Artifact is available in Maven Central under

	<dependency>
		<groupId>com.github.kenwdelong</groupId>
		<artifactId>jmx-console</artifactId>
		<version>2.0.1</version>
	</dependency>

Releases
--------
- 2.0.1: (June 7, 2016) - Made it possible to subclass `JmxConsoleController` so that you can just override the path mapping.
- 2.0.0: (Aug 24, 2015) - Adapted for use in Spring Boot, brand new UI (see below)
- 1.1.0: This release is for Java 1.8 and Servlet 3.1
- 1.0.0: This release is for Java 1.7, and Servlet 3.0

## Version 2.0
Version 2.0 has a new UI. In version 1, the UI was exposed through web fragments - just drop the JAR into the project and the servlets were added to the web configuration.  Most modern Spring apps are going to be using Spring Boot; unfortunately, web fragments don't work with Boot's internal web container architecture. So I added a new UI independent of web fragments. Spring-webmvc is now a dependency of the project; however, if you not using Boot you can exclude this dependency and just use the old web fragment style.  None of that code has changed.

### Configuration
There is a single Spring controller in the package `com.kendelong.jmxconsole.web.controller` called `JmxConsoleController`; just mount that in your application context. You can either instantiate the bean directly in XML, Java config, or add the package to your component scan path.

The new UI appears at `/admin/jmx`. If you need to change that, you'll need to write your own controller, or subclass the existing one and change the root request mapping.  You can use the `MBeanDataRetriever` to do the work. It requires a reference to the MBeanServer passed in the constructor. The controller gets it from Spring.

### Screenshot

![screenshot](https://raw.github.com/kenwdelong/jmx-console/master/misc/Console.jpg)

#### Design
The initial view of the page just loads the names of the MBeans.  When you expand an individual MBean entry, it goes back to the server to fetch the data about that MBean. This is because each MBean has different attributes and operations, so the MBeanServer needs to be queried.  Querying for every MBean, and rendering all that HTML, would be slow and inefficient. When the MBean entry is expanded, the DOM is created for it on the fly, and when you close the entry that DOM is removed. That means if you open and close an entry the data is refreshed from the server.
	