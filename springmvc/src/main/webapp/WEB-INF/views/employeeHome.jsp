<!-- 
First – notice that we’re including a tag library into our JSP page – the form taglib – to help with 
defining our form.

Next – the form:form tag plays an important role here; it’s very similar to the regular HTLM <form> tag 
but the modelAttribute attribute is the key which specifies name of the model object that backs this form.

Next – each input fields is using yet another useful tag from the Spring Form taglib – form: prefix. 
Each of these fields specifies a path attribute – this must correspond to a getter / setter of the model 
attribute (in this case, the Employee class). When the page is loaded, the input fields are populated 
by Spring, which calls the getter of each field bound to an input field. When the form is submitted, 
the setters are called to save the values of the form to the object.

Finally – when the form is submitted, the POST handler in the controller is invoked and the form is 
automatically bound to the employee argument that we passed in. 
 -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<html>
    <head>
    </head>
    <body>
        <h3>Welcome, Enter The Employee Details</h3>
        <form:form method="POST" action="/springmvc/addEmployee" modelAttribute="employee">
             <table>
                <tr>
                    <td><form:label path="name">Name</form:label></td>
                    <td><form:input path="name"/></td>
                </tr>
                <tr>
                    <td><form:label path="id">Id</form:label></td>
                    <td><form:input path="id"/></td>
                </tr>
                <tr>
                    <td><form:label path="contactNumber">Contact Number</form:label></td>
                    <td><form:input path="contactNumber"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="Submit"/></td>
                </tr>
            </table>
        </form:form>
    </body>
</html>
