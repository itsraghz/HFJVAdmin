<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<form action="HFJVAdminServlet" name="formAddDependentField" method="post">
    <table border="1" cellspacing="5" cellpadding="5">
        <tr>
            <th>Field</th>
            <th>Value</th>
        </tr>
        <tr>
            <td>Dependent Field</td>
            <td>
                <select id="reqDependentFieldList" name="reqDependentFieldList">
                    <c:forEach var="hfjvConstraint" items="${hfjvConstraintList}">
                        <option name="${hfjvConstraint}">${hfjvConstraint}</option>
                    </c:forEach>
                </select>
            </td>
        </tr>
        <tr>
            <td>Dependent Value</td>
            <td>
                <input type="text" name="txtDependentFieldValue" value="" size="20"/>                
            </td>
        </tr>
        <tr>
            <td colspan="2">
                <input type="hidden" name="controlAction" value="addDependentField"/>
                <input type="submit" name="btnAddConstraint" value="Submit"/>
            </td>
        </tr>
    </table>
</form>