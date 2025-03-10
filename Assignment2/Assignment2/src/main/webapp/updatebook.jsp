<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.assignment2.Book" %>
<%
    String isbn = request.getParameter("isbn");
    Book book = (Book) request.getAttribute("book");
%>
<html>
<head>
    <title>Update Book</title>
</head>
<body>
<h2>Update Book</h2>
<form action="LibraryData" method="post">
    <input type="hidden" name="formType" value="updateBook">
    <input type="hidden" name="isbn" value="<%= isbn %>">
    Title: <input type="text" name="title" value="<%= book != null ? book.getTitle() : "" %>" required><br>
    Edition: <input type="number" name="edition" value="<%= book != null ? book.getEditionNumber() : "" %>" required><br>
    Copyright: <input type="text" name="copyright" value="<%= book != null ? book.getCopyright() : "" %>" required><br>
    <input type="submit" value="Update Book">
</form>
<a href="index.jsp">Back to Home</a>
</body>
</html>
