<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add a Book</title>
</head>
<body>
<h2>Add a New Book</h2>
<form action="LibraryData" method="post">
    <input type="hidden" name="formType" value="book">
    ISBN: <input type="text" name="isbn" required><br>
    Title: <input type="text" name="title" required><br>
    Edition: <input type="number" name="edition" required><br>
    Copyright: <input type="text" name="copyright" required><br>
    Author First Name: <input type="text" name="firstName" required><br>
    Author Last Name: <input type="text" name="lastName" required><br>
    <input type="submit" value="Add Book">
</form>
<br>
<a href="index.jsp">Back to Home</a>
</body>
</html>
