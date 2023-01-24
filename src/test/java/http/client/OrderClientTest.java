package http.client;

import generator.UserGenerator;
import http.dto.Ingredients;
import http.dto.Order;
import http.dto.Token;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import http.model.Credentials;
import http.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.TestCase.*;
import static org.junit.Assert.assertEquals;

public class OrderClientTest {
    private UserClient userClient;
    private Token token;

    @Before
    public void setup() {
        userClient = new UserClient();
    }

    @After
    public void teardown() {
        userClient.delete(token);
    }

    @Test
    @DisplayName("Order creation")
    @Description("Order creation with ingredients and auth")
    public void orderCreateWithAuthTest() {
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(5));
        User user = UserGenerator.random();
        token = userClient.create(user);
        Order order = orderClient.createOrderWithAuthUser(token, ingredients);
        assertEquals(order.getIngredients().size(), ingredients.getIngredients().size());
        for (int i = 0; i < order.getIngredients().size() - 1; i++) {
            assertEquals(order.getIngredients().get(i).getId(), ingredients.getIngredients().get(i));
        }

        assertNotNull(order.getId());
        assertNotNull(order.getStatus());
        assertTrue(order.getNumber() > 0);
        assertTrue(order.getPrice() > 0);
    }

    @Test
    @DisplayName("Order creation")
    @Description("Order creation without auth and without ingredients")
    public void orderCreateWithoutAuthAndIngredientsTest() {
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(3));
        User user = UserGenerator.random();
        token = userClient.create(user);
        Credentials credentials = new Credentials(user);
        userClient.logout(token);
        Order order = orderClient.createOrderWithoutAuthUser(ingredients);
        token = userClient.login(credentials);

        assertNull(order.getIngredients());
        assertNull(order.getId());
        assertNull(order.getOrderUser());
        assertNull(order.getStatus());
        assertNull(order.getName());
        assertTrue(order.getNumber() > 0);
        assertEquals(order.getPrice(), 0);
    }

    @Test
    @DisplayName("Order creation")
    @Description("Order creation with auth and without ingredients")
    public void orderCreateWithAuthAndWithoutIngredientsTest() {
        String expected_message = "Ingredient ids must be provided";
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(3));

        User user = UserGenerator.random();
        token = userClient.create(user);
        String actual_message = orderClient.createOrderWithoutIngredients(token);

        assertEquals(expected_message, actual_message);
    }

    @Test
    @DisplayName("Order creation")
    @Description("Order creation with auth and with incorrect hash of ingredients")
    public void orderCreateWithAuthAndWithWrongHashIngredientsTest() {
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(List.of(RandomStringUtils.randomAlphabetic(10)));
        User user = UserGenerator.random();
        token = userClient.create(user);
        orderClient.createOrderWithIncorrectHashIngredients(token, ingredients);
    }

    @Test
    @DisplayName("Getting orders without auth")
    @Description("Getting orders for not auth user")
    public void getOrderNegativeTest() {
        String expectedMessage = "You should be authorised";
        OrderClient orderClient = new OrderClient();
        OrderClient orderClientNew = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(5));
        User user = UserGenerator.random();
        token = userClient.create(user);
        Credentials credentials = new Credentials(user);
        userClient.logout(token);
        String actualMessage = orderClientNew.getOrderWithoutUserAuth();
        token = userClient.login(credentials);

        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Getting orders with auth")
    @Description("Getting orders with authorisation")
    public void getOrderPositiveTest() {
        OrderClient orderClient = new OrderClient();
        Ingredients ingredients = new Ingredients();
        ingredients.setIngredients(orderClient.getListIngredients(5));
        User user = UserGenerator.random();
        token = userClient.create(user);

        Order order0 = orderClient.createOrderWithAuthUser(token, ingredients);
        List<Integer> actualNumberOrders = orderClient.getOrderWithUserAuth(token);

        assertEquals(actualNumberOrders.size(), 1);
        assertEquals(order0.getNumber(), actualNumberOrders.get(0).intValue());
    }
}