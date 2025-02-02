package JMWJava3Assignment1;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDatabaseManager {
    private Connection connection;

    public BookDatabaseManager(String dbUrl, String username, String password) throws SQLException {
        connection = DriverManager.getConnection(dbUrl, username, password);
    }

    public List<Book> getAllBooks() throws SQLException {
        String query = "SELECT * FROM titles";
        List<Book> books = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getInt("editionNumber"),
                        rs.getString("copyright")
                );
                books.add(book);
                loadAuthorsForBook(book);
            }
        }
        return books;
    }

    public List<Author> getAllAuthors() throws SQLException {
        String query = "SELECT * FROM authors";
        List<Author> authors = new ArrayList<>();

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("authorID"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );
                authors.add(author);
                loadBooksForAuthor(author);
            }
        }
        return authors;
    }

    private void loadAuthorsForBook(Book book) throws SQLException {
        String query = "SELECT a.* FROM authors a " +
                "JOIN authorISBN ai ON a.authorID = ai.authorID " +
                "WHERE ai.isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, book.getIsbn());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("authorID"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );
                book.addAuthor(author);
            }
        }
    }

    private void loadBooksForAuthor(Author author) throws SQLException {
        String query = "SELECT t.* FROM titles t " +
                "JOIN authorISBN ai ON t.isbn = ai.isbn " +
                "WHERE ai.authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, author.getAuthorId());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getInt("editionNumber"),
                        rs.getString("copyright")
                );
                author.addBook(book);
            }
        }
    }

    public Book getBookByIsbn(String isbn) throws SQLException {
        String query = "SELECT * FROM titles WHERE isbn = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, isbn);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Book book = new Book(
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getInt("editionNumber"),
                        rs.getString("copyright")
                );
                loadAuthorsForBook(book);
                return book;
            }
        }
        return null;
    }

    public Author getAuthorByName(String fullName) throws SQLException {
        String[] nameParts = fullName.split(" ", 2);
        if (nameParts.length < 2) return null;

        String query = "SELECT * FROM authors WHERE firstName = ? AND lastName = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, nameParts[0]);
            stmt.setString(2, nameParts[1]);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Author author = new Author(
                        rs.getInt("authorID"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                );
                loadBooksForAuthor(author);
                return author;
            }
        }
        return null;
    }

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

    public void updateAuthor(Author author) throws SQLException {
        String query = "UPDATE authors SET firstName = ?, lastName = ? WHERE authorID = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, author.getFirstName());
            stmt.setString(2, author.getLastName());
            stmt.setInt(3, author.getAuthorId());
            stmt.executeUpdate();
        }
    }
}
