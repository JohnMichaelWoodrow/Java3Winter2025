package JMWJava3Assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents an author in the library system.
 * An author has a unique ID, first name, last name, and a list of books.
 * Provides methods to retrieve and modify author details.
 * @author john-michael woodrow
 */
public class Author {
    private int authorId;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    /**
     * Constructs a new author with the specified details.
     * @param authorId The unique identifier for the author.
     * @param firstName The first name of the author.
     * @param lastName The last name of the author.
     */
    public Author(int authorId, String firstName, String lastName) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new ArrayList<>();
    }

    /**
     * Retrieves the author's ID.
     * @return The author ID.
     */
    public int getAuthorId() {
        return authorId;
    }

    /**
     * Sets a new ID for the author.
     * @param authorId The new author ID.
     */
    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    /**
     * Retrieves the first name of the author.
     * @return The author's first name.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets a new first name for the author.
     * @param firstName The new first name.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Retrieves the last name of the author.
     * @return The author's last name.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets a new last name for the author.
     * @param lastName The new last name.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Adds a book to the author's list of books.
     * Ensures that the relationship is maintained in both directions.
     * @param book The book to add.
     */
    public void addBook(Book book) {
        if (!bookList.contains(book)) {
            bookList.add(book);
            book.addAuthor(this);
        }
    }

    /**
     * Retrieves a list of books written by the author.
     * @return A list of books.
     */
    public List<Book> getBooks() {
        return bookList;
    }

    /**
     * Checks if two authors are equal based on their ID.
     * @param o The object to compare.
     * @return true if the authors have the same ID, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId == author.authorId;
    }

    /**
     * Generates a hash code based on the author's ID.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(authorId);
    }
}
