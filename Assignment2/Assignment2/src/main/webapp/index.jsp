<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Library System</title>
</head>
<body>

<h1>Library System</h1>

<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<div style="color: green; font-weight: bold; border: 1px solid green; padding: 10px; margin-bottom: 10px;">
    <%= message %>
</div>
<% } %>

<ul>
    <li><a href="addbook.jsp">Add a New Book</a></li>
    <li><a href="addauthor.jsp">Add an Author</a></li>
    <li><a href="LibraryData?view=books">View Books</a></li>
    <li><a href="LibraryData?view=authors">View Authors</a></li>
</ul>

</body>
</html>
