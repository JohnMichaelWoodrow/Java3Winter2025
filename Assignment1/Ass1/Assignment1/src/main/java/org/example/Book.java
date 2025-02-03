package org.example;

import java.util.ArrayList;
import java.util.List;

/**
 * Java 3 Assignment 1
 * A Java Class to manage Books.
 * author @John-Michael Woodrow
 */

/**
 * Constructs a new Book
 */
public class Book {
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    /**
     * Constructs a Book with the specified attributes.
     * @param isbn The ISBN Number.
     * @param title The Title.
     * @param editionNumber The Edition Number.
     * @param copyright The Copyright Year.
     * @param authorList The Author.
     */
    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    /**
     * Returns The books isbn.
     * @return The books isbn.
     */
    public String getIsbn() { return isbn; }

    /**
     * Returns The books title.
     * @return The books title.
     */
    public String getTitle() { return title; }

    /**
     * Sets the title of the book.
     *
     * @param title the new title to set.
     */
    public void setTitle(String title) { this.title = title; }

    /**
     * Returns The books editionNumber.
     * @return The books editionNumber.
     */
    public int getEditionNumber() { return editionNumber; }

    /**
     * Returns The books copyright year.
     * @return The books copyright year.
     */
    public String getCopyright() { return copyright; }

    /**
     * Adds a author to the book.
     * @param author The author to add.
     */
    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            author.addBook(this);
        }
    }

    /**
     * Returns The books author(s).
     * @return The books author(s).
     */
    public List<Author> getAuthors() {
        return authorList;
    }
}


