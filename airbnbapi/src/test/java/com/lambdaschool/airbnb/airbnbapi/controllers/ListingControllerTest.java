package com.lambdaschool.airbnb.airbnbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.airbnb.airbnbapi.models.*;
import com.lambdaschool.airbnb.airbnbapi.services.HelperFunctions;
import com.lambdaschool.airbnb.airbnbapi.services.ListingService;
import com.lambdaschool.airbnb.airbnbapi.services.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class ListingControllerTest {

    List<Role> roles = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Listing> listings = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    ListingService listingService;

    @MockBean
    UserService userService;

    @MockBean
    HelperFunctions helperFunctions;

    @Before
    public void setUp() throws Exception {

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

        listings.add(l1);

        Listing l2 = new Listing(10453, "House",
            2000, 3, 2, 70,
            1, "strict", 300,
            true, true, true, u1);
        l2.setListingid(2);

        listings.add(l2);

        u1.getListings().add(l1);

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void listAllListings() throws Exception{
        String apiUrl = "/listings/listings";

        Mockito.when(listingService.findAll()).thenReturn(listings);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listings);

        assertEquals(er, tr);
    }

    @Test
    public void getListingById() throws Exception{
        String apiUrl = "/listings/listing/1";

        Mockito.when(listingService.findByListingId(1)).thenReturn(listings.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listings.get(0));


        assertEquals(er, tr);
    }

    @Test
    public void getMyListings() throws Exception{
        String apiUrl = "/listings/myListings";

        User u = users.get(0);

        Mockito.when(userService.findByName("admin")).thenReturn(u);
        Mockito.when(listingService.findListingsByUserId(1)).thenReturn(listings);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(listings);


        assertEquals(er, tr);
    }

    @Test
    public void addNewListing() throws Exception{
        String apiUrl = "/listings/listing";

        Listing l1 = new Listing(10453, "House",
            2000, 3, 2, 70,
            1, "strict", 300,
            true, true, true, users.get(0));
//        Listing l3WithId = new Listing(10453, "House",
//            2000, 3, 2, 70,
//            1, "strict", 300,
//            true, true, true, users.get(0));
//        l3WithId.setListingid(3);

        Prediction prediction = new Prediction(300.0);
        Mockito.when(listingService.save(any(Listing.class))).thenReturn(l1);
        Mockito.when(helperFunctions.giveListingReceivePrice(l1)).thenReturn(prediction);

        ObjectMapper mapper = new ObjectMapper();
        String listingString = mapper.writeValueAsString(l1);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(listingString);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = mapper.writeValueAsString(prediction);


        mockMvc.perform(rb)
            .andExpect(status().isCreated())
            .andDo(MockMvcResultHandlers.print());

        assertEquals(er, tr);
    }

    @Test
    public void updateFullListing() throws Exception{
        String apiUrl = "/listings/listing/1";

        Listing l1 = new Listing(10453, "House",
            2000, 3, 2, 70,
            1, "strict", 300,
            true, true, true, users.get(0));
        l1.setListingid(5);

        Prediction prediction = new Prediction(300.0);

        Mockito.when(listingService.save(any(Listing.class))).thenReturn(l1);
        Mockito.when(helperFunctions.giveListingReceivePrice(l1)).thenReturn(prediction);

        ObjectMapper mapper = new ObjectMapper();
        String listingString = mapper.writeValueAsString(l1);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(listingString);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        String er = mapper.writeValueAsString(prediction);

        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

        assertEquals(er, tr);
    }

    @Test
    public void updateListing() throws Exception{
        String apiUrl = "/listings/listing/1";

        Listing l1 = new Listing();
        l1.setZipcode(72143);

        Prediction prediction = new Prediction(300.0);

        Mockito.when(listingService.update(any(Listing.class), any(Long.class))).thenReturn(l1);
        Mockito.when(helperFunctions.giveListingReceivePrice(any(Listing.class))).thenReturn(prediction);

        ObjectMapper mapper = new ObjectMapper();
        String listingString = mapper.writeValueAsString(l1);

        RequestBuilder rb = MockMvcRequestBuilders.patch(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(listingString);
        MvcResult r = mockMvc.perform(rb).andReturn();
        System.out.println("test response " + r);
        String tr = r.getResponse().getContentAsString();
        System.out.println("test true response " + tr);

        String er = mapper.writeValueAsString(prediction);

        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

        assertEquals(er, tr);
    }

    @Test
    public void deleteListingById() throws Exception{
        String apiUrl = "/listings/listing/1";

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }
}