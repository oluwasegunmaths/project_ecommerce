package com.example.demo.controllers;

import com.example.demo.Utils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)

@SpringBootTest()
public class ItemControllerTest {

    private ItemController itemController;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    private final String itemName="itemname";
    @Before
    public void setUp() {
        itemController = new ItemController();
        Utils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Widget");
        when(itemRepository.findAll()).thenReturn(Collections.singletonList(item));
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName(itemName)).thenReturn(Collections.singletonList(item));
    }

    @Test
    public void testGetItems() {
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItems();
        assertEquals(itemResponseEntity.getBody().get(0).getName(),"Widget");
    }

    @Test
    public void testGetItemById() {
        ResponseEntity<Item> itemResponseEntity = itemController.getItemById(1l);
        assertEquals(itemResponseEntity.getBody().getName(),"Widget");

    }
    @Test
    public void testGetItemByName() {
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName(itemName);
        assertEquals(itemResponseEntity.getStatusCode(), HttpStatus.OK);

    }
    @Test
    public void testGetFakeItemByName() {
        ResponseEntity<List<Item>> itemResponseEntity = itemController.getItemsByName("fakename");
        assertEquals(itemResponseEntity.getStatusCode(), HttpStatus.NOT_FOUND);

    }

}