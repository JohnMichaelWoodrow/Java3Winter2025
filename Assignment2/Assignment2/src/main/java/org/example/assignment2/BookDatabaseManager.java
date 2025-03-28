package org.example.assignment2;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database operations for books and authors in the library system.
 * Provides methods for retrieving, adding, updating, and deleting books and authors from a SQL database.
 * Handles relationships between books and authors.
 * @author john-michael woodrow
 */
public class BookDatabaseManager {
    private final Connection connection;

    /**
     * Establishes a connection to the database using the provided credentials.
     *
     * @param dbUrl    The database URL.
     * @param username The database username.
     * @param password The database password.
     * @throws SQLException if a database connection error occurs.
     */
    public BookDatabaseManager(String dbUrl, String username, String password) throws SQLException {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            connection = DriverManager.getConnection(dbUrl, username, password);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MariaDB JDBC Driver not found", e);
        }
    }

    /**
     * Retrieves all books from the database.
     *
     * @return A list of all books.
     * @throws SQLException if a database access error occurs.
     */
    public List<Book> getAllBooks() throws SQLException {
        String query = "SELECT * FROM titles";
        List<Book> books = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));
                loadAuthorsForBook(book);
                books.add(book);
            }
        }
        return books;
    }

    /**
     * Retrieves a book by its ISBN.
     */
    public Book getBookByIsbn(String isbn) throws SQLException {
        String query = "SELECT * FROM titles WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Book book = new Book(rs.getString("isbn"), rs.getString("title"),
                        rs.getInt("editionNumber"), rs.getString("copyright"));
                loadAuthorsForBook(book);
                return book;
            }
        }
        return null;
    }

    /**
     * Adds a new book to the database.
     *
     * @param book The book to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addBook(Book book) throws SQLException {
        String query = "INSERT INTO titles (isbn, title, editionNumber, copyright) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getIsbn());
            stmt.setString(2, book.getTitle());
            stmt.setInt(3, book.getEditionNumber());
            stmt.setString(4, book.getCopyright());
            stmt.executeUpdate();
        }
        addBookAuthors(book);
    }

    /**
     * Updates an existing book's details in the database.
     * @param book The book to update.
     * @throws SQLException if a database access error occurs.
     */
    public void updateBook(Book book) throws SQLException {
        String query = "UPDATE titles SET title = ?, editionNumber = ?, copyright = ? WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getTitle());
            stmt.setInt(2, book.getEditionNumber());
            stmt.setString(3, book.getCopyright());
            stmt.setString(4, book.getIsbn());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes a book from the database.
     * Also removes any author-book associations.
     * @param book The book to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteBook(Book book) throws SQLException {
        System.out.println("Deleting book from database: " + book.getIsbn());
        String deleteAuthorLinkQuery = "DELETE FROM authorISBN WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteAuthorLinkQuery)) {
            stmt.setString(1, book.getIsbn());
            int affectedRows = stmt.executeUpdate();
            System.out.println("Deleted " + affectedRows + " authorISBN records.");
        }

        String deleteBookQuery = "DELETE FROM titles WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteBookQuery)) {
            stmt.setString(1, book.getIsbn());
            int affectedRows = stmt.executeUpdate();
            System.out.println("Deleted " + affectedRows + " book records.");
        }
    }

    /**
     * Returns all authors of a specific book.
     */
    public void loadAuthorsForBook(Book book) throws SQLException {
        String query = "SELECT a.authorID, a.firstName, a.lastName FROM authors a " +
                "JOIN authorISBN ai ON a.authorID = ai.authorID " +
                "WHERE ai.isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getIsbn());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
                book.addAuthor(author);
            }
        }
    }

    /**
     * Retrieves all authors from the database.
     * @return A list of all authors.
     * @throws SQLException if a database access error occurs.
     */
    public List<Author> getAllAuthors() throws SQLException {
        String query = "SELECT * FROM authors";
        List<Author> authors = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Author author = new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
                loadBooksForAuthor(author);
                authors.add(author);
            }
        }
        return authors;
    }

    /**
     * Returns an author by their ID.
     */
    public Author getAuthorById(int authorId) throws SQLException {
        String query = "SELECT * FROM authors WHERE authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, authorId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
            }
        }
        return null;
    }

    /**
     * Returns an author by their first and last name.
     */
    public Author getAuthorByName(String firstName, String lastName) throws SQLException {
        String query = "SELECT * FROM authors WHERE firstName = ? AND lastName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, firstName);
            stmt.setString(2, lastName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName"));
            }
        }
        return null;
    }

    /**
     * Adds a new author to the database.
     * @param author The author to add.
     * @throws SQLException if a database access error occurs.
     */
    public void addAuthor(Author author) throws SQLException {
        String query = "INSERT INTO authors (firstName, lastName) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.executeUpdate();
            ResultSet keys = stmt.getGeneratedKeys();
            if (keys.next()) {
                author.setAuthorId(keys.getInt(1));
            }
        }
    }

    /**
     * Updates an existing author's details in the database.
     * @param author The author to update.
     * @throws SQLException if a database access error occurs.
     */
    public void updateAuthor(Author author) throws SQLException {
        String query = "UPDATE authors SET firstName = ?, lastName = ? WHERE authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setInt(3, author.getAuthorId());
            stmt.executeUpdate();
        }
    }

    /**
     * Deletes an author from the database.
     * @param author The author to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteAuthor(Author author) throws SQLException {
        String query = "DELETE FROM authors WHERE authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, author.getAuthorId());
            stmt.executeUpdate();
        }
    }

    /**
     * Returns all books by a specific author.
     */
    public void loadBooksForAuthor(Author author) throws SQLException {
        String query = "SELECT t.isbn, t.title, t.editionNumber, t.copyright FROM titles t " +
                "JOIN authorISBN ai ON t.isbn = ai.isbn " +
                "WHERE ai.authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, author.getAuthorId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright"));
                author.addBook(book);
            }
        }
    }

    /**
     * Associates a book with its authors in the database.
     * @param book The book to associate with authors.
     * @throws SQLException if a database access error occurs.
     */
    private void addBookAuthors(Book book) throws SQLException {
        String query = "INSERT INTO authorISBN (authorID, isbn) VALUES (?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            for (Author author : book.getAuthors()) {
                stmt.setInt(1, author.getAuthorId());
                stmt.setString(2, book.getIsbn());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

}
