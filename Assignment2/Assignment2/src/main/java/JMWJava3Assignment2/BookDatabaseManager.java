package JMWJava3Assignment2;

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
     * @param dbUrl The database URL.
     * @param username The database username.
     * @param password The database password.
     * @throws SQLException if a database connection error occurs.
     */
    public BookDatabaseManager(String dbUrl, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
    }

    /**
     * Retrieves all books from the database.
     * @return A list of all books.
     * @throws SQLException if a database access error occurs.
     */
    public List<Book> getAllBooks() throws SQLException {
        String query = "SELECT * FROM titles";
        List<Book> books = new ArrayList<>();
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                books.add(new Book(rs.getString("isbn"), rs.getString("title"), rs.getInt("editionNumber"), rs.getString("copyright")));
            }
        }
        return books;
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
                authors.add(new Author(rs.getInt("authorID"), rs.getString("firstName"), rs.getString("lastName")));
            }
        }
        return authors;
    }

    /**
     * Adds a new book to the database.
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
            addBookAuthors(book);
        }
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

    /**
     * Returns all authors of a specific book.
     */
    public void loadAuthorsForBook(Book book, Library library) throws SQLException {
        String query = "SELECT ai.authorID FROM authorISBN ai WHERE ai.isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getIsbn());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                library.getAuthors().stream()
                        .filter(a -> {
                            try {
                                return a.getAuthorId() == rs.getInt("authorID");
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .findFirst()
                        .ifPresent(book::addAuthor);
            }
        }
    }

    /**
     * Returns all books by a specific author.
     */
    public void loadBooksForAuthor(Author author, Library library) throws SQLException {
        String query = "SELECT ai.isbn FROM authorISBN ai WHERE ai.authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, author.getAuthorId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                library.getBooks().stream()
                        .filter(b -> {
                            try {
                                return b.getIsbn().equals(rs.getString("isbn"));
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .findFirst()
                        .ifPresent(author::addBook);
            }
        }
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
     * Deletes a book from the database.
     * Also removes any author-book associations.
     * @param book The book to delete.
     * @throws SQLException if a database access error occurs.
     */
    public void deleteBook(Book book) throws SQLException {
        String deleteAuthorIsbnQuery = "DELETE FROM authorISBN WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteAuthorIsbnQuery)) {
            stmt.setString(1, book.getIsbn());
            stmt.executeUpdate();
        }

        String deleteBookQuery = "DELETE FROM titles WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(deleteBookQuery)) {
            stmt.setString(1, book.getIsbn());
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
}
