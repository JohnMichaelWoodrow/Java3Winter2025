package JMWJava3Assignment1;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Scanner;

/**
 * Application for managing a library database.
 * Provides options to list, add, edit, and delete books and authors.
 * Uses database connections to store and retrieve information.
 * @author john-michael woodrow
 */

public class BookApplication {
    /**
     * Main method to run the application.
     * Initializes database connection and provides an interactive menu.
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            String dbUrl = System.getenv("DB_URL");
            String dbUsername = System.getenv("DB_USERNAME");
            String dbPassword = System.getenv("DB_PASSWORD");

            if (dbUrl == null || dbUsername == null || dbPassword == null) {
                Properties config = loadConfig();
                dbUrl = config.getProperty("db.url");
                dbUsername = config.getProperty("db.username");
                dbPassword = config.getProperty("db.password");
            }

            BookDatabaseManager dbManager = new BookDatabaseManager(dbUrl, dbUsername, dbPassword);
            Library library = new Library(dbManager);

            //Code to test author states
            //library.getAuthors().get(0).setFirstName("Paul");

            boolean running = true;
            do {
                System.out.println("\nMenu:");
                System.out.println("1. Print all books");
                System.out.println("2. Print all authors");
                System.out.println("3. Edit a book or author");
                System.out.println("4. Print all books by Author");
                System.out.println("5. Print all authors of a Book");
                System.out.println("6. Add a new book");
                System.out.println("7. Delete a book or author");
                System.out.println("8. Quit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    printAllBooks(library);
                } else if (choice == 2) {
                    printAllAuthors(library);
                } else if (choice == 3) {
                    editBookOrAuthor(scanner, library);
                } else if (choice == 4) {
                    printBooksForAuthor(scanner, library, dbManager);
                } else if (choice == 5) {
                    printAuthorsForBook(scanner, library, dbManager);
                } else if (choice == 6) {
                    addNewBook(scanner, library);
                } else if (choice == 7) {
                    deleteBookOrAuthor(scanner, library);
                } else if (choice == 8) {
                    running = false;
                } else {
                    System.out.println("Invalid choice. Please try again.");
                }
            } while (running);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads the database configuration from a properties file.
     * @return A Properties object containing database connection settings.
     * @throws IOException if the properties file cannot be found or read.
     */
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

    /**
     * Prints a list of all books in the library along with their authors.
     * @param library The library instance.
     */
    private static void printAllBooks(Library library) {
        for (Book book : library.getBooks()) {
            System.out.println(book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
            for (Author author : book.getAuthors()) {
                System.out.println("  - " + author.getFirstName() + " " + author.getLastName());
            }
        }
    }

    /**
     * Prints all authors of a specific book.
     * @param scanner The scanner for user input.
     * @param library The library instance containing existing books and authors.
     * @param dbManager The database manager for retrieving author associations.
     */
    private static void printAuthorsForBook(Scanner scanner, Library library, BookDatabaseManager dbManager) {
        System.out.println("Enter the ISBN of the book:");
        String isbn = scanner.nextLine();

        Book book = library.getBookByIsbn(isbn);
        if (book == null) {
            System.out.println("Book not found.");
            return;
        }

        try {
            dbManager.loadAuthorsForBook(book, library);
            if (book.getAuthors().isEmpty()) {
                System.out.println("No authors found for this book.");
            } else {
                System.out.println("Authors for \"" + book.getTitle() + "\":");
                for (Author author : book.getAuthors()) {
                    System.out.println(" - " + author.getFirstName() + " " + author.getLastName());
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading authors for book: " + e.getMessage());
        }
    }

    /**
     * Prints all books by a specific author.
     * @param scanner The scanner for user input.
     * @param library The library instance containing existing books and authors.
     * @param dbManager The database manager for retrieving book associations.
     */
    private static void printBooksForAuthor(Scanner scanner, Library library, BookDatabaseManager dbManager) {
        System.out.println("Enter the first name of the author:");
        String firstName = scanner.nextLine();
        System.out.println("Enter the last name of the author:");
        String lastName = scanner.nextLine();

        Author author = library.getAuthorByFullName(firstName, lastName);
        if (author == null) {
            System.out.println("Author not found.");
            return;
        }

        try {
            dbManager.loadBooksForAuthor(author, library);
            if (author.getBooks().isEmpty()) {
                System.out.println("No books found for this author.");
            } else {
                System.out.println("Books by " + author.getFirstName() + " " + author.getLastName() + ":");
                for (Book book : author.getBooks()) {
                    System.out.println(" - " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error loading books for author: " + e.getMessage());
        }
    }



    /**
     * Prints a list of all authors in the library along with the books they have written.
     * @param library The library instance.
     */
    private static void printAllAuthors(Library library) {
        for (Author author : library.getAuthors()) {
            System.out.println(author.getFirstName() + " " + author.getLastName());
            for (Book book : author.getBooks()) {
                System.out.println("  - " + book.getTitle() + " (ISBN: " + book.getIsbn() + ")");
            }
        }
    }

    /**
     * Allows the user to edit a book or an author in the library.
     * @param scanner The Scanner object for user input.
     * @param library The library instance.
     */
    private static void editBookOrAuthor(Scanner scanner, Library library) {
        System.out.println("Do you want to edit a book or an author? (Enter 'book' or 'author'):");
        String choice = scanner.nextLine().trim().toLowerCase();

        if ("book".equals(choice)) {
            System.out.println("Enter the ISBN of the book you want to edit:");
            String isbn = scanner.nextLine();
            Book book = library.getBookByIsbn(isbn);
            if (book != null) {
                System.out.println("Current title: " + book.getTitle());
                System.out.println("Enter new title (or press Enter to keep the current title):");
                String newTitle = scanner.nextLine();
                if (!newTitle.isEmpty()) {
                    book.setTitle(newTitle);
                    library.updateBook(book);
                    library.refreshData();
                    System.out.println("Book updated successfully.");
                } else {
                    System.out.println("No changes made.");
                }
            } else {
                System.out.println("Book not found.");
            }
        } else if ("author".equals(choice)) {
            System.out.println("Enter the first name of the author:");
            String firstName = scanner.nextLine();
            System.out.println("Enter the last name of the author:");
            String lastName = scanner.nextLine();

            Author author = library.getAuthorByFullName(firstName, lastName);
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
                library.updateAuthor(author);
                library.refreshData();
                System.out.println("Author updated successfully.");
            } else {
                System.out.println("Author not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Allows the user to delete a book or an author in the library.
     * @param scanner The Scanner object for user input.
     * @param library The library instance.
     */
    private static void deleteBookOrAuthor(Scanner scanner, Library library) {
        System.out.println("Do you want to delete a book or an author? (Enter 'book' or 'author'):");
        String choice = scanner.nextLine().trim().toLowerCase();

        if ("book".equals(choice)) {
            System.out.println("Enter the ISBN of the book you want to delete:");
            String isbn = scanner.nextLine();
            Book book = library.getBookByIsbn(isbn);
            if (book != null) {
                library.deleteBook(book);
                library.refreshData();
                System.out.println("Book deleted successfully.");
            } else {
                System.out.println("Book not found.");
            }
        } else if ("author".equals(choice)) {
            System.out.println("Enter the first name of the author:");
            String firstName = scanner.nextLine();
            System.out.println("Enter the last name of the author:");
            String lastName = scanner.nextLine();

            Author author = library.getAuthorByFullName(firstName, lastName);
            if (author != null) {
                library.deleteAuthor(author);
                library.refreshData();
                System.out.println("Author deleted successfully.");
            } else {
                System.out.println("Author not found.");
            }
        } else {
            System.out.println("Invalid choice.");
        }
    }

    /**
     * Allows the user to add a new book and an author in the library.
     * @param scanner The Scanner object for user input.
     * @param library The library instance.
     */
    private static void addNewBook(Scanner scanner, Library library) {
        try {
            System.out.println("Enter the title of the new book:");
            String title = scanner.nextLine();

            System.out.println("Enter the ISBN of the new book:");
            String isbn = scanner.nextLine();

            System.out.println("Enter the edition number:");
            int editionNumber = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Enter the copyright information:");
            String copyright = scanner.nextLine();

            Book newBook = new Book(isbn, title, editionNumber, copyright);

            System.out.println("How many authors does this book have?");
            int authorCount = scanner.nextInt();
            scanner.nextLine();

            for (int i = 0; i < authorCount; i++) {
                System.out.println("Enter the first name of author " + (i + 1) + ":");
                String firstName = scanner.nextLine();
                System.out.println("Enter the last name of author " + (i + 1) + ":");
                String lastName = scanner.nextLine();

                Author author = library.getAuthorByFullName(firstName, lastName);
                if (author == null) {
                    author = new Author(0, firstName, lastName);
                    library.addAuthor(author);
                }
                newBook.addAuthor(author);
            }
            library.addBook(newBook);
            library.refreshData();
            System.out.println("New book added successfully.");
        } catch (Exception e) {
            System.err.println("Error adding new book: " + e.getMessage());
        }
    }
}



