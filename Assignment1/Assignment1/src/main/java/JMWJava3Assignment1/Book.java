package JMWJava3Assignment1;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents a book in the library system.
 * A book has an ISBN, title, edition number, copyright information, and a list of authors.
 * Provides methods to retrieve and modify book details.
 * @author john-michael woodrow
 */
public class Book {
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    /**
     * Constructs a new book with the specified details.
     * @param isbn The ISBN of the book.
     * @param title The title of the book.
     * @param editionNumber The edition number of the book.
     * @param copyright The copyright information.
     */
    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    /**
     * Retrieves the ISBN of the book.
     * @return The ISBN of the book.
     */
    public String getIsbn() {
        return isbn;
    }

    /**
     * Retrieves the title of the book.
     * @return The title of the book.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets a new title for the book.
     * @param title The new title of the book.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Retrieves the edition number of the book.
     * @return The edition number of the book.
     */
    public int getEditionNumber() {
        return editionNumber;
    }

    /**
     * Sets a new edition number for the book.
     * @param editionNumber The new edition number of the book.
     */
    public void setEditionNumber(int editionNumber) {
        this.editionNumber = editionNumber;
    }

    /**
     * Retrieves the copyright information of the book.
     * @return The copyright details.
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets new copyright information.
     * @param copyright
     */
    public void setCopyRight(String copyright) {
        this.copyright = this.copyright;
    }

    /**
     * Adds an author to the book's list of authors.
     * Ensures that the relationship is maintained in both directions.
     * @param author The author to add.
     */
    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            author.addBook(this);
        }
    }

    /**
     * Retrieves a list of authors associated with the book.
     * @return A list of authors.
     */
    public List<Author> getAuthors() {
        return authorList;
    }

    /**
     * Checks if two books are equal based on their ISBN.
     * @param o The object to compare.
     * @return true if the books have the same ISBN, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(isbn, book.isbn);
    }

    /**
     * Generates a hash code based on the book's ISBN.
     * @return The hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(isbn);
    }
}
