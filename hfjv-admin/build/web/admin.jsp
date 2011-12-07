<%-- 
    Document   : admin
    Created on : Nov 16, 2011, 11:59:43 PM
    Author     : mraghavan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>HFJV Admin | Administration Page</title>
        <script type="text/javascript"> 
            function onLoad()
            {
                //alert("onLoad");
            }
            /* Not used, see staticTextGetFields instead */
            function getFields()
            {
                alert('getFields()');

                var moduleName = '';

                var sel = document.getElementById("moduleList");
                //alert('sel : '+sel);

                var optsLength = sel.options.length;
                //alert('# of elements  : '+optsLength);

                for(var i=0;i<optsLength;i++){
                    //  alert('optsLength[i] text : '+sel.options[i].text);
                    //  alert('optsLength[i] value : '+sel.options[i].value);
                    if(sel.options[i].selected) {
                        moduleName = sel.options[i].value;
                        //  alert(sel.options[i].text);
                        // alert('sel.value : '+sel.options[i].text);
                    }
                }

                alert('module selected is : '+moduleName);

                /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
                /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
                document.getElementsByName('selectedModule')[0].value=moduleName;
                document.getElementsByName('selectedModule')[0].text=moduleName;
                //alert('Hidden element selectedModule value : '+document.getElementsByName('selectedModule').text);
                document.forms[0].submit();
            }

            function staticTextGetFields(moduleName)
            {
                //alert('staticTextGetFields = ENTER');
                //alert('moduleName :  '+moduleName);

                //alert('module selected is : '+moduleName);

                /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
                /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
                document.getElementsByName('selectedModule')[0].value=moduleName;
                document.getElementsByName('selectedModule')[0].text=moduleName;
                //alert('Hidden element selectedModule value : '+document.getElementsByName('selectedModule').text);
                document.forms[0].submit();                
            }

            function staticTextGetConstraints(fieldName)
            {
                //alert('staticTextGetConstraints = ENTER');
                //alert('fieldName :  '+fieldName);

                //alert('field selected is : '+fieldName);

                /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
                /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
                document.getElementsByName('selectedField')[0].value=fieldName;
                document.getElementsByName('selectedField')[0].text=fieldName;
                //alert('Hidden element fieldName value : '+document.getElementsByName('selectedField').text);
                document.forms[1].submit();                
            }

            function staticTextGetConstraintInfo(constraintName)
            {
                //alert('staticTextGetConstraintInfo = ENTER');
                //alert('constraintName :  '+constraintName);

                //alert('constraintName selected is : '+constraintName);

                /* Good tip given at : http://stackoverflow.com/questions/477543/how-do-i-assign-value-to-a-textbox-using-javascript */
                /* You should use [0] whenever you use getElementsByName as it returns an array of elements */
                document.getElementsByName('selectedConstraint')[0].value=constraintName;
                document.getElementsByName('selectedConstraint')[0].text=constraintName;
                //alert('Hidden element constraintName value : '+document.getElementsByName('selectedConstraint').text);
                document.forms[2].submit();                
            }
            function addModule()
            {
                //alert('addModule()');
                document.forms[0].action="admin.jsp";
                document.forms[0].method="POST";
                //alert('document.forms[0].action : '+document.forms[0].action);
                document.forms[0].submit();
            }
            function addField()
            {
               // alert('addField()');
                
                document.forms[1].action="admin.jsp";
                //document.forms[1].url="HFJVAdminServlet";
                document.forms[1].method="POST";
                //alert('document.forms[1].action : '+document.forms[1].action);
                document.forms[1].submit();
            }		
            function addConstraint()
            {
                //alert('addConstraint()');
                
                document.forms[2].action="admin.jsp";
                document.forms[2].method="POST";
                //alert('document.forms[1].action : '+document.forms[1].action);
                document.forms[2].submit();                
            }
            
            function addFieldFormLoad()
            {
                document.getElementsByName('txtFieldName')[0].focus();
            }	
            
        </script>
    </head>
    <body onload="onLoad()">
        <h1>HJFV Admin Page!</h1>
        <font face="Tahoma" size="2" color="grey"/>
        Present Date and Time in Server : <b><font color="RGB(<%= new java.util.Random().nextInt(255) %>,<%= new java.util.Random().nextInt(255) %>,<%= new java.util.Random().nextInt(255) %>)"><%= new java.util.Date()%></font>
            </font></b>
        <br/>
        <%-- Good Link : http://stackoverflow.com/questions/6219730/jstl-conditinal-check --%>
        <hr color="green"/>
        
        <%-- ================================================================ --%>
        <%--                Exception block - starts                          --%>
        <%-- ================================================================ --%>
        <c:if test="${not empty exceptionObj}">
            <jsp:include page="error.jsp">
                <jsp:param name="exceptionObj" value="${exceptionObj}"/>
            </jsp:include>
            <c:remove var="exceptionObj"/>
        </c:if> 
        <%-- ================================================================ --%>
        <%--                Exception  Block - ends                           --%>
        <%-- ================================================================ --%>
        
        <c:choose>
            <c:when test="${not empty moduleList}">
                <%--<b>Debug!</b> <i>moduleList is NOT EMPTY! invoking moduleList.jsp page!</i>
                <br/>--%>
                <jsp:include page="moduleList.jsp"/>
                <c:if test="${param.btnReqAddModule!=null}">
                    <jsp:include page="addModule.jsp"/>
                </c:if>				
            </c:when>
            <%--<c:when test="${pageContext.request.method=='POST'}">
                    <c:if test="${param.btnReqAddModule!=null}">
                            <jsp:include page="addModule.jsp"/>
                    </c:if>
            </c:when>--%>
            <c:otherwise>
                <i>There are <b>No</b> modules configured!</i>
            </c:otherwise>
        </c:choose>
        <%--
        <c:if test="${pageContext.request.method=='POST'}">
        --%>
        <c:choose>
            <c:when test="${not empty fieldList}">
            <%--<c:when test="${fn:length(fieldList) > 0}">--%>
                <br/>
                <jsp:include page="fieldList.jsp"/>
                <c:if test="${param.btnReqAddField!=null}">
                    <jsp:include page="addField.jsp"/>
                </c:if>                 
            </c:when>
             <c:when test="${(not empty controlAction and controlAction eq 'getFields') 
                           or (null==fieldList or fn:length(fieldList)<0)}">
                <br/><b>List of fields : </b><i>There are <b>No</b> fields configured for this module!</i>              
            </c:when>					                       
        </c:choose>
        <c:if test="${not empty fieldConstraintList or not empty dependentFieldValueMap}">
            <br/>
            <jsp:include page="constraintList.jsp" />
            <c:if test="${param.btnReqAddConstraint!=null}">
                <jsp:include page="addConstraint.jsp"/>
            </c:if>
        </c:if>
        <c:if test="${not empty requestScope.constraintObj}">
            <br/>		
            <jsp:include page="constraintInfo.jsp" />
        </c:if>
        <br/><br/>
        <form name="exportHFJVForm" action="HFJVAdminServlet" method="post">            <input type="hidden" name="controlAction" value="export"/>
            <input type="submit" name="btnReqExport" value="Export"/>
        </form>
    </body>
</html>
