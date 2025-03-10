package org.example.assignment2;

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
     */
    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            Properties config = loadConfig();
            String dbUrl = config.getProperty("db.url");
            String dbUsername = config.getProperty("db.username");
            String dbPassword = config.getProperty("db.password");

            BookDatabaseManager dbManager = new BookDatabaseManager(dbUrl, dbUsername, dbPassword);
            Library library = new Library(dbManager);

            boolean running = true;
            while (running) {
                System.out.println("\nMenu:");
                System.out.println("1. Print all books");
                System.out.println("2. Print all authors");
                System.out.println("3. Print all books by an author");
                System.out.println("4. Print all authors of a book");
                System.out.println("5. Quit");

                int choice = scanner.nextInt();
                scanner.nextLine();

                if (choice == 1) {
                    printAllBooks(library);
                } else if (choice == 2) {
                    printAllAuthors(library);
                } else if (choice == 3) {
                    printBooksForAuthor(scanner, library, dbManager);
                } else if (choice == 4) {
                    printAuthorsForBook(scanner, library, dbManager);
                } else if (choice == 5) {
                    running = false;
                } else {
                    System.out.println("Invalid choice. Please try again.");
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
     * Prints all books by a specific author.
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
            dbManager.loadAuthorsForBook(book);
            for (Author author : book.getAuthors()) {
                System.out.println(" - " + author.getFirstName() + " " + author.getLastName());
            }
        } catch (SQLException e) {
            System.err.println("Error loading authors for book: " + e.getMessage());
        }
    }

    /**
     * Prints a list of all authors in the library along with the books they have written.
     */
    private static void printBooksForAuthor(Scanner scanner, Library library, BookDatabaseManager dbManager) {
        System.out.println("Enter the author's full name:");
        String firstName = scanner.next();
        String lastName = scanner.next();
        Author author = library.getAuthorByFullName(firstName, lastName);
        printAllAuthors(library);
    }
}

