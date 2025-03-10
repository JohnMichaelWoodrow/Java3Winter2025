package org.example.assignment2;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Servlet for handling library data requests.
 */
@WebServlet("/LibraryData")
public class LibraryData extends HttpServlet {
    private BookDatabaseManager dbManager;

    /**
     * Initializes the servlet by establishing a database connection.
     * @throws ServletException if the database connection fails.
     */
    @Override
    public void init() throws ServletException {
        try {
            String dbUrl = "jdbc:mariadb://localhost:3306/books";
            String dbUsername = "root";
            String dbPassword = "roottoor";
            dbManager = new BookDatabaseManager(dbUrl, dbUsername, dbPassword);
            System.out.println("LibraryData servlet initialized successfully.");
        } catch (SQLException e) {
            throw new ServletException("Database connection failed", e);
        }
    }

    /**
     * Handles GET requests for viewing books and authors.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String view = request.getParameter("view");

            if ("books".equals(view)) {
                request.setAttribute("books", dbManager.getAllBooks());
                request.getRequestDispatcher("viewbooks.jsp").forward(request, response);
            } else if ("authors".equals(view)) {
                request.setAttribute("authors", dbManager.getAllAuthors());
                request.getRequestDispatcher("viewauthors.jsp").forward(request, response);
            } else {
                response.sendRedirect("index.jsp");
            }
        } catch (SQLException e) {
            request.setAttribute("message", "Database error: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Handles POST requests for adding, updating, and deleting books or authors.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String formType = request.getParameter("formType");

        try {
            if ("book".equals(formType)) {
                addBook(request, response);
            } else if ("author".equals(formType)) {
                addAuthor(request, response);
            } else if ("updateBook".equals(formType)) {
                updateBook(request, response);
            } else if ("updateAuthor".equals(formType)) {
                updateAuthor(request, response);
            } else if ("deleteBook".equals(formType)) {
                deleteBook(request, response);
            } else if ("deleteAuthor".equals(formType)) {
                deleteAuthor(request, response);
            } else {
                request.setAttribute("message", "Invalid action.");
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("message", "Error: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Adds a new book with an associated author.
     */
    private void addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        int edition = Integer.parseInt(request.getParameter("edition"));
        String copyright = request.getParameter("copyright");
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        try {
            Book book = new Book(isbn, title, edition, copyright);
            Author author = dbManager.getAuthorByName(firstName, lastName);
            if (author == null) {
                author = new Author(0, firstName, lastName);
                dbManager.addAuthor(author);
            }
            book.addAuthor(author);
            dbManager.addBook(book);
            request.setAttribute("message", "Book added successfully.");
        } catch (SQLException e) {
            request.setAttribute("message", "Error adding book: " + e.getMessage());
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Adds a new author.
     */
    private void addAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
            request.setAttribute("message", "Error: Missing first or last name.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        try {
            Author author = new Author(0, firstName, lastName);
            dbManager.addAuthor(author);
            request.setAttribute("message", "Author added successfully.");
            response.sendRedirect("LibraryData?view=authors"); // Redirect to author list
        } catch (SQLException e) {
            request.setAttribute("message", "Error adding author: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Deletes a book from the database.
     */
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        System.out.println("Attempting to delete book with ISBN: " + isbn);
        try {
            Book book = dbManager.getBookByIsbn(isbn);
            if (book != null) {
                dbManager.deleteBook(book);
                System.out.println("Book deleted successfully: " + isbn);
                request.setAttribute("message", "Book deleted successfully.");
            } else {
                System.out.println("Book not found: " + isbn);
                request.setAttribute("message", "Error: Book not found.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while deleting book: " + e.getMessage());
            request.setAttribute("message", "Error deleting book: " + e.getMessage());
        }

        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Updates an existing book in the database.
     */
    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        int edition = Integer.parseInt(request.getParameter("edition"));
        String copyright = request.getParameter("copyright");

        if (isbn == null || title == null || copyright == null || edition <= 0) {
            request.setAttribute("message", "Error: Missing or invalid book details.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        try {
            Book book = new Book(isbn, title, edition, copyright);
            dbManager.updateBook(book);
            request.setAttribute("message", "Book updated successfully.");
            response.sendRedirect("LibraryData?view=books"); // Redirect to updated book list
        } catch (SQLException e) {
            request.setAttribute("message", "Error updating book: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }

    /**
     * Deletes an author from the database.
     */
    private void deleteAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int authorId = Integer.parseInt(request.getParameter("authorId"));
        System.out.println("Attempting to delete author with ID: " + authorId);

        try {
            Author author = dbManager.getAuthorById(authorId);
            if (author != null) {
                dbManager.deleteAuthor(author);
                System.out.println("Author deleted successfully: " + authorId);
                request.setAttribute("message", "Author deleted successfully.");
            } else {
                System.out.println("Author not found: " + authorId);
                request.setAttribute("message", "Error: Author not found.");
            }
        } catch (SQLException e) {
            System.err.println("SQL Error while deleting author: " + e.getMessage());
            request.setAttribute("message", "Error deleting author: " + e.getMessage());
        }
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    /**
     * Updates an existing author in the database.
     */
    private void updateAuthor(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int authorId = Integer.parseInt(request.getParameter("authorId"));
        String firstName = request.getParameter("firstName");
        String lastName = request.getParameter("lastName");

        if (authorId <= 0 || firstName == null || lastName == null || firstName.isEmpty() || lastName.isEmpty()) {
            request.setAttribute("message", "Error: Missing or invalid author details.");
            request.getRequestDispatcher("index.jsp").forward(request, response);
            return;
        }

        try {
            Author author = new Author(authorId, firstName, lastName);
            dbManager.updateAuthor(author);
            request.setAttribute("message", "Author updated successfully.");
            response.sendRedirect("LibraryData?view=authors"); // Redirect to updated author list
        } catch (SQLException e) {
            request.setAttribute("message", "Error updating author: " + e.getMessage());
            request.getRequestDispatcher("index.jsp").forward(request, response);
        }
    }


}
