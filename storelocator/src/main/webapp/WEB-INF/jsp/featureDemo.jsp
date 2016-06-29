<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Spring MVC feature demo</title>
    </head>
    <body>
    	<h1>Spring MVC Feature Demonstration</h1>
 		<table>
 		<tr> <td>Owner ID obtained from URL path</td> <td> ${ownerId } </td> </tr>
 		<tr> <td> Message injected from Spring MVC internal model</td><td> ${helloStr } </td></tr>
 		<tr> <td> Message contained in Http request parameter</td><td> ${msg } </td></tr>
 		</table>
    </body>
</html>