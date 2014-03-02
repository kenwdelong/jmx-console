<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="master.css" type="text/css">
<title>Operation Result</title>
</head>
<body>
<p><a href="/jmx-console">Back To Agent</a></p>
<p><a href="${beanUrl}">Back to MBean</a></p>

<p><strong>Bean</strong>: ${beanName}</p>
<p><strong>Operation</strong>: ${opName}</p>
<c:if test="${not empty opParams}">
<strong>Parameters</strong>:
<ul>
	<c:forEach items="${opParams}" var="opparam">
		<li>${opparam}</li>
	</c:forEach>
</ul>
</c:if>
<p><a href="${request.requestURL}">Reinvoke this Operation</a></p>

<strong>Result</strong>: ${result}

</body>
</html>