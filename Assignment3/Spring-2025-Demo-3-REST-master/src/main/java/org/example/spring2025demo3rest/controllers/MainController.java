package org.example.spring2025demo3rest.controllers;

import org.example.spring2025demo3rest.dataaccess.UserRepository;
import org.example.spring2025demo3rest.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller that handles all REST API endpoints related to Users.
 * Uses constants from RESTNouns to define paths for consistency.
 */
@Controller
@RequestMapping(path = RESTNouns.VERSION_1)
public class MainController {

    @Autowired private UserRepository userRepository;

    /**
     * Get all users in the database
     * @return List of all users
     */
    @GetMapping(path = RESTNouns.USER)
    public @ResponseBody Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * Get a single user by ID
     * @return Optional user object
     */
    @GetMapping(path = RESTNouns.USER + RESTNouns.ID)
    public @ResponseBody Optional<User> getUser(@PathVariable("id") Integer userId) {
        return userRepository.findById(userId);
    }

    /**
     * Create a new user using query parameters
     * @param name
     * @param email
     * @return The created user
     */
    @PostMapping(path = RESTNouns.USER)
    public @ResponseBody User createUser(@RequestParam String name, @RequestParam String email) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        return userRepository.save(user);
    }

    /**
     * Delete a user by ID
     * @param userId
     * @return Message indicating success or failure
     */
    @DeleteMapping(path = RESTNouns.USER + RESTNouns.ID)
    public @ResponseBody String deleteUser(@PathVariable("id") Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return "User deleted.";
        }
        return "User not found.";
    }

    /**
     * Update a user by ID
     * @param userId
     * @param name
     * @param email
     * @return Message indicating success or failure
     */
    @PutMapping(path = RESTNouns.USER + RESTNouns.ID)
    public @ResponseBody String updateUser(@PathVariable("id") Integer userId, @RequestParam String name, @RequestParam String email) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            user.get().setName(name);
            user.get().setEmail(email);
            userRepository.save(user.get());
            return "User updated.";
        }
        return "User not found.";
    }
}
