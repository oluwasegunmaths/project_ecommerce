package com.example.demo.controllers;

import com.example.demo.Utils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)

@SpringBootTest()
public class OrderControllerTest {

    private OrderController orderController;

    private final UserRepository userRepository = mock(UserRepository.class);
    private final OrderRepository orderRepository= mock(OrderRepository.class);
    private final String username="fineuser";
    @Before
    public void setUp() {
        orderController = new OrderController();
        Utils.injectObjects(orderController, "userRepository", userRepository);
        Utils.injectObjects(orderController, "orderRepository", orderRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Widget");
        item.setPrice(new BigDecimal("5.5"));
        User user=new User();
        Cart cart = new Cart();
        cart.addItem(item);
        user.setCart(cart);
        when(userRepository.findByUsername(username)).thenReturn(user);
    }

    @Test
    public void test_submit_and_get_order() {
        ResponseEntity<UserOrder> cartResponseEntity = orderController.submit(username);
        assertEquals(cartResponseEntity.getStatusCode(), HttpStatus.OK);
        ResponseEntity<List<UserOrder>> response =
                orderController.getOrdersForUser(username);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

    }
    @Test

    public void test_submit_for_fake_user() {
        ResponseEntity<UserOrder> cartResponseEntity = orderController.submit("fakeuser");
        assertNotNull(cartResponseEntity);
        assertEquals(cartResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);
    }
    @Test
    public void test_get_orders_for_fake_user(){
        ResponseEntity<List<UserOrder>> response =
                orderController.getOrdersForUser("fakeuser");
        assertNotNull(response);
        assertEquals(response.getStatusCode(), HttpStatus.NOT_FOUND);
    }
}