package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.AirbnbapiApplication;
import com.lambdaschool.airbnb.airbnbapi.models.*;
import com.lambdaschool.airbnb.airbnbapi.repository.ListingRepository;
import com.lambdaschool.airbnb.airbnbapi.repository.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.matchers.Any;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirbnbapiApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListingServiceImplTest {

    @Autowired
    ListingService listingService;

    @MockBean
    ListingRepository listingrepos;

    @MockBean
    UserService userService;

    @MockBean
    HelperFunctions helperFunctions;

    List<Role> roles = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Listing> listings = new ArrayList<>();

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        Role r1 = new Role("ADMIN");
        r1.setRoleid(1);
        Role r2 = new Role("USER");
        r2.setRoleid(2);

        roles.add(r1);
        roles.add(r2);

        User u1 = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u1.setUserid(1);
        u1.getRoles()
            .add(new UserRoles(u1, r1));
        u1.getRoles()
            .add(new UserRoles(u1, r2));

        u1.getUseremails().add(new Useremail(u1, "whatever@test.test"));

        users.add(u1);

        Listing l1 = new Listing(10453, "House",
            4000, 6, 2, 70,
            1, "moderate", 300,
            true, true, true, u1);
        l1.setListingid(1);

        u1.getListings().add(l1);

        listings.add(l1);


    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findAll() {

        Mockito.when(listingrepos.findAll()).thenReturn(listings);
        assertEquals(1, listingService.findAll().size());
    }

    @Test
    public void a_findByListingId() {
        Long id = Long.valueOf(1);
        Mockito.when(listingrepos.findById(id)).thenReturn(Optional.of(listings.get(0)));
        assertEquals(1, listingService.findByListingId(1).getListingid());
    }

    @Test
    public void a_findListingsByUserId() {
        Mockito.when(listingrepos.findListingsByUserUserid(1)).thenReturn(listings);
        assertEquals(1, listingService.findListingsByUserId(1).size());
    }

    @Test
    public void y_delete() {

        Mockito.when(listingrepos.findById(Long.valueOf(1))).thenReturn(Optional.of(listings.get(0)));
        Mockito.when(helperFunctions.isUserAuthorizedForListing(1)).thenReturn(Boolean.TRUE);
        Mockito.when(listingrepos.findAll()).thenReturn(listings);
        listingService.delete(1);
        assertEquals(1, listingService.findAll().size());
    }

    @Test
    public void a_save() {

        User u1 = users.get(0);
        Mockito.when(userService.findUserById(1)).thenReturn(u1);

        Listing l1 = new Listing(10453, "House",
            4000, 6, 2.0, 70.0,
            1, "moderate", 300.0,
            true, true, true, u1);
        System.out.println(u1);
        l1.setListingid(0);
//        {listingid=0,
        //        zipcode=10453,
        //        propertytype='House,
        //        squarefeet=4000,
        //        bedrooms=6,
        //        bathrooms=2.0,
        //        reviewscore=70.0,
        //        accomodates=1,
        //        cancellationpolicy='moderate,
        //        cleaningfee=300.0,
        //        freeparking=true,
        //        wifi=true,
        //        cabletv=true,
        //        user=com.lambdaschool.airbnb.airbnbapi.models.User@76a2db3f}
        Listing l1andId = new Listing(10453, "",
            4000, 6, 2.0, 70.0,
            1, "", 300.0,
            true, true, true, u1);
        l1andId.setListingid(2);
        listings.add(l1andId);
        Mockito.when(listingrepos.save(any(Listing.class))).thenReturn(l1andId);

        System.out.println(l1andId.getListingid());

        l1 = listingService.save(l1);
        System.out.println(l1);

        assertEquals(2, l1.getListingid());
    }

    @Test
    public void b_update() {

        Listing l = listings.get(0);
        User u = users.get(0);
        Mockito.when(helperFunctions.isUserAuthorizedForListing(l.getListingid())).thenReturn(true);
        Listing nl = new Listing();
        nl.setZipcode(12345);
        Mockito.when(listingrepos.findById((long)1)).thenReturn(Optional.of(l));
        Mockito.when(userService.findUserById(1)).thenReturn(u);
        Mockito.when(listingrepos.save(any(Listing.class))).thenReturn(l);
        listingService.update(nl, l.getListingid());

        assertEquals(12345, l.getZipcode());
    }
//
//    @Test
//    public void z_deleteAll() {
//
//        listingService.deleteAll();
//        assertEquals(0, listingService.findAll().size());
//    }
}