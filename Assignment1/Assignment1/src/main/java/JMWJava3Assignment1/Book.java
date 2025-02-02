package JMWJava3Assignment1;

import java.util.ArrayList;
import java.util.List;

public class Book {
    private String isbn;
    private String title;
    private int editionNumber;
    private String copyright;
    private List<Author> authorList;

    public Book(String isbn, String title, int editionNumber, String copyright) {
        this.isbn = isbn;
        this.title = title;
        this.editionNumber = editionNumber;
        this.copyright = copyright;
        this.authorList = new ArrayList<>();
    }

    public String getIsbn() { return isbn; }

    public String getTitle() { return title; }

    public void setTitle(String title) { this.title = title; }

    public int getEditionNumber() { return editionNumber; }

    public String getCopyright() { return copyright; }

    public void addAuthor(Author author) {
        if (!authorList.contains(author)) {
            authorList.add(author);
            author.addBook(this);  // Maintain relationship in both directions
        }
    }

    public List<Author> getAuthors() {
        return authorList;
    }
}


