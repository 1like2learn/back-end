package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.AirbnbapiApplication;
import com.lambdaschool.airbnb.airbnbapi.models.User;
import com.lambdaschool.airbnb.airbnbapi.models.Useremail;
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
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirbnbapiApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UseremailServiceTest {

    @Autowired
    UseremailService useremailService;

    @Autowired
    UserService userService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void a_findAll() {

        assertEquals(1, useremailService.findAll().size());
    }

    @Test
    public void a_findUseremailById() {
        assertEquals(4, useremailService.findUseremailById(4).getUseremailid());
    }

    @Test
    public void z_delete() {

        useremailService.delete(useremailService.findAll().get(0).getUseremailid());
        assertEquals(0, useremailService.findAll().size());
    }

    @Test
    public void update() {

        Useremail email = useremailService.findAll().get(0);
        useremailService.update(email.getUseremailid(), "updated@email.address");
        assertEquals("updated@email.address", email.getUseremail());
    }

    @Transactional
    @Test
    public void a_save() {
        User u1 = userService.findAll().get(0);
        u1.getUseremails().add(new Useremail(u1, "newTest@email.test"));
        assertEquals(2, useremailService.findAll().size());
    }
}