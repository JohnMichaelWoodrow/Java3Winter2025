package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Java 3 Assignment 1
 * A Java Class to manage Authors.
 * author @John-Michael Woodrow
 */

/**
 * Constructs a new Author
 */
public class Author {
    private int authorId;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    /**
     * Constructs a Book with the specified attributes.
     * @param authorId The Author ID.
     * @param firstName The First Name.
     * @param lastName The Last Name.
     * @param bookList The Book.
     */
    public Author(int authorId, String firstName, String lastName) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new ArrayList<>();
    }

    /**
     * Returns The authorId.
     * @return The authorId.
     */
    public int getAuthorId() { return authorId; }

    /**
     * Sets The authorId.
     * @param authorId .
     */
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    /**
     * Returns The firstName.
     * @return The firstName.
     */
    public String getFirstName() { return firstName; }

    /**
     * Sets The firstName.
     * @param firstName .
     */
    public void setFirstName(String firstName) { this.firstName = firstName; }

    /**
     * Returns The lastName.
     * @return The lastName.
     */
    public String getLastName() { return lastName; }

    /**
     * Sets The lastName.
     * @param lastName .
     */
    public void setLastName(String lastName) { this.lastName = lastName; }

    /**
     * Adds a book to the author.
     * @param book The book to add.
     */
    public void addBook(Book book) {
        if (!bookList.contains(book)) {
            bookList.add(book);
            book.addAuthor(this);
        }
    }

    /**
     * Returns List of The Books.
     * @return The bookList.
     */
    public List<Book> getBooks() {
        return bookList;
    }
}


