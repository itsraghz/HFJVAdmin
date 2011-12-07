<%-- 
    Document   : moduleList
    Created on : Nov 19, 2011, 8:35:51 PM
    Author     : mraghavan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form name="moduleSelectForm" action="HFJVAdminServlet" method="post">
	<!--<select id="moduleList" onchange="getFields()">
		<option name="0">--Select One--</option>
		<%-- <c:forEach var="module" items="${moduleList}">
			<option name="${module}">${module}</option>
		</c:forEach> --%>
	</select>-->               
	<b>List of modules configured</b> : 
	<input type="hidden" name="controlAction" value="getFields"/>
	<input type="hidden" name="selectedModule" value=""/>
	<c:forEach var="module" items="${moduleList}" varStatus="status">
            <c:choose>
		<c:when test="${module eq selectedModule}">
                    <a href="javascript:staticTextGetFields('<c:out value="${module}"/>')">
                        <span style="background-color: #CCFF99"><b><u><i>${module}</i></u></b></span></a> | 
		</c:when>
		<c:otherwise>
                    <a href="javascript:staticTextGetFields('<c:out value="${module}"/>')">${module}</a> | 
		</c:otherwise>
            </c:choose>
		<%-- <c:if test="${status.current ne status.last}"> | </c:if> --%>
	</c:forEach>
	<input type="submit" name="btnReqAddModule" value="Add a new module" onclick="addModule()"/>
</form>
