package com.kendelong.jmxconsole.web.controller;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import javax.management.Attribute;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanParameterInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectInstance;
import javax.management.ObjectName;

import org.springframework.ui.ModelMap;

import com.kendelong.web.jmxconsole.TypeConverter;

public class MbeanDataRetriever
{
	private MBeanServer server;
	
	public MbeanDataRetriever()
	{
		this(null);
	}
	
	public MbeanDataRetriever(MBeanServer mbeanServer)
	{
		if(mbeanServer != null)
		{
			server = mbeanServer;
		}
		else
		{
			MBeanServerFactory.createMBeanServer();
			server = MBeanServerFactory.findMBeanServer(null).get(0);
		}
	}

	public Object listBeans()
	{
		Set<ObjectInstance> beans = server.queryMBeans(null, null);
		Map<String, List<ObjectInstance>> beansByDomain = beans.stream()
				.collect(groupingBy( (ObjectInstance oi) -> oi.getObjectName().getDomain()));
		

		List<Map<String, Object>> data = beansByDomain.entrySet().stream().sorted((e1, e2) -> e1.getKey().toLowerCase().compareTo(e2.getKey().toLowerCase()))
				.map((Map.Entry<String, List<ObjectInstance>> e) -> 
				{
					Map<String, Object> domain = new HashMap<>();
					domain.put("name", e.getKey());
					domain.put("safeName", e.getKey().replaceAll("[\\.=,]", ""));
					List<ObjectInstance> instances = e.getValue();
					List<Map<String, String>> beanData = instances.stream().map(oi -> oi.getObjectName()).sorted().map((ObjectName oname) -> 
					{
						Map<String, String> vals = new HashMap<>();
						String props = oname.getCanonicalKeyPropertyListString();
						vals.put("name", props);
						vals.put("safeName", props.replaceAll("[\\.=,]", ""));
						return vals;
					})
					.collect(toList());
					domain.put("beans", beanData);
					return domain;
				})
				.collect(toList());
		return data;
		/*
		 * Output is like this:
		 * 		return  [
			[
				"name":"app.hatch.web",
				"safeName": "apphatchweb",
				"beans": [
					[
						"name": "service=ConfigControllerMan",
						"safeName": "serviceConfigController"
					],
					[
						"name": "service=LengthController",
						"safeName": "serviceLengthController"
					]
				]
			],
			[
				"name": "app.hatch.log",
				"beans": [
					[
						"name": "service=logger",
						"safeName": "servicelogger"
					],
					[
						"name": "service=unlogger",
						"safeName": "serviceunlogger"
					]
				]
			]
		];

		 */
	}
	
	public Object getDataForBean(String domain, String bean) throws Exception
	{
		ObjectName oname = new ObjectName(domain + ":" + bean);
		MBeanInfo mbeanInfo = server.getMBeanInfo(oname);

		List<AttributeData> attrs = Stream.of(mbeanInfo.getAttributes())
			.map(attrInfo -> 
			{
				String attrName = attrInfo.getName();
				Object value = null;
				try
				{
					value = server.getAttribute(oname, attrName);
				}
				catch(Exception e)
				{
				}
				AttributeData mba = new AttributeData(attrInfo, value);
				return mba;
			})
			.sorted()
			.collect(toList());
		
		List<OperationData> operations = Stream.of(mbeanInfo.getOperations())
			.map(opInfo ->
			{
				List<OperationParameterData> params = Stream.of(opInfo.getSignature())
					.map(paramInfo -> 
					{
						OperationParameterData param = new OperationParameterData(paramInfo.getName(), paramInfo.getType(), paramInfo.getDescription());
						return param;
					})
					.sorted()
					.collect(toList());
				OperationData opData = new OperationData(opInfo.getName(), opInfo.getDescription(), params);
				return opData;
			})
			.sorted()
			.collect(toList());

		Map<String, Object> results = new HashMap<>();
		results.put("className", mbeanInfo.getClassName());
		results.put("attributes", attrs);
		results.put("operations", operations);
		return results;
		/*
		 * 		return [
			attributes:
			[
				[
					name: "Attr1",
					value: 42,
					type: "int",
					description: "This is a nice integer",
					readOnly: false
				],
				[
					name: "Attr2",
					value: "I am a string",
					type: "string",
					description: "This is very wordy",
					readOnly: true
				]
			],
			operations:
			[
				[
					name: "Operation1",
					description: "Make the thing slower",
					parameters:
					[
						[
							name: "param1",
							type: "java.lang.String",
							description: "The name of the thing"
						],
						[
							name: "param2",
							type: "java.lang.Integer",
							description: "How many are there"
						]
					]
				],
				[
					name: "Operation2",
					description: "Make it all fuzzy",
					parameters:
					[
						[
							name: "param1",
							type: "java.lang.String",
							description: "The name of the thing"
						],
						[
							name: "param2",
							type: "java.lang.Integer",
							description: "How many are there"
						]
					]
				]
			]
		];

		 */
	}
	
	public void updateBeanData(String domain, String bean, Map<String, Object> model) throws Exception
	{
		ObjectName oname = new ObjectName(domain + ":" + bean);
		MBeanInfo mbeanInfo = server.getMBeanInfo(oname);
		for(String name : model.keySet())
		{
			Object value = model.get(name);
			MBeanAttributeInfo info = Stream.of(mbeanInfo.getAttributes()).filter(a -> a.getName().equals(name)).findFirst().get();
			if(info.isWritable())
			{
				Attribute attr = new Attribute(name, TypeConverter.convertObjectValue(info.getType(), value.toString()));
				server.setAttribute(oname, attr);
			}
		}
		
	}

	public String invokeOperation(String domain, String bean, String operation, ModelMap model) throws Exception
	{
		ObjectName oname = new ObjectName(domain + ":" + bean);
		MBeanInfo mbeanInfo = server.getMBeanInfo(oname);
		MBeanOperationInfo opInfo = Stream.of(mbeanInfo.getOperations()).filter(o -> o.getName().equals(operation)).findFirst().get();
		List<Object> params = new ArrayList<Object>();
		List<String> signature = new ArrayList<String>();
		for(MBeanParameterInfo pInfo : opInfo.getSignature())
		{
			Object param = model.get(pInfo.getName());
			String type = pInfo.getType();
			Object oval = TypeConverter.convertObjectValue(type, param.toString());
			params.add(oval);
			signature.add(type);
		}
		Object result = server.invoke(oname, operation, params.toArray(), signature.toArray(new String[0]));
		return result != null ? result.toString() : null;
	}
	
	public static void main(String[] args)
	{
		System.out.println("bean=foo:hey.foo".replaceAll("[\\.=]", ""));
	}
}
