<%-- 
    Document   : constraintList
    Created on : Nov 19, 2011, 8:41:10 PM
    Author     : mraghavan
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<form name="constraintSelectForm" action="HFJVAdminServlet" method="post">
    <b>List of Constraints </b> : 
    <c:choose>
            <c:when test="${not empty fieldConstraintList}">
                    <input type="hidden" name="controlAction" value="getConstraintInfo"/>
                    <input type="hidden" name="selectedConstraint" value=""/>            
                    <c:forEach var="fieldConstraint" items="${fieldConstraintList}" varStatus="status">
                        <c:choose>
                            <c:when test="${fieldConstraint eq selectedConstraint}">
                                <a href="javascript:staticTextGetConstraintInfo('<c:out value="${fieldConstraint.name}"/>')">
                                    <span style="background-color: #CCFF99"><b><u><i>${fieldConstraint.name}</i></u></b></span></a> | 
                            </c:when>
                            <c:otherwise>
                                <a href="javascript:staticTextGetConstraintInfo('<c:out value="${fieldConstraint.name}"/>')">${fieldConstraint.name}</a> | 
                            </c:otherwise>                            
                        </c:choose>                                    
                    </c:forEach>
                    <input type="submit" name="btnReqAddConstraint" value="Add a new constraint" onclick="addConstraint()"/>                                    
            </c:when>
            <c:otherwise>
                    <i>There are <b>No</b> constraints specified for this field!</i> <br/>
            </c:otherwise>
    </c:choose>
    <c:if test="${not empty dependentFieldValueMap}">
    <br/>
    <b>Dependent Fields Info : </b>
            <c:forEach var="item" items="${dependentFieldValueMap}" varStatus="status">
                    <%-- ${dependentField.value} |  --%>
                    [<i><span style="background-color: #FFFFCC; color:black;"><b>${fn:substring(item.key,fn:indexOf(item.key,'-')+1,-1)}</b></span></i> 
                    with a value of 
                    <span style="background-color: #FFFFCC; color:black;"><i><b>'${item.value}'</b></i></span>] | 
            </c:forEach>
    </c:if>
</form>   
