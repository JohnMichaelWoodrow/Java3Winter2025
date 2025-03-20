package org.example.springdemo2.testers;

import org.example.springdemo2.dataaccess.UserRepository;
import org.example.springdemo2.pojos.User;

public class UserTester {
    public static void main(String[] args) {
        System.out.println("Hello World!");
        User user = new User();
        user.setName("John");
        user.setEmail("john@example.com");
        System.out.printf("User name %s, email: %s\n", user.getName(), user.getEmail());

        UserRepository userRepository;
        // userRepository.save or delete, etc
    }
}
