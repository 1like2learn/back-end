package com.lambdaschool.airbnb.airbnbapi.controllers;

import com.lambdaschool.airbnb.airbnbapi.models.Listing;
import com.lambdaschool.airbnb.airbnbapi.models.Prediction;
import com.lambdaschool.airbnb.airbnbapi.models.User;
import com.lambdaschool.airbnb.airbnbapi.services.HelperFunctions;
import com.lambdaschool.airbnb.airbnbapi.services.ListingService;
import com.lambdaschool.airbnb.airbnbapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**The Controller grants access to our users**/
@RestController
@RequestMapping("/listings")
public class ListingController {

    //connect the controller to our service provider
    @Autowired
    private ListingService listingService;

    //connect to user services
    @Autowired
    private UserService userService;

    @Autowired
    private HelperFunctions helperFunctions;

    //list all listings
    //admins only
    @GetMapping(value = "/listings", produces = "application/json")
    public ResponseEntity<?> listAllListings(){
        List<Listing> listings = listingService.findAll();
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

    //list a specific listing given it's id
    //admins and users
    @GetMapping(value = "/listing/{listingid}", produces = "application/json")
    public ResponseEntity<?> getListingById(@PathVariable long listingid){

        Listing listing = listingService.findByListingId(listingid);
        return new ResponseEntity<>(listing, HttpStatus.OK);
    }

    //list a user's listings given it's id
    @GetMapping(value = "/myListings", produces = "application/json")
    public ResponseEntity<?> getMyListings(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = authentication.getName();

        List<Listing> listings = listingService.findListingsByUserId(
            userService.findByName(currentUser).getUserid());
        return new ResponseEntity<>(listings, HttpStatus.OK);
    }

    //creates a new listing
    //admins and users
    @PostMapping(value = "/listing", consumes = "application/json")
    public ResponseEntity<?> addNewListing(@Valid @RequestBody Listing newListing) throws URISyntaxException {

        //make sure the listing doesn't inexplicably have an id
        newListing.setListingid(0);
        //save the new listing
        newListing = listingService.save(newListing);

        // set the location header for the newly created listing
        HttpHeaders responseHeaders = new HttpHeaders();
        URI newUserURI = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{userid}")
            .buildAndExpand(newListing.getListingid())
            .toUri();
        responseHeaders.setLocation(newUserURI);

        Prediction prediction = helperFunctions.giveListingReceivePrice(newListing);
        //return the location of the new listing
        return new ResponseEntity<>(prediction, responseHeaders, HttpStatus.CREATED);
    }

    //update an entire listing object
    //admins and users can delete their own
    @PutMapping(value = "/listing/{listingid}", consumes = "application/json")
    public ResponseEntity<?> updateFullListing(
        @Valid @RequestBody Listing updatingListing, @PathVariable long listingid)
    {
        updatingListing.setListingid(listingid);
        Listing updatedListing = listingService.save(updatingListing);

        Prediction prediction = helperFunctions.giveListingReceivePrice(updatedListing);
        return new ResponseEntity<>(prediction, HttpStatus.OK);
    }

    //updates specific fields
    //admins and users can delete their own
    @PatchMapping(value = "/listing/{id}", consumes = "application/json")
    public ResponseEntity<?> updateListing(
        @RequestBody Listing updateListing, @PathVariable Long id)
    {
        System.out.println("updated listing " + updateListing);
        Listing updatedListing = listingService.update(updateListing, id);
        System.out.println("updated listing mocked " + updatedListing);

        Prediction prediction = new Prediction();
        System.out.println("controller prediction created " + prediction.getPrediction());
        prediction = helperFunctions.giveListingReceivePrice(updatedListing);
        System.out.println("controller prediction equal helper mocked function result " + prediction.getPrediction());

        return new ResponseEntity<>(prediction, HttpStatus.OK);
    }

    //delete a listing
    //admins and users can delete their own
    @DeleteMapping(value = "/listing/{id}")
    public ResponseEntity<?> deleteListingById(@PathVariable long id)
    {
        listingService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
