package com.example.demo.controllers;

import com.example.demo.Utils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)

@SpringBootTest()
public class CartControllerTest {

    private CartController cartController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);
    private final UserRepository userRepository = mock(UserRepository.class);
    private final CartRepository cartRepository= mock(CartRepository.class);
    private  ModifyCartRequest modifyCartRequest;

    @Before
    public void setUp() {
        cartController = new CartController();
        modifyCartRequest=new ModifyCartRequest();
        Utils.injectObjects(cartController, "itemRepository", itemRepository);
        Utils.injectObjects(cartController, "userRepository", userRepository);
        Utils.injectObjects(cartController, "cartRepository", cartRepository);
        modifyCartRequest.setUsername("fineuser");
        modifyCartRequest.setItemId(1l);
        modifyCartRequest.setQuantity(3);
        Item item = new Item();
        item.setId(1L);
        item.setName("Widget");
        item.setPrice(new BigDecimal("5.5"));
        User user=new User();
        user.setCart(new Cart());
        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        when(itemRepository.findById(modifyCartRequest.getItemId())).thenReturn(java.util.Optional.of(item));
    }

    @Test
    public void test_add_to_cart_then_remove() {
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        assertEquals(cartResponseEntity.getBody().getItems().get(0).getName(),"Widget");
        assertEquals(cartResponseEntity.getStatusCode(), HttpStatus.OK);
        ResponseEntity<Cart> responseEntity = cartController.removeFromcart(modifyCartRequest);
        assertEquals(responseEntity.getStatusCode(), HttpStatus.OK);

    }
    @Test

    public void test_add_to_cart_by_fake_user() {
        modifyCartRequest.setUsername("fakeuser");
        ResponseEntity<Cart> cartResponseEntity = cartController.addTocart(modifyCartRequest);
        assertEquals(cartResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    @Test

    public void test_remove_from_cart_by_fake_user() {
        modifyCartRequest.setUsername("fakeuser");
        ResponseEntity<Cart> cartResponseEntity = cartController.removeFromcart(modifyCartRequest);
        assertEquals(cartResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}