package org.example.spring2025demo3rest.pojos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Home object for REST Assignment - meant to be used for Capstone project (change as needed).
 * This object will demonstrate a relationship in the ORM and enum and date fields
 */
@Entity
public class Home {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @JsonFormat(pattern="yyyy-MM-dd")
    private LocalDate dateBuilt;

    private int value;

    @Enumerated(EnumType.ORDINAL)
    private HeatingType heatingType;

    private Location location;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;      //User can have many homes - this will maintain the relationship

    /**
     * Get the home ID
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set the home ID
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * Get the date the home was built
     * @return dateBuilt
     */
    public LocalDate getDateBuilt() {
        return dateBuilt;
    }

    /**
     * Set the date the home was built
     * @param yearBuilt
     */
    public void setDateBuilt(LocalDate yearBuilt) {
        this.dateBuilt = yearBuilt;
    }

    /**
     * Get the value of the home
     * @return value
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the value of the home
     * @param value
     */
    public void setValue(int value) {
        this.value = value;
    }

    /**
     * Get the heating type
     * @return heatingType
     */
    public HeatingType getHeatingType() {
        return heatingType;
    }

    /**
     * Set the heating type
     * @param heatingType
     */
    public void setHeatingType(HeatingType heatingType) {
        this.heatingType = heatingType;
    }

    /**
     * Get the location type
     * @return location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * Set the location type
     * @param location type of location
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * Get the user that owns the home
     * @return user
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user that owns the home
     * @param user
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Heating type enum
     * {@link #OIL_HEATING}
     * {@link #WOOD_HEATING}
     * {@link #OTHER_HEATING}
     */
    public enum HeatingType {
        /**
         * Oil Heating
         */
        OIL_HEATING,
        /**
         * Wood Heating
         */
        WOOD_HEATING,
        /**
         * Other Heating
         */
        OTHER_HEATING
    }

    /**
     * Location
     * {@link #URBAN}
     * {@link #RURAL}
     */
    public enum Location {
        /**
         * Urban Location
         */
        URBAN,
        /**
         * Rural Location
         */
        RURAL
    }

}
