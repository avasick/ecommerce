package com.example.ecommerce;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.RandomStringUtils.randomNumeric;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.example.ecommerce.controller.OrderController;
import com.example.ecommerce.controller.ProductController;
import com.example.ecommerce.dto.OrderProductDto;
import com.example.ecommerce.model.*;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { EcomerceApplication.class }, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EcommerceLiveTest {
    private static final String ORDER_API_ROOT = "http://localhost:8080/api/orders";
    private static final String PRODUCT_API_ROOT = "http://localhost:8080/api/products";

    @Autowired private TestRestTemplate restTemplate;

    @LocalServerPort private int port;

    @Autowired private ProductController productController;

    @Autowired private OrderController orderController;

    private Order createRandomOrder() {
        ArrayList <Product> products = new ArrayList<>();

        products.add(new Product(1L, "TV Set", 300.00, "http://placehold.it/200x100"));
        products.add(new Product(2L, "Game Console", 200.00, "http://placehold.it/200x100"));
        products.add(new Product(3L, "Sofa", 100.00, "http://placehold.it/200x100"));
        products.add(new Product(4L, "Icecream", 5.00, "http://placehold.it/200x100"));
        products.add(new Product(5L, "Beer", 3.00, "http://placehold.it/200x100"));
        products.add(new Product(6L, "Phone", 500.00, "http://placehold.it/200x100"));
        products.add(new Product(7L, "Watch", 30.00, "http://placehold.it/200x100"));
        Order order = new Order();
        ArrayList<OrderProduct> orders = new ArrayList<>();
        int numProducts = (int)(Math.random() * products.size());
        for(int i = 0; i < numProducts; ++i){
            orders.add(new OrderProduct(order, products.get(i), (int)Math.random() * 10));
        }

        order.setOrderProducts(orders);
        order.setId((long)(Math.random() * 100));

        return order;
    }

    private String createOrderAsUri(Order order) {
        final Response response = RestAssured.given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(order)
                .post(ORDER_API_ROOT);
        return ORDER_API_ROOT + "/" + response.jsonPath()
                .get("id");
    }

    @Test
    public void whenGetAllOrders_thenOK() {
        final Response response = RestAssured.get("http://localhost:" + port + "/api/products");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void whenGetAllProducts_thenOK() {
        final Response response = RestAssured.get("http://localhost:" + port + "/api/products");
        assertEquals(HttpStatus.OK.value(), response.getStatusCode());
    }

    @Test
    public void contextLoads() {
        Assertions
                .assertThat(productController)
                .isNotNull();
        Assertions
                .assertThat(orderController)
                .isNotNull();
    }

    @Test
    public void givenGetProductsApiCall_whenProductListRetrieved_thenSizeMatchAndListContainsProductNames() {
        ResponseEntity<Iterable<Product>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/products", HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<Product>>() {
        });
        Iterable<Product> products = responseEntity.getBody();
        Assertions
                .assertThat(products)
                .hasSize(7);

        assertThat(products, hasItem(hasProperty("name", is("TV Set"))));
        assertThat(products, hasItem(hasProperty("name", is("Game Console"))));
        assertThat(products, hasItem(hasProperty("name", is("Sofa"))));
        assertThat(products, hasItem(hasProperty("name", is("Icecream"))));
        assertThat(products, hasItem(hasProperty("name", is("Beer"))));
        assertThat(products, hasItem(hasProperty("name", is("Phone"))));
        assertThat(products, hasItem(hasProperty("name", is("Watch"))));
    }

    @Test
    public void givenGetOrdersApiCall_whenProductListRetrieved_thenSizeMatchAndListContainsProductNames() {
        ResponseEntity<Iterable<Order>> responseEntity = restTemplate.exchange("http://localhost:" + port + "/api/orders", HttpMethod.GET, null, new ParameterizedTypeReference<Iterable<Order>>() {
        });

        Iterable<Order> orders = responseEntity.getBody();
        Assertions
                .assertThat(orders)
                .hasSize(0);
    }

    @Test
    public void givenPostOrder_whenBodyRequestMatcherJson_thenResponseContainsEqualObjectProperties() {
        final ResponseEntity<Order> postResponse = restTemplate.postForEntity("http://localhost:" + port + "/api/orders", prepareOrderForm(), Order.class);
        Order order = postResponse.getBody();
        Assertions
                .assertThat(postResponse.getStatusCode())
                .isEqualByComparingTo(HttpStatus.CREATED);

        assertThat(order, hasProperty("status", is("PAID")));
        assertThat(order.getOrderProducts(), hasItem(hasProperty("quantity", is(2))));
    }

    private OrderController.OrderForm prepareOrderForm() {
        OrderController.OrderForm orderForm = new OrderController.OrderForm();
        OrderProductDto productDto = new OrderProductDto();
        productDto.setProduct(new Product(1L, "TV Set", 300.00, "http://placehold.it/200x100"));
        productDto.setQuantity(2);
        orderForm.setProductOrders(Collections.singletonList(productDto));

        return orderForm;
    }


}
