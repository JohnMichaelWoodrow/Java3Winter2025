package JMWJava3Assignment1;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages a library system containing books and authors.
 * Provides methods to retrieve, add, update, and delete books and authors.
 * Data is loaded from and synchronized with database.
 * @author john-michael woodrow
 */
public class Library {
    private List<Book> books;
    private List<Author> authors;
    private final BookDatabaseManager dbManager;

    /**
     * Constructs a Library instance with a given database manager and loads initial data.
     * @param dbManager The database manager handling book and author data.
     */
    public Library(BookDatabaseManager dbManager) {
        this.dbManager = dbManager;
        loadLibraryData();
    }

    /**
     * Loads books and authors from the database into the library.
     */
    private void loadLibraryData() {
        try {
            books = dbManager.getAllBooks();
            authors = dbManager.getAllAuthors();
        } catch (SQLException e) {
            System.err.println("Error loading library data: " + e.getMessage());
        }
    }

    /**
     * Retrieves all books in the library.
     * @return A list of books.
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * Retrieves all authors in the library.
     * @return A list of authors.
     */
    public List<Author> getAuthors() {
        return authors;
    }

    /**
     * Finds and returns a book by its ISBN.
     * @param isbn The ISBN of the book.
     * @return The book with the given ISBN, or null if not found.
     */
    public Book getBookByIsbn(String isbn) {
        return books.stream()
                .filter(book -> book.getIsbn().equals(isbn))
                .findFirst()
                .orElse(null);
    }

    /**
     * Finds and returns an author by their full name.
     * @param firstName The author's first name.
     * @param lastName The author's last name.
     * @return The author with the given name, or null if not found.
     */
    public Author getAuthorByFullName(String firstName, String lastName) {
        return authors.stream()
                .filter(author -> author.getFirstName().equals(firstName) && author.getLastName().equals(lastName))
                .findFirst()
                .orElse(null);
    }

    /**
     * Updates a book's information in the database.
     * @param book The book to update.
     */
    public void updateBook(Book book) {
        try {
            dbManager.updateBook(book);
        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        }
    }

    /**
     * Updates an author's information in the database.
     * @param author The author to update.
     */
    public void updateAuthor(Author author) {
        try {
            dbManager.updateAuthor(author);
        } catch (SQLException e) {
            System.err.println("Error updating author: " + e.getMessage());
        }
    }

    /**
     * Adds a new author to the library and database.
     * @param author The author to add.
     */
    public void addAuthor(Author author) {
        try {
            dbManager.addAuthor(author);
            authors.add(author);
        } catch (SQLException e) {
            System.err.println("Error adding author: " + e.getMessage());
        }
    }

    /**
     * Adds a new book to the library and database.
     * @param book The book to add.
     */
    public void addBook(Book book) {
        try {
            dbManager.addBook(book);
            books.add(book);
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    /**
     * Deletes a book from the library and database.
     * @param book The book to delete.
     */
    public void deleteBook(Book book) {
        try {
            dbManager.deleteBook(book);
            books.removeIf(b -> b.getIsbn().equals(book.getIsbn()));
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }

    /**
     * Deletes an author from the library and database.
     * @param author The author to delete.
     */
    public void deleteAuthor(Author author) {
        try {
            dbManager.deleteAuthor(author);
            authors.removeIf(a -> a.getAuthorId() == author.getAuthorId());
        } catch (SQLException e) {
            System.err.println("Error deleting author: " + e.getMessage());
        }
    }

    /**
     * Refreshes the library data by reloading books and authors from the database.
     */
    public void refreshData() {
        loadLibraryData();
    }
}
