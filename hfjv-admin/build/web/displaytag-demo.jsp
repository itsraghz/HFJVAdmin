<%-- 
    Document   : displaytag-demo
    Created on : Dec 7, 2011, 8:44:05 PM
    Author     : mraghavan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="display" uri="http://displaytag.sf.net" %>
<%@page import="java.util.ArrayList, java.util.Arrays" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>DisplayTag - Demo</title>
    </head>
    <body>
        <% 
            ArrayList<String> listOfNames = new ArrayList<String>();
            String[] namesArray = new String[]{"Raghs", "Saro","Sathish"};
            listOfNames = (ArrayList<String>)Arrays.asList(namesArray);
            request.setAttribute("listOfNames",listOfNames);            
        %>
        <display:table name="listOfNames" />
    </body>
</html>
