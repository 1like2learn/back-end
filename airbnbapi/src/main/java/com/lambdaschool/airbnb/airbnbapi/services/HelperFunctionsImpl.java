package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.exceptions.ResourceNotFoundException;
import com.lambdaschool.airbnb.airbnbapi.models.*;
import com.lambdaschool.airbnb.airbnbapi.repository.ListingRepository;
import com.lambdaschool.airbnb.airbnbapi.repository.RoleRepository;
import com.lambdaschool.airbnb.airbnbapi.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Service(value = "helperFunctions")
public class HelperFunctionsImpl
        implements HelperFunctions
{
    @Autowired
    UserRepository userrepos;

    @Autowired
    ListingRepository listingrepos;
    
    public List<ValidationError> getConstraintViolation(Throwable cause)
    {
        // Find any data violations that might be associated with the error and report them
        // data validations get wrapped in other exceptions as we work through the Spring
        // exception chain. Hence we have to search the entire Spring Exception Stack
        // to see if we have any violation constraints.
        while ((cause != null) && !(cause instanceof ConstraintViolationException))
        {
            cause = cause.getCause();
        }

        List<ValidationError> listVE = new ArrayList<>();

        // we know that cause either null or an instance of ConstraintViolationException
        if (cause != null)
        {
            ConstraintViolationException ex = (ConstraintViolationException) cause;
            for (ConstraintViolation cv : ex.getConstraintViolations())
            {
                ValidationError newVe = new ValidationError();
                newVe.setCode(cv.getInvalidValue()
                                      .toString());
                newVe.setMessage(cv.getMessage());
                listVE.add(newVe);
            }
        }
        return listVE;
    }

    @Override
    public Boolean isUserAuthorizedForListing(long id) {

        //find the name of the currently logged in User
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        //find the user with that name
        User currentUser = userrepos.findByUsername(currentUsername);

        //variable that represents whether or not the currently logged in user is allowed to alter a listing
        Boolean authorized = false;

        //find listing by provided id
        Listing currentLisitng = listingrepos.findById(id)
            .orElseThrow(()-> new ResourceNotFoundException("Listing ID " + " not Found!"));

        //if the user is an admin or owns the listing set them as authorized
        for(UserRoles ur: currentUser.getRoles()){

            if(ur.getRole().getName() == "ADMIN") {

                authorized = true;
            }else if (currentLisitng.getUser() == currentUser){

                authorized = true;
            }
        }
        return authorized;
    }

    @Override
    public Prediction giveListingReceivePrice(Listing listing) {


        final String uri = "https://bw-air-bnb-2.herokuapp.com/predict";
        RestTemplate restTemplate = new RestTemplate();
        Prediction prediction = restTemplate.postForObject(uri, listing.getRequestBody(), Prediction.class);
//        Prediction prediction = restTemplate.getForObject(uri, Prediction.class);

        return prediction;
    }
}
