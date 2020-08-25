package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.models.Listing;
import com.lambdaschool.airbnb.airbnbapi.models.ValidationError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Class contains helper functions - functions that are needed throughout the application. The class can be autowired
 * into any class.
 */
public interface HelperFunctions
{
    /**
     * Searches to see if the exception has any constraint violations to report
     *
     * @param cause the exception to search
     * @return constraint violations formatted for sending to the client
     */
    List<ValidationError> getConstraintViolation(Throwable cause);

    //checks if a user is an admin or if they created a listing and returns a boolean
    Boolean isUserAuthorizedForListing(long id);

    //send data to an external api and receive a price recommendation
    long giveListingReceivePrice(Listing listing);
}
