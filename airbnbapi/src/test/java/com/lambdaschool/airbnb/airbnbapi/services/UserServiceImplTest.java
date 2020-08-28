package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.AirbnbapiApplication;
import com.lambdaschool.airbnb.airbnbapi.models.User;
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

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AirbnbapiApplication.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class UserServiceImplTest {

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
    public void a_findUserById() {

        User u1 = userService.findAll().get(0);
        assertEquals(u1.getUserid(), userService.findUserById(u1.getUserid()).getUserid());
    }

    @Test
    public void b_findByNameContaining() {

        assertEquals("admin", userService.findByNameContaining("ad").get(0).getUsername());
    }

    @Test
    public void c_findAll() {

        assertEquals(1, userService.findAll().size());
    }

    @Test
    public void y_delete() {

        User u1 = userService.findAll().get(0);
        userService.delete(u1.getUserid());
        assertEquals(1, userService.findAll().size());
    }

    @Test
    public void d_findByName() {

        assertEquals("admin", userService.findByName("admin").getUsername());
    }

    @Test
    public void x_save() {

        User u1 = new User("testerman",
            "password",
            "test@tester.local");
        userService.save(u1);
        assertEquals(u1.getUsername(), userService.findByName("testerman").getUsername());
    }

    @Test
    public void e_update() {

        User u = new User();
        u.setPrimaryemail("test1@tester.local");
        User u1 = userService.findAll().get(0);
        userService.update(u, u1.getUserid());
        assertEquals("test1@tester.local", userService.findUserById(u1.getUserid()).getPrimaryemail());
    }

    @Test
    public void z_deleteAll() {

        userService.deleteAll();
        assertEquals(0, userService.findAll().size());
    }
}