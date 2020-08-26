package com.lambdaschool.airbnb.airbnbapi.services;

import com.lambdaschool.airbnb.airbnbapi.AirbnbapiApplication;
import com.lambdaschool.airbnb.airbnbapi.models.Role;
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
public class RoleServiceImplTest {

    @Autowired
    RoleService roleService;

    @Before
    public void setUp() throws Exception {

        MockitoAnnotations.initMocks(this);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findAll() {

        assertEquals(2, roleService.findAll().size());
    }

    @Test
    public void findRoleById() {

        assertEquals(1, roleService.findRoleById(1).getRoleid());
    }

    @Test
    public void findByName() {

        assertEquals("ADMIN", roleService.findByName("ADMIN").getName());
    }

    @Test
    public void save() {
        Role role = new Role("test");
        roleService.save(role);
        assertEquals(3, roleService.findAll().size());
    }

    @Test
    public void z_deleteAll() {

        roleService.deleteAll();
        assertEquals(0, roleService.findAll().size());
    }

    @Test
    public void update() {

    }
}