<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, org.example.assignment2.Book, org.example.assignment2.Author" %>
<html>
<head><title>Book List</title></head>
<body>

<h1>Book List</h1>

<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<div style="color: red; font-weight: bold; border: 1px solid green; padding: 10px; margin-bottom: 10px;">
    <%= message %>
</div>
<% } %>

<ul>
    <%
        List<Book> books = (List<Book>) request.getAttribute("books");
        if (books != null) {
            for (Book book : books) {
    %>
    <li>
        <%= book.getTitle() %> (ISBN: <%= book.getIsbn() %>) - Authors:
        <%
            if (book.getAuthors().isEmpty()) {
                out.print("No authors listed");
            } else {
                for (Author author : book.getAuthors()) {
                    out.print(author.getFirstName() + " " + author.getLastName() + ", ");
                }
            }
        %>

        <form action="LibraryData" method="post" style="display:inline;">
            <input type="hidden" name="formType" value="deleteBook">
            <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
            <input type="submit" value="Delete">
        </form>

        <form action="updatebook.jsp" method="get" style="display:inline;">
            <input type="hidden" name="isbn" value="<%= book.getIsbn() %>">
            <input type="submit" value="Update">
        </form>
    </li>
    <%  }
    } else {
        out.print("No books available.");
    }
    %>
</ul>
<a href="index.jsp">Back to Home</a>
</body>
</html>
