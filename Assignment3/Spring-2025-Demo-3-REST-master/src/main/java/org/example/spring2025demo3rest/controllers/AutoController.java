package org.example.spring2025demo3rest.controllers;

import org.example.spring2025demo3rest.dataaccess.AutoRepository;
import org.example.spring2025demo3rest.dataaccess.UserRepository;
import org.example.spring2025demo3rest.pojos.Auto;
import org.example.spring2025demo3rest.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller that handles operations for Autos belonging to a user
 */
@RestController
@RequestMapping(path = RESTNouns.VERSION_1 + RESTNouns.USER + RESTNouns.ID + RESTNouns.AUTO)
public class AutoController {

    @Autowired private AutoRepository autoRepository;
    @Autowired private UserRepository userRepository;

    /**
     * Get all autos belonging to a user
     * @param userId
     * @return All autos for a user
     */
    @GetMapping
    public Iterable<Auto> getAutosByUser(@PathVariable("id") Integer userId) {
        return autoRepository.getAllByUserId(userId);
    }

    /**
     * Create a new auto for a user
     * @param userId
     * @param make
     * @param model
     * @param year
     * @return Saved Auto object
     */
    @PostMapping
    public @ResponseBody Auto createAuto(@PathVariable("id") Integer userId, @RequestParam String make, @RequestParam String model, @RequestParam int year) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Auto auto = new Auto();
            auto.setMake(make);
            auto.setModel(model);
            auto.setYear(year);
            auto.setUser(user.get());
            return autoRepository.save(auto);
        }
        return null;
    }

    /**
     * Update an existing auto for a user
     * @param userId
     * @param autoId
     * @param make
     * @param model
     * @param year
     * @return String message indicating result
     */
    @PutMapping("/{autoId}")
    public @ResponseBody String updateAuto(@PathVariable("id") Integer userId, @PathVariable("autoId") Integer autoId, @RequestParam String make, @RequestParam String model, @RequestParam int year) {
        Optional<Auto> optionalAuto = autoRepository.findById(autoId);
        if (optionalAuto.isPresent() && optionalAuto.get().getUser().getId().equals(userId)) {
            Auto auto = optionalAuto.get();
            auto.setMake(make);
            auto.setModel(model);
            auto.setYear(year);
            autoRepository.save(auto);
            return "Auto updated.";
        }
        return "Auto not found.";
    }

    /**
     * Delete an auto for a user
     * @param userId
     * @param autoId
     */
    @DeleteMapping("/{autoId}")
    public void deleteAuto(@PathVariable("id") Integer userId, @PathVariable("autoId") Integer autoId) {
        Optional<Auto> optionalAuto = autoRepository.findById(autoId);
        if (optionalAuto.isPresent() && optionalAuto.get().getUser().getId().equals(userId)) {
            autoRepository.deleteById(autoId);
        }
    }
}

