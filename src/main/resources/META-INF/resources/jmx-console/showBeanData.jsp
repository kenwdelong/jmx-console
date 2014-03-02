<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="master.css" type="text/css">
<title>MBean Data</title>
</head>
<body>
<p style="float:right;"><a href="/jmx-console">Back To Agent</a></p>
<p style="float:right; clear:right;"><a href="${request.requestURL}">Refresh this Page</a></p>
<h1>${instance.objectName}</h1>
<p><strong>Class</strong>: ${instance.className}</p>
<p><strong>Description</strong>: ${info.description}</p>

<h2>Attributes</h2>
<form action="updateAttributes" method="POST">
<table cellspacing="1" cellpadding="1" border="1">
	<tbody>
		<tr>
			<th>Name</th><th>Type</th><th>Access</th><th>Value</th><th>Description</th>
		</tr>
		<c:forEach items="${attributes}" var="attr">
			<tr>
				<td>${attr.info.name}</td>
				<td>${attr.info.type}</td>
				<td>${attr.access}</td>
				<td>
					<c:if test="${attr.info.writable}">
						<input type="text" name="attr_${attr.info.name}" value="${attr.value}" size="30"/>
					</c:if>
					<c:if test="${not attr.info.writable}">
						${attr.value}				
					</c:if>
				</td>
				<td>${attr.info.description}</td>
			</tr>
		</c:forEach>
	</tbody>
</table>
<input type="hidden" name="beanName" value="${instance.objectName}"/>
<input type="submit" value="Update Values"/>
</form>

<h2>Operations</h2>
<c:forEach items="${operations}" var="op">
<h4>${op.name}()</h4>
<p>${op.description}</p>
<form action="invokeOperation" method="GET">
<c:if test="${not empty op.signature}">
<table  cellspacing="1" cellpadding="1" border="1">
	<tr>
		<th>Param</th><th>Param Type</th><th>Param Value</th><th>Param Description</th>
		<c:forEach items="${op.signature}" var="parm" varStatus="status">
			<tr>
				<td>${parm.name}</td>
				<td>${parm.type}</td>
				<td><input type="text" name="arg${status.index}"/></td>
				<td>${parm.description}</td>
			</tr>
		</c:forEach>
	</tr>
</table>
</c:if>
<input type="hidden" name="beanName" value="${instance.objectName}"/>
<input type="hidden" name="opName" value="${op.name}"/>
<input type="submit" value="Invoke"/>
</form>
<hr/>
</c:forEach>


</body>
</html>