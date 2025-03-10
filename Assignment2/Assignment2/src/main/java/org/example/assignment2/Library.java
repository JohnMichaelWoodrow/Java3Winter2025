package org.example.assignment2;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a library that manages books and authors.
 * Handles book-author relationships using a database manager.
 * @author john-michael woodrow
 */
public class Library {
    private final BookDatabaseManager dbManager;
    private List<Book> books;
    private List<Author> authors;

    /**
     * Constructs a Library instance and loads initial data.
     * @param dbManager The database manager handling book and author data.
     */
    public Library(BookDatabaseManager dbManager) {
        this.dbManager = dbManager;
        loadLibraryData();
    }

    /**
     * Loads books and authors from the database and associates them.
     */
    private void loadLibraryData() {
        try {
            books = dbManager.getAllBooks();
            authors = dbManager.getAllAuthors();

            for (Book book : books) {
                dbManager.loadAuthorsForBook(book);
            }

            for (Author author : authors) {
                dbManager.loadBooksForAuthor(author);
            }
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
     * Finds a book by ISBN.
     * @param isbn The ISBN of the book.
     * @return The matching Book or null if not found.
     */
    public Book getBookByIsbn(String isbn) {
        return books.stream().filter(book -> book.getIsbn().equals(isbn)).findFirst().orElse(null);
    }

    /**
     * Finds an author by full name.
     * @param firstName The first name.
     * @param lastName The last name.
     * @return The matching Author or null if not found.
     */
    public Author getAuthorByFullName(String firstName, String lastName) {
        return authors.stream().filter(author -> author.getFirstName().equalsIgnoreCase(firstName) && author.getLastName().equalsIgnoreCase(lastName)).findFirst().orElse(null);
    }

    /**
     * Adds a book to the Library and database.
     * @param book The book to add.
     */
    public void addBook(Book book) {
        books.add(book);
        try {
            dbManager.addBook(book);
        } catch (SQLException e) {
            System.err.println("Error adding book: " + e.getMessage());
        }
    }

    /**
     * Adds an author to the Library and database.
     * @param author The author to add.
     */
    public void addAuthor(Author author) {
        authors.add(author);
        try {
            dbManager.addAuthor(author);
        } catch (SQLException e) {
            System.err.println("Error adding author: " + e.getMessage());
        }
    }

    /**
     * Updates a book in the database.
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
     * Updates an author in the database.
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
     * Deletes a book from the library and database.
     * @param book The book to delete.
     */
    public void deleteBook(Book book) {
        books.remove(book);
        try {
            dbManager.deleteBook(book);
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
        }
    }

    /**
     * Deletes an author from the library and database.
     * @param author The author to delete.
     */
    public void deleteAuthor(Author author) {
        authors.remove(author);
        try {
            dbManager.deleteAuthor(author);
        } catch (SQLException e) {
            System.err.println("Error deleting author: " + e.getMessage());
        }
    }
}
