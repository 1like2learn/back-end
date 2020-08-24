package com.lambdaschool.airbnb.airbnbapi.repository;

import com.lambdaschool.airbnb.airbnbapi.models.Listing;
import org.springframework.data.repository.CrudRepository;

public interface ListingRepository extends CrudRepository<Listing, Long> {

}
