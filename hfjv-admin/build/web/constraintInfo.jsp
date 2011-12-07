<%-- 
    Document   : constraintInfo
    Created on : Nov 19, 2011, 8:43:10 PM
    Author     : mraghavan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<b>Constraint Info </b> :  
<br/>
Constraint Name :  <i>${requestScope.constraintObj.name}</i>
<br/> 
Constraint Value :  <i>${constraintObj.valueToCheck} </i>
<br/> 
<%--Constraint Class : <i>${constraintObj}  </i>--%>