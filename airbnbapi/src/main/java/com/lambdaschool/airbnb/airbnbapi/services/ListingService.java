package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.models.Listing;

import java.util.List;

/**Creates a Service for Listings that we can implement**/
public interface ListingService {

    //Admin only, returns list of all listings
    List<Listing> findAll();

    //Admins get access and users can access Listings they've made
    Listing findByListingId(long id);

    //Admins get access and users can delete Listings they've made
    void delete(long id);

    //Admins and users can save listings
    Listing save(Listing listing);

    //Admins get access and users can update Listings they've made
    Listing update(Listing listing, long listinid);

}
