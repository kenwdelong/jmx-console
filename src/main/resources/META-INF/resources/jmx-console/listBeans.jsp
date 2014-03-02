<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="master.css" type="text/css">
<title>JMX Console</title>
</head>
<body>
<h1>JMX MBeans</h1>
<c:forEach items="${beans}" var="domainEntry">
<h2>${domainEntry.key}</h2>
	<ul>
	<c:forEach items="${domainEntry.value}" var="bean">
		<c:url value="/jmx-console/showBean" var="url">
			<c:param name="beanName" value="${domainEntry.key}:${bean}"/>
		</c:url>
		<li><a href="${url}">${bean}</a></li>
	</c:forEach>
	</ul>
</c:forEach>

</body>
</html>