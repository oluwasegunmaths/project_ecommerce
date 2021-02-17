package com.example.demo.controllers;

import com.example.demo.Utils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class UserControllerTest {

    private UserController userController;

    private final UserRepository userRepository = mock(UserRepository.class);

    private final CartRepository cartRepository= mock(CartRepository.class);

    private final BCryptPasswordEncoder bCryptPasswordEncoder= mock(BCryptPasswordEncoder.class);
    @Before
    public void setUp() {
        userController = new UserController();
        Utils.injectObjects(userController, "userRepository", userRepository);
        Utils.injectObjects(userController, "cartRepository", cartRepository);
        Utils.injectObjects(userController, "bCryptPasswordEncoder", bCryptPasswordEncoder);
        when(userRepository.findById(1l)).thenReturn(java.util.Optional.of(new User()));
        when(userRepository.findByUsername("fineuser")).thenReturn(new User());

   }

    @Test
    public void test_create_user_then_find_by_id_and_name() {
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("fineuser");
        createUserRequest.setPassword("mypassword");
        createUserRequest.setConfirmPassword("mypassword");
        ResponseEntity<User> itemResponseEntity = userController.createUser(createUserRequest);
        assertNotNull(itemResponseEntity);
        assertEquals(itemResponseEntity.getStatusCode(), HttpStatus.OK);

    }
    @Test

    public void test_create_user_with_password_mismatch() {
        CreateUserRequest createUserRequest= new CreateUserRequest();
        createUserRequest.setUsername("fineuser");
        createUserRequest.setPassword("myssword");
        createUserRequest.setConfirmPassword("mypassword");
        ResponseEntity<User> itemResponseEntity = userController.createUser(createUserRequest);
        assertNotNull(itemResponseEntity);
        assertNotEquals(itemResponseEntity.getStatusCode(), HttpStatus.OK);

    }
    @Test

    public void test_find_user_by_id() {
        ResponseEntity<User> responseEntity = userController.findById(1l);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }
    @Test

    public void test_find_user_by_name() {
        ResponseEntity<User> response = userController.findByUserName("fineuser");
        assertEquals(response.getStatusCode(), HttpStatus.OK);

    }
}