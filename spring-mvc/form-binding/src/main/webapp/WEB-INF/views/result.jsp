<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<html>
<head>
    <title>Getting Started: Handling Form Submission</title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
</head>
<body>

    <h2>Submitted Employee Information</h2>
    <table>
        <tr>
            <td>Name :</td>
            <td>${employee.name}</td>
        </tr>
        <tr>
            <td>ID :</td>
            <td>${employee.id}</td>
        </tr>
        <tr>
            <td>Email :</td>
            <td>${employee.email}</td>
        </tr>
    </table>
</body>
</html>