package ru.geekbrains.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.geekbrains.controller.dto.ProductDto;
import ru.geekbrains.service.dto.LineItem;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CartServiceTest {

    private CartService cartService;

    @BeforeEach
    public void init() {
        this.cartService = new CartServiceImpl();
    }

    @Test
    public void testIfNewCartIsEmpty() {
        assertNotNull(cartService.getLineItems());
        assertEquals(0, cartService.getLineItems().size());
        assertEquals(BigDecimal.ZERO, cartService.getSubTotal());
    }

    @Test
    public void testAddProduct() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setCost(new BigDecimal(500));
        expectedProduct.setTitle("LG 27UP850");

        cartService.addProductQty(expectedProduct, "", "", true, true, 1);
        cartService.addProductQty(expectedProduct, "", "", true, true, 1);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("LG 27UP850", lineItem.getProductDto().getTitle());
        assertEquals(new BigDecimal(500), lineItem.getProductDto().getCost());
        assertTrue(lineItem.getGiftWrap());
        assertEquals(2, lineItem.getQty());
        assertEquals(new BigDecimal(1000), lineItem.getItemTotal());
    }

    @Test
    public void testUpdateProductQty() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setCost(new BigDecimal(500));
        expectedProduct.setTitle("LG 27UP850");

        cartService.addProductQty(expectedProduct, "", "", true, true, 1);

        expectedProduct.setCost(new BigDecimal(520));
        cartService.updateProductQty(expectedProduct, "", "", true, false, 1);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(1, lineItems.size());

        LineItem lineItem = lineItems.get(0);
        assertEquals("LG 27UP850", lineItem.getProductDto().getTitle());
        assertEquals(new BigDecimal(520), lineItem.getProductDto().getCost());
        assertFalse(lineItem.getGiftWrap());
        assertEquals(1, lineItem.getQty());
        assertEquals(new BigDecimal(520), lineItem.getItemTotal());
    }

    @Test
    public void testRemoveProduct() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setCost(new BigDecimal(500));
        expectedProduct.setTitle("LG 27UP850");

        cartService.addProductQty(expectedProduct, "", "", true, true, 4);

        cartService.removeProduct(expectedProduct, "", "", true, true);

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(0, lineItems.size());
    }

    @Test
    public void testClear() {
        ProductDto expectedProduct = new ProductDto();
        expectedProduct.setId(1L);
        expectedProduct.setCost(new BigDecimal(500));
        expectedProduct.setTitle("LG 27UP850");

        cartService.addProductQty(expectedProduct, "", "", true, true, 4);
        cartService.addProductQty(expectedProduct, "", "", false, false, 1);

        cartService.clear();

        List<LineItem> lineItems = cartService.getLineItems();
        assertNotNull(lineItems);
        assertEquals(0, lineItems.size());
    }
}
