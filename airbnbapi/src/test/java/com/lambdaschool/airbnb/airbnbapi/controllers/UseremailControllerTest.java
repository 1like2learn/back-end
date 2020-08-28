package com.lambdaschool.airbnb.airbnbapi.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lambdaschool.airbnb.airbnbapi.models.*;
import com.lambdaschool.airbnb.airbnbapi.services.HelperFunctions;
import com.lambdaschool.airbnb.airbnbapi.services.ListingService;
import com.lambdaschool.airbnb.airbnbapi.services.UserService;
import com.lambdaschool.airbnb.airbnbapi.services.UseremailService;
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
public class UseremailControllerTest {

    List<Role> roles = new ArrayList<>();
    List<User> users = new ArrayList<>();
    List<Listing> listings = new ArrayList<>();
    List<Useremail> useremails = new ArrayList<>();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    UseremailService useremailService;

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
        Useremail ue1 = new Useremail(u1, "whatever@test.test");
        u1.getUseremails().add(ue1);

        useremails.add(ue1);

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
    public void listAllUseremails() throws Exception{
        String apiUrl = "/useremails/useremails";

        Mockito.when(useremailService.findAll()).thenReturn(useremails);

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(useremails);

        assertEquals(er, tr);
    }

    @Test
    public void getUserEmailById() throws Exception{
        String apiUrl = "/useremails/useremail/1";

        Mockito.when(useremailService.findUseremailById(1)).thenReturn(useremails.get(0));

        RequestBuilder rb = MockMvcRequestBuilders.get(apiUrl)
            .accept(MediaType.APPLICATION_JSON);
        MvcResult r = mockMvc.perform(rb).andReturn();
        String tr = r.getResponse().getContentAsString();

        ObjectMapper mapper = new ObjectMapper();
        String er = mapper.writeValueAsString(useremails.get(0));

        assertEquals(er, tr);
    }

    @Test
    public void deleteUserEmailById() throws Exception{
        String apiUrl = "/users/user/{userid}";

        RequestBuilder rb = MockMvcRequestBuilders.delete(apiUrl, "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
            .andExpect(status().is2xxSuccessful())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void updateUserEmail() throws Exception{
        String apiUrl = "/useremails/useremail/1/email/admin@lambdaschool.local";

        RequestBuilder rb = MockMvcRequestBuilders.put(apiUrl, "1")
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(rb)
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());
    }

    @Test
    public void addNewUserEmail() throws Exception{
        String apiUrl = "/useremails/user/1/email/test@email.local";

    Useremail ue = new Useremail(users.get(0), "test@email.local");

        Mockito.when(useremailService.save(1, "test@email.local")).thenReturn(ue);

        ObjectMapper mapper = new ObjectMapper();
        String userString = mapper.writeValueAsString(ue);

        RequestBuilder rb = MockMvcRequestBuilders.post(apiUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(userString);

        mockMvc.perform(rb)
            .andExpect(status().isCreated())
            .andDo(MockMvcResultHandlers.print());
    }
}