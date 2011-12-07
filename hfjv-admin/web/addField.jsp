<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="HFJVAdminServlet" name="formAddField" method="post" onload="addFieldFormLoad()">
    <table border="0" cellspacing="5" cellpadding="5">
        <tr>
            <td>
                Enter Field Name : 
            </td>
            <td>
                <input type="text" name="txtFieldName" value="" size="20"/>
                <select id="reqFieldTypeList" name="reqFieldTypeList">
                    <c:forEach var="hfjvFieldType" items="${hfjvFieldTypeList}">
                        <option name="${hfjvFieldType}">${hfjvFieldType}</option>
                    </c:forEach>
                </select>
                <input type="hidden" name="controlAction" value="addField"/>
                <input type="submit" name="btnAddField" value="Submit"/>
            </td>
        </tr>
    </table>
</form>
