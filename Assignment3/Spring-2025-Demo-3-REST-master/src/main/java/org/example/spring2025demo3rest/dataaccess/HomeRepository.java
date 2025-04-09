package org.example.spring2025demo3rest.dataaccess;

import org.example.spring2025demo3rest.pojos.Home;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Home Repository interface that will be used by String to create a bean that handles all the CRUD operations
 */
public interface HomeRepository extends CrudRepository<Home, Integer> {

    /**
     * Get all homes for a user
     * @param userId
     * @return
     */
    Iterable<Home> getAllByUserId(Integer userId);

    /**
     * Get user by ID
     * @param userId
     * @return
     */
    Optional<Home> findByUserId(Integer userId);


    //Note: I am surprised this works! Spring detects it and fills in the method. Crazy.
    //If this didn't work I would have retrieved all the homes and looped through and grab the homes that
    //matched the user id.



}
