package org.example.spring2025demo3rest.testers;

import org.example.spring2025demo3rest.dataaccess.UserRepository;
import org.example.spring2025demo3rest.pojos.User;

public class UserTester {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        User user = new User();
        user.setName("Test Testerson");
        user.setEmail("tester@example.com");
        System.out.printf("User name %s, email: %s\n", user.getName(), user.getEmail());
    }

}
