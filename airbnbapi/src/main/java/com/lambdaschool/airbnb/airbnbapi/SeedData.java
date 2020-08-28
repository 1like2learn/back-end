package com.lambdaschool.airbnb.airbnbapi;

import com.lambdaschool.airbnb.airbnbapi.models.*;
import com.lambdaschool.airbnb.airbnbapi.services.ListingService;
import com.lambdaschool.airbnb.airbnbapi.services.RoleService;
import com.lambdaschool.airbnb.airbnbapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Component
public class SeedData implements CommandLineRunner {

    @Autowired
    RoleService roleService;

    @Autowired
    UserService userService;

    @Autowired
    ListingService listingService;

    @Transactional
    @Override
    public void run(String[] args) throws Exception{

        roleService.deleteAll();
        userService.deleteAll();
        listingService.deleteAll();

        Role r1 = new Role("ADMIN");
        Role r2 = new Role("USER");

        r1 = roleService.save(r1);
        r2 = roleService.save(r2);

        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.getRoles()
            .add(new UserRoles(u1, r1));
        u1.getRoles()
            .add(new UserRoles(u1, r2));

        u1.getUseremails().add(new Useremail(u1, "whatever@test.test"));

        u1 = userService.save(u1);

        Listing l1 = new Listing(10453, "House",
            4000, 6, 2, 70,
            1, "moderate", 300,
            true, true, true, u1);

        l1 = listingService.save(l1);

        u1.getListings().add(l1);


//            "zipcode": 10453,
//            "property_type": "House",
//            "square_footage": 4000,
//            "bedrooms": 6,
//            "bathrooms": 2,
//            "review_score_rating": 70,
//            "accommodates": 1,
//            "cancellation_policy": "moderate",
//            "cleaning_fee": 300,
//            "free_parking": "yes",
//            "wifi": "yes",
//            "cable_tv": "yes"
    }
}
