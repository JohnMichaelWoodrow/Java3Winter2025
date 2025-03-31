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

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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

    //TODO Document Getters and Setters

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getDateBuilt() {
        return dateBuilt;
    }

    public void setDateBuilt(LocalDate yearBuilt) {
        this.dateBuilt = yearBuilt;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public HeatingType getHeatingType() {
        return heatingType;
    }

    public void setHeatingType(HeatingType heatingType) {
        this.heatingType = heatingType;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public User getUser() {
        return user;
    }

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
