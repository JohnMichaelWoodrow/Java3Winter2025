package JMWJava3Assignment1;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

public class BookApplication {
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            // Load configuration from environment variables or a properties file
            String dbUrl = System.getenv("DB_URL");
            String dbUsername = System.getenv("DB_USERNAME");
            String dbPassword = System.getenv("DB_PASSWORD");

            // If environment variables are not set, use a fallback configuration file
            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                Properties config = loadConfig();
                dbUrl = config.getProperty("db.url");
                dbUsername = config.getProperty("db.username");
                dbPassword = config.getProperty("db.password");
            }

            BookDatabaseManager dbManager = new BookDatabaseManager(dbUrl, dbUsername, dbPassword);

            boolean running = true;
            while (running) {
                System.out.println("\nMenu:");
                System.out.println("1. Print all books");
                System.out.println("2. Print all authors");
                System.out.println("3. Edit a book or author");
                System.out.println("4. Add a new book");
                System.out.println("5. Quit");

                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1 -> printAllBooks(dbManager);
                    case 2 -> printAllAuthors(dbManager);
                    case 3 -> editBookOrAuthor(scanner, dbManager);
                    case 4 -> addNewBook(scanner, dbManager);
                    case 5 -> running = false;
                    default -> System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Properties loadConfig() throws IOException {
        Properties props = new Properties();
        try (InputStream input = BookApplication.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IOException("Sorry, unable to find config.properties");
            }
            props.load(input);
        }
        return props;
    }

    private static void printAllBooks(BookDatabaseManager dbManager) {
        try {
            List<Book> books = dbManager.getAllBooks();
            for (Book book : books) {
                System.out.println(book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
                for (Author author : book.getAuthors()) {
                    System.out.println("  - " + author.getFirstName() + " " + author.getLastName());
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving books: " + e.getMessage());
        }
    }

    private static void printAllAuthors(BookDatabaseManager dbManager) {
        try {
            List<Author> authors = dbManager.getAllAuthors();
            for (Author author : authors) {
                System.out.println(author.getFirstName() + " " + author.getLastName());
                for (Book book : author.getBooks()) {
                    System.out.println("  - " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
                }
            }
        } catch (Exception e) {
            System.err.println("Error retrieving authors: " + e.getMessage());
        }
    }

    private static void editBookOrAuthor(Scanner scanner, BookDatabaseManager dbManager) {
        System.out.println("Do you want to edit a book or an author? (Enter 'book' or 'author'):");
        String choice = scanner.nextLine().trim().toLowerCase();

        try {
            if ("book".equals(choice)) {
                System.out.println("Enter the ISBN of the book you want to edit:");
                String isbn = scanner.nextLine();
                Book book = dbManager.getBookByIsbn(isbn);
                if (book != null) {
                    System.out.println("Current title: " + book.getTitle());
                    System.out.println("Enter new title (or press Enter to keep the current title):");
                    String newTitle = scanner.nextLine();
                    if (!newTitle.isEmpty()) {
                        book.setTitle(newTitle);
                        dbManager.updateBook(book);
                        System.out.println("Book updated successfully.");
                    } else {
                        System.out.println("No changes made.");
                    }
                } else {
                    System.out.println("Book not found.");
                }
            } else if ("author".equals(choice)) {
                System.out.println("Enter the full name of the author you want to edit:");
                String fullName = scanner.nextLine();
                Author author = dbManager.getAuthorByName(fullName);
                if (author != null) {
                    System.out.println("Current name: " + author.getFirstName() + " " + author.getLastName());
                    System.out.println("Enter new first name (or press Enter to keep the current first name):");
                    String newFirstName = scanner.nextLine();
                    if (!newFirstName.isEmpty()) {
                        author.setFirstName(newFirstName);
                    }
                    System.out.println("Enter new last name (or press Enter to keep the current last name):");
                    String newLastName = scanner.nextLine();
                    if (!newLastName.isEmpty()) {
                        author.setLastName(newLastName);
                    }
                    dbManager.updateAuthor(author);
                    System.out.println("Author updated successfully.");
                } else {
                    System.out.println("Author not found.");
                }
            } else {
                System.out.println("Invalid choice.");
            }
        } catch (Exception e) {
            System.err.println("Error editing book or author: " + e.getMessage());
        }
    }

    private static void addNewBook(Scanner scanner, BookDatabaseManager dbManager) {
        try {
            System.out.println("Enter the title of the new book:");
            String title = scanner.nextLine();

            System.out.println("Enter the ISBN of the new book:");
            String isbn = scanner.nextLine();

            System.out.println("Enter the edition number:");
            int editionNumber = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            System.out.println("Enter the copyright information:");
            String copyright = scanner.nextLine();

            Book newBook = new Book(isbn, title, editionNumber, copyright);

            System.out.println("How many authors does this book have?");
            int authorCount = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            for (int i = 0; i < authorCount; i++) {
                System.out.println("Enter the full name of author " + (i + 1) + ":");
                String authorName = scanner.nextLine();
                Author author = dbManager.getAuthorByName(authorName);
                if (author == null) {
                    System.out.println("Author not found. Enter details to create a new author.");
                    System.out.println("Enter first name:");
                    String firstName = scanner.nextLine();
                    System.out.println("Enter last name:");
                    String lastName = scanner.nextLine();
                    author = new Author(0, firstName, lastName);
                    dbManager.addAuthor(author);
                }
                newBook.addAuthor(author);
            }

            dbManager.addBook(newBook);
            System.out.println("New book added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding new book: " + e.getMessage());
        }
    }
}
