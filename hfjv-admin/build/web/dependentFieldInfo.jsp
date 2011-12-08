<%-- 
    Document   : dependentFieldInfo
    Created on : Dec 8, 2011, 11:12:17 AM
    Author     : mraghavan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<br/>
<b>Dependent Fields Info : </b>
<c:forEach var="item" items="${dependentFieldValueMap}" varStatus="status">
    <%-- ${dependentField.value} |  --%>
    [<i><span style="background-color: #FFFFCC; color:black;">
            <b>${fn:substring(item.key,fn:indexOf(item.key,'-')+1,-1)}</b>
        </span></i> 
    with a value of 
    <span style="background-color: #FFFFCC; color:black;">
        <i><b>'${item.value}'</b></i>
    </span>] | 
</c:forEach>
<%--Constraint Class : <i>${constraintObj}  </i>--%>
