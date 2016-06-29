<!DOCTYPE HTML>
<!-- Define thymeleaf  -->
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>
	<h1>Form</h1>
	<!-- 
	The th:action="@{/greeting}" expression directs the form to POST to the /greeting endpoint, 
	while the th:object="${greeting}" expression declares the model object to use for collecting 
	the form data. The two form fields, expressed with th:field="*{id}" and th:field="*{content}", 
	correspond to the fields in the Greeting object above.
	 -->
    <form action="#" th:action="@{/springmvc/greeting}" th:object="${greeting}" method="post">
    	<p>Id: <input type="text" th:field="*{id}" /></p>
        <p>Message: <input type="text" th:field="*{content}" /></p>
        <p><input type="submit" value="Submit" /> <input type="reset" value="Reset" /></p>
    </form>
</body>
</html>
