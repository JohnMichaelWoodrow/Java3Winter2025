package org.example.spring2025demo3rest.controllers;

import org.example.spring2025demo3rest.dataaccess.HomeRepository;
import org.example.spring2025demo3rest.dataaccess.UserRepository;
import org.example.spring2025demo3rest.pojos.Home;
import org.example.spring2025demo3rest.pojos.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Controller that handles operations for managing Homes related to a specific User
 */
@RestController
@RequestMapping(path = RESTNouns.VERSION_1 + RESTNouns.USER + RESTNouns.ID + RESTNouns.HOME)
public class HomeController {

    @Autowired private HomeRepository homeRepository;
    @Autowired private UserRepository userRepository;

    /**
     * Get all homes belonging to a user
     * @param userId The ID of the user
     * @return All homes for a user
     */
    @GetMapping
    public Iterable<Home> getAllHomesByUser(@PathVariable("id") Integer userId) {
        return homeRepository.getAllByUserId(userId);
    }

    /**
     * Create a home for a user
     * @param userId
     * @param dateBuilt
     * @param heatingType
     * @param location
     * @param value
     * @return Saved Home object
     */
    @PostMapping
    public @ResponseBody Home createHomeByUser(@PathVariable("id") Integer userId, @RequestParam LocalDate dateBuilt, @RequestParam int heatingType, @RequestParam int location, @RequestParam int value) {
        Optional<User> user = userRepository.findById(userId);
        if (user.isPresent()) {
            Home home = new Home();
            home.setDateBuilt(dateBuilt);
            home.setHeatingType(Home.HeatingType.values()[heatingType]);
            home.setLocation(Home.Location.values()[location]);
            home.setValue(value);
            home.setUser(user.get());
            return homeRepository.save(home);
        }
        return null;
    }

    /**
     * Update a home for a user
     * @param userId
     * @param homeId
     * @param dateBuilt
     * @param heatingType
     * @param location
     * @param value
     * @return Success or failure message
     */
    @PutMapping("/{homeId}")
    public @ResponseBody String updateHomeByUser(@PathVariable("id") Integer userId, @PathVariable("homeId") Integer homeId, @RequestParam LocalDate dateBuilt, @RequestParam int heatingType, @RequestParam int location, @RequestParam int value) {
        Optional<Home> optionalHome = homeRepository.findById(homeId);

        if (optionalHome.isPresent()) {
            if (optionalHome.get().getUser().getId().equals(userId)) {
                Home home = optionalHome.get();
                home.setDateBuilt(dateBuilt);
                home.setHeatingType(Home.HeatingType.values()[heatingType]);
                home.setLocation(Home.Location.values()[location]);
                home.setValue(value);
                homeRepository.save(home);
                return "Home updated.";
            } else {
                return "User mismatch — not owner.";
            }
        }
        return "Home not found.";
    }

    /**
     * Delete a home for a user
     * @param userId
     * @param homeId
     * @return Success or failure message
     */
    @DeleteMapping("/{homeId}")
    public @ResponseBody String deleteHomeByUser(@PathVariable("id") Integer userId, @PathVariable("homeId") Integer homeId) {
        Optional<Home> optionalHome = homeRepository.findById(homeId);
        if (optionalHome.isPresent() && optionalHome.get().getUser().getId().equals(userId)) {
            homeRepository.deleteById(homeId);
            return "Home deleted.";
        }
        return "Home not found.";
    }
}
