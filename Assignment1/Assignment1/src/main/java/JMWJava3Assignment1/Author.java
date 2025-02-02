package JMWJava3Assignment1;

import java.util.ArrayList;
import java.util.List;

public class Author {
    private int authorId;
    private String firstName;
    private String lastName;
    private List<Book> bookList;

    public Author(int authorId, String firstName, String lastName) {
        this.authorId = authorId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.bookList = new ArrayList<>();
    }

    public int getAuthorId() { return authorId; }

    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void addBook(Book book) {
        if (!bookList.contains(book)) {
            bookList.add(book);
            book.addAuthor(this);  // Maintain relationship in both directions
        }
    }

    public List<Book> getBooks() {
        return bookList;
    }
}


