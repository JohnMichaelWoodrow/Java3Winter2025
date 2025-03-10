<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.assignment2.Author" %>
<%
    int authorId = Integer.parseInt(request.getParameter("authorId"));
    Author author = (Author) request.getAttribute("author");
%>
<html>
<head>
    <title>Update Author</title>
</head>
<body>
<h2>Update Author</h2>
<form action="LibraryData" method="post">
    <input type="hidden" name="formType" value="updateAuthor">
    <input type="hidden" name="authorId" value="<%= authorId %>">
    First Name: <input type="text" name="firstName" value="<%= author != null ? author.getFirstName() : "" %>" required><br>
    Last Name: <input type="text" name="lastName" value="<%= author != null ? author.getLastName() : "" %>" required><br>
    <input type="submit" value="Update Author">
</form>
<a href="index.jsp">Back to Home</a>
</body>
</html>
