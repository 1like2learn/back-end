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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
public class UserControllerTest {

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
    public void listAllUsers() throws Exception{
        String apiUrl = "/users/users";

        Mockito.when(userService.findAll()).thenReturn(users);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(users);

        assertEquals(er, tr);
    }

    @Test
    public void getUserById() throws Exception{
        String apiUrl = "/users/user/1";

        Mockito.when(userService.findUserById(1)).thenReturn(users.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(users.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void getUserByName() throws Exception{
        String apiUrl ="/users/user/name/admin";

        Mockito.when(userService.findByName("admin")).thenReturn(users.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(users.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void getUserLikeName() throws Exception{
        String apiUrl = "/users/user/name/like/ad";

        Mockito.when(userService.findByNameContaining("ad")).thenReturn(users);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(users);

        assertEquals(er, tr);
    }

    @Test
    public void addNewUser() throws Exception{
        String apiUrl = "/users/user";

        User u = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u.setUserid(2);

        Mockito.when(userService.save(any(User.class))).thenReturn(u);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb)
            .andExpect(status().isCreated())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateFullUser() throws Exception{
        String apiUrl = "/users/user/1";

        User u = new User("admin",
            "password",
            "admin@admin.local");
        u.setUserid(1);

        Mockito.when(userService.save(any(User.class))).thenReturn(u);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void updateUser() throws Exception{
        String apiUrl = "/users/user/1";

        User u = new User("admin",
            "password",
            "admin@lambdaschool.local");
        u.setUserid(2);
        u.setPrimaryemail("admin@admin.local");

        Mockito.when(userService.save(any(User.class))).thenReturn(u);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(u);

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void deleteUserById() throws Exception{

        String apiUrl = "/users/user/1";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
            .andExpect(status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
    }
}