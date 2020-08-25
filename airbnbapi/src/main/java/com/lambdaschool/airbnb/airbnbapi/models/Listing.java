package com.lambdaschool.airbnb.airbnbapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

/**Model for Listings**/
@Entity
@Table(name = "listings")
public class Listing extends Auditable{

    /**Fields**/
    //primary key for listings
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long listingid;
    private long zipcode;
    private String propertytype;
    private long squarefeet;
    private int bedrooms;
    private double bathrooms;
    private double reviewscore;
    private int accomodates;
    private String cancellationpolicy;
    private double cleaningfee;
    private Boolean freeparking;
    private Boolean wifi;
    private Boolean cabletv;

    //Joins the users and listings tables along the userid column
    @ManyToOne
    @JoinColumn(name ="userid", nullable = false)
    @JsonIgnoreProperties(value = "listings")
    private User user;

    /**Constructor**/
    public Listing() {
    }

    public Listing(
        long zipcode,
        String propertytype,
        long squarefeet,
        int bedrooms,
        double bathrooms,
        double reviewscore,
        int accomodates,
        String cancellationpolicy,
        double cleaningfee,
        Boolean freeparking,
        Boolean wifi,
        Boolean cabletv,
        User user) {
        this.zipcode = zipcode;
        this.propertytype = propertytype;
        this.squarefeet = squarefeet;
        this.bedrooms = bedrooms;
        this.bathrooms = bathrooms;
        this.reviewscore = reviewscore;
        this.accomodates = accomodates;
        this.cancellationpolicy = cancellationpolicy;
        this.cleaningfee = cleaningfee;
        this.freeparking = freeparking;
        this.wifi = wifi;
        this.cabletv = cabletv;
        this.user = user;
    }

    /**Getters and Setters**/
    public long getListingid() {
        return listingid;
    }

    public void setListingid(long listingid) {
        this.listingid = listingid;
    }

    public long getZipcode() {
        return zipcode;
    }

    public void setZipcode(long zipcode) {
        this.zipcode = zipcode;
    }

    public String getPropertytype() {
        return propertytype;
    }

    public void setPropertytype(String propertytype) {
        this.propertytype = propertytype;
    }

    public long getSquarefeet() {
        return squarefeet;
    }

    public void setSquarefeet(long squarefeet) {
        this.squarefeet = squarefeet;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    public void setBedrooms(int bedrooms) {
        this.bedrooms = bedrooms;
    }

    public double getBathrooms() {
        return bathrooms;
    }

    public void setBathrooms(double bathrooms) {
        this.bathrooms = bathrooms;
    }

    public double getReviewscore() {
        return reviewscore;
    }

    public void setReviewscore(double reviewscore) {
        this.reviewscore = reviewscore;
    }

    public int getAccomodates() {
        return accomodates;
    }

    public void setAccomodates(int accomodates) {
        this.accomodates = accomodates;
    }

    public String getCancellationpolicy() {
        return cancellationpolicy;
    }

    public void setCancellationpolicy(String cancellationpolicy) {
        this.cancellationpolicy = cancellationpolicy;
    }

    public double getCleaningfee() {
        return cleaningfee;
    }

    public void setCleaningfee(double cleaningfee) {
        this.cleaningfee = cleaningfee;
    }

    public Boolean getFreeparking() {
        return freeparking;
    }

    public void setFreeparking(Boolean freeparking) {
        this.freeparking = freeparking;
    }

    public Boolean isWifi() {
        return wifi;
    }

    public void setWifi(Boolean wifi) {
        this.wifi = wifi;
    }

    public Boolean isCabletv() {
        return cabletv;
    }

    public void setCabletv(Boolean cabletv) {
        this.cabletv = cabletv;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
