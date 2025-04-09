package org.example.spring2025demo3rest.pojos;

import jakarta.persistence.*;

/**
 * Auto object for REST Assignment - represents a vehicle associated with a user
 */
@Entity
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String make;
    private String model;
    private int year;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // User can have many autos

    /**
     * Get the auto ID
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the auto ID
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the make of the vehicle
     * @return make
     */
    public String getMake() {
        return make;
    }

    /**
     * Set the make of the vehicle
     * @param make
     */
    public void setMake(String make) {
        this.make = make;
    }

    /**
     * Get the model of the vehicle
     * @return model
     */
    public String getModel() {
        return model;
    }

    /**
     * Set the model of the vehicle
     * @param model
     */
    public void setModel(String model) {
        this.model = model;
    }

    /**
     * Get the year the auto was manufactured
     * @return year
     */
    public int getYear() {
        return year;
    }

    /**
     * Set the year of manufacture
     * @param year
     */
    public void setYear(int year) {
        this.year = year;
    }

    /**
     * Get the user that owns the auto
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user that owns the auto
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }
}
