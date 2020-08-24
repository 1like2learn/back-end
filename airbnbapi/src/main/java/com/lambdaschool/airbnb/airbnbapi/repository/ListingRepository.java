package com.lambdaschool.airbnb.airbnbapi.repository;

import com.lambdaschool.airbnb.airbnbapi.models.Listing;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ListingRepository extends CrudRepository<Listing, Long> {

    List<Listing> findListingsByUserUserid(long id);
}
