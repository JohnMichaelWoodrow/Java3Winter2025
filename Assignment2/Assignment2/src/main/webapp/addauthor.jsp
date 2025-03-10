<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add an Author</title>
</head>
<body>
<h2>Add an Author</h2>
<form action="LibraryData" method="post">
    <input type="hidden" name="formType" value="author">
    First Name: <input type="text" name="firstName" required><br>
    Last Name: <input type="text" name="lastName" required><br>
    <input type="submit" value="Add Author">
</form>
<br>
<a href="index.jsp">Back to Home</a>
</body>
</html>
