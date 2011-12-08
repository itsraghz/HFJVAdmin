<%-- 
    Document   : fieldList
    Created on : Nov 19, 2011, 8:39:10 PM
    Author     : mraghavan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form name="fieldSelectForm" action="HFJVAdminServlet" method="post">
		<!--<select id="fieldList" onchange="">
			<option name="0">--Select One--</option>
		<%--<c:forEach var="field" items="${fieldList}">
			<option name="${field}">${field}</option>
		</c:forEach> --%>                   
		</select> -->
	<b>List of fields </b> : 
	<c:choose>
            <c:when test="${not empty fieldList}">
                <input type="hidden" name="controlAction" value="getConstraints"/>
                <input type="hidden" name="selectedField" value=""/>            
                <c:forEach var="field" items="${fieldList}" varStatus="status">
                    <c:choose>
                        <c:when test="${field eq selectedField}">
                            <a href="javascript:staticTextGetConstraints('<c:out value="${field}"/>')">
                                <span style="background-color: #CCFF99"><b><u><i>${field}</i></u></b></span></a> | 
                        </c:when>
                        <c:otherwise>
                            <a href="javascript:staticTextGetConstraints('<c:out value="${field}"/>')">${field}</a> | 
                        </c:otherwise>
                    </c:choose>                            
                </c:forEach>
                <input type="submit" name="btnReqAddField" value="Add a new field" onclick="addField()"/>
                <br/>
                <c:if test="${null!=selectedFieldObj}">
                    <br/>
                    <b>Field Properties : </b>
                    <br/>
                    <b>Name : </b> <i>${selectedFieldObj.name}</i> |
                   <b>Type : </b> <i>${selectedFieldObj.type}</i>
                    <%--<c:remove var="selectedFieldObj"/>--%>
                </c:if>                
            </c:when>
            <c:otherwise>
                    <i>There are <b>No</b> fields configured for this module</i>
            </c:otherwise>
	</c:choose>
</form>    
