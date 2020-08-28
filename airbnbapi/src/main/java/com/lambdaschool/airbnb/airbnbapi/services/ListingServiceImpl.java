package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.exceptions.ResourceNotFoundException;
import com.lambdaschool.airbnb.airbnbapi.models.Listing;
import com.lambdaschool.airbnb.airbnbapi.repository.ListingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.exceptions.UnauthorizedClientException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**Implements ListingService**/
@Transactional
@Service(value = "listingService")
public class ListingServiceImpl implements ListingService{

    //connects the two repositories we will need
    @Autowired
    private ListingRepository listingrepos;

    @Autowired
    private UserService userService;

    @Autowired
    private HelperFunctions helperFunctions;


    /**Implements the Methods of ListingService**/
    //give us a list of all listings
    @Override
    public List<Listing> findAll() {

        //make a list of listings
        List<Listing> list = new ArrayList<>();
        //add listings to list
        listingrepos.findAll().iterator().forEachRemaining(list::add);
        //return the list of listings
        return list;
    }

    //give use a listing based on its id
    @Override
    public Listing findByListingId(long id) {

        //find the listing and make sure it exists then return it
        return listingrepos.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Listing ID " + " not Found!"));
    }

    @Override
    public List<Listing> findListingsByUserId(long id) {
        return listingrepos.findListingsByUserUserid(id);
    }

    //delete a listing based on its id
    @Transactional
    @Override
    public void delete(long id) {

        //find the listing and make sure it exists
        Listing listingToDelete = listingrepos.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Listing ID " + " not Found!"));

        if(helperFunctions.isUserAuthorizedForListing(id)){

            //delete the listing
            listingrepos.delete(listingToDelete);
        }else{

            throw new UnauthorizedClientException("User is not authorized to update this listing");
        }
    }

    //save a listing
    @Transactional
    @Override
    public Listing save(Listing listing) {

        //create a new listing to tack stuff onto
        Listing newListing = new Listing();

        //check if the listing has an id and thus already exists
        if (listing.getListingid() != 0){
            listingrepos.findById(listing.getListingid())
                .orElseThrow(()-> new ResourceNotFoundException("Listing ID " + " not Found!"));
            if(helperFunctions.isUserAuthorizedForListing(listing.getListingid())){

                newListing.setListingid(listing.getListingid());
            }else{

                throw new UnauthorizedClientException("User is not authorized to update this listing");
            }
        }

        //tack stuff onto listing
        newListing.setZipcode(listing.getZipcode());
        newListing.setPropertytype(listing.getPropertytype());
        newListing.setSquarefeet(listing.getSquarefeet());
        newListing.setBathrooms(listing.getBathrooms());
        newListing.setBedrooms(listing.getBedrooms());
        newListing.setReviewscore(listing.getReviewscore());
        newListing.setAccomodates(listing.getAccomodates());
        newListing.setCancellationpolicy(listing.getCancellationpolicy());
        newListing.setCleaningfee(listing.getCleaningfee());
        newListing.setFreeparking(listing.getFreeparking());
        newListing.setWifi(listing.isWifi());
        newListing.setCabletv(listing.isCabletv());

        //make sure the user that was sent exists
        newListing.setUser(userService.findUserById(listing.getUser().getUserid()));

        //save the new listing
        Listing savedListing = listingrepos.save(listing);
        return savedListing;
    }

    //update a listing
    @Transactional
    @Override
    public Listing update(Listing listing, long listingid) {

        //give currentListing all the data from the listing it is updating
        Listing currentListing = listingrepos.findById(listingid)
            .orElseThrow(()-> new ResourceNotFoundException("Listing ID " + " not Found!"));

        //update the data if the client has sent new data
        if (listing.getZipcode() != 0){

            currentListing.setZipcode(listing.getZipcode());
        }
        if (listing.getPropertytype() != null){

            currentListing.setPropertytype(listing.getPropertytype());
        }
        if (listing.getSquarefeet() != 0){

            currentListing.setSquarefeet(listing.getSquarefeet());
        }
        if (listing.getBathrooms() != 0){

            currentListing.setBathrooms(listing.getBathrooms());
        }
        if (listing.getBedrooms() != 0){

            currentListing.setBedrooms(listing.getBedrooms());
        }
        if (listing.getReviewscore() != 0){

            currentListing.setReviewscore(listing.getReviewscore());
        }
        if (listing.getAccomodates() != 0){

            currentListing.setAccomodates(listing.getAccomodates());
        }
        if (listing.getCancellationpolicy() != null){

            currentListing.setCancellationpolicy(listing.getCancellationpolicy());
        }
        if (listing.getCleaningfee() != 0){

            currentListing.setCleaningfee(listing.getCleaningfee());
        }
        if (listing.getFreeparking() != null){

            currentListing.setFreeparking(listing.getFreeparking());
        }
        if (listing.isWifi() != null){

            currentListing.setWifi(listing.isWifi());
        }
        if (listing.isCabletv() != null){

            currentListing.setCabletv(listing.isCabletv());
        }
        if (listing.getUser() != null) {

            //set currentListing's user and check the id of the sent user to make sure it exists
            currentListing.setUser(userService.findUserById(listing.getUser().getUserid()));
        }

        if(helperFunctions.isUserAuthorizedForListing(listingid)) {
            //save the new listing
            currentListing = listingrepos.save(currentListing);
        }else{
            throw new UnauthorizedClientException("User is not authorized to update this listing");
        }
        return currentListing;
    }

    @Transactional
    @Override
    public void deleteAll() {

        listingrepos.deleteAll();
    }
}
