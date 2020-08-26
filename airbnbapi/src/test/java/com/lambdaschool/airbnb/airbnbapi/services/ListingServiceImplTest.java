package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.AirbnbapiApplication;
import com.lambdaschool.airbnb.airbnbapi.models.Listing;
import com.lambdaschool.airbnb.airbnbapi.models.Role;
import com.lambdaschool.airbnb.airbnbapi.models.User;
import com.lambdaschool.airbnb.airbnbapi.models.UserRoles;
import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirbnbapiApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ListingServiceImplTest {

    @Autowired
    ListingService listingService;

    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);

        List<Listing> list = listingService.findAll();
        for (Listing l : list){
            System.out.println("Listing: " + l.getListingid() + " User: " + l.getUser().getUserid());
        }
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findAll() {

        assertEquals(1, listingService.findAll().size());
    }

    @Test
    public void a_findByListingId() {

        assertEquals(4, listingService.findByListingId(4).getListingid());
    }

    @Test
    public void a_findListingsByUserId() {

        assertEquals(4, listingService.findListingsByUserId(3).get(0).getListingid());
    }

    @Test
    public void y_delete() {

    }

    @Test
    public void a_save() {

        Role r1 = new Role("ADMIN");

        User u1 = new User("whatever",
            "password",
            "whatever@lambdaschool.local");

        u1 = userService.save(u1);

        u1.getRoles()
            .add(new UserRoles(u1, r1));

        Listing l1 = new Listing(10453, "House",
            4000, 6, 2, 70,
            1, "moderate", 300,
            true, true, true, u1);
        l1 = listingService.save(l1);
        System.out.println();
        assertEquals(l1.getListingid(), listingService.findByListingId(l1.getListingid()).getListingid());
    }

    @Test
    public void b_update() {


    }

    @Test
    public void z_deleteAll() {

        listingService.deleteAll();
        assertEquals(0, listingService.findAll().size());
    }
}