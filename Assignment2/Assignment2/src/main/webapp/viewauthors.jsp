<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List, org.example.assignment2.Author, org.example.assignment2.Book" %>
<html>
<head><title>Author List</title></head>
<body>

<h1>Author List</h1>

<%
    String message = (String) request.getAttribute("message");
    if (message != null) {
%>
<div style="color: green; font-weight: bold; border: 1px solid green; padding: 10px; margin-bottom: 10px;">
    <%= message %>
</div>
<% } %>

<ul>
    <%
        List<Author> authors = (List<Author>) request.getAttribute("authors");
        if (authors != null && !authors.isEmpty()) {
            for (Author author : authors) {
    %>
    <li>
        <%= author.getFirstName() %> <%= author.getLastName() %> - Books:
        <%
            if (author.getBooks() == null || author.getBooks().isEmpty()) {
                out.print("No books listed");
            } else {
                for (Book book : author.getBooks()) {
                    out.print(book.getTitle() + " (ISBN: " + book.getIsbn() + "), ");
                }
            }
        %>

        <form action="LibraryData" method="post" style="display:inline;">
            <input type="hidden" name="formType" value="deleteAuthor">
            <input type="hidden" name="authorId" value="<%= author.getAuthorId() %>">
            <input type="submit" value="Delete">
        </form>

        <form action="updateauthor.jsp" method="get" style="display:inline;">
            <input type="hidden" name="authorId" value="<%= author.getAuthorId() %>">
            <input type="hidden" name="firstName" value="<%= author.getFirstName() %>">
            <input type="hidden" name="lastName" value="<%= author.getLastName() %>">
            <input type="submit" value="Update">
        </form>
    </li>
    <%  }
    } else {
        out.print("<p>No authors available.</p>");
    }
    %>
</ul>

<a href="index.jsp">Back to Home</a>
</body>
</html>

