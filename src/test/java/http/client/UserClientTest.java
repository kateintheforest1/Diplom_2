package http.client;

import com.google.gson.JsonObject;
import http.generator.UserdataGenerator;
import http.generator.UserGenerator;
import http.dto.Token;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import http.model.Credentials;
import http.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class UserClientTest {
    private UserClient userClient;
    private Token token;

    @Before
    public void setup() {
        userClient = new UserClient();
        token = null;
    }

    @After
    public void teardown() {
        if (token != null) {
            UserClient asd = new UserClient();
            asd.delete(token);
        }
    }

    @Test
    @DisplayName("User registration")
    @Description("User account created successful")
    public void userRegisterTest() {
        User user = UserGenerator.random();
        token = userClient.create(user);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @DisplayName("Duplicate registration")
    @Description("Failed to create a duplicate user")
    public void userRegisterAnExistedAlreadyTest() {
        String expected = "User already exists";

        User user = UserGenerator.random();
        token = userClient.create(user);

        String actual = userClient.createBadUser(user);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Registration without required fields")
    @Description("Registration without required fields failed")
    public void userRegisterWithEmptyFieldsTest() {
        String expected = "Email, password and name are required fields";
        User user = UserGenerator.empty();

        String actual = userClient.createBadUser(user);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Registration without email")
    @Description("Failed registration without email")
    public void userRegisterWithEmptyEmailTest() {
        String expected = "Email, password and name are required fields";
        User user = UserGenerator.randomWithEmail("");

        String actual = userClient.createBadUser(user);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Registration without login")
    @Description("Failed registration without login")
    public void userRegisterWithEmptyLoginTest() {
        String expected = "Email, password and name are required fields";
        User user = UserGenerator.randomWithName("");

        String actual = userClient.createBadUser(user);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Registration without pass")
    @Description("Failed registration without pass")
    public void userRegisterWithEmptyPasswordTest() {
        String expected = "Email, password and name are required fields";
        User user = UserGenerator.randomWithPassword("");

        String actual = userClient.createBadUser(user);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Unsuccessful editing of user data")
    @Description("Unsuccessful editing user data without auth")
    public void userDataChangeWithoutAuthTest() {
        String expectedMessage = "You should be authorised";
        User user = UserGenerator.random();
        JsonObject jsonWithNewEmail = new JsonObject();

        String newEmail = UserdataGenerator.randomEmail();

        jsonWithNewEmail.addProperty("email", newEmail);
        token = userClient.create(user);
        String expectedOldEmail = user.getEmail();

        Credentials credentials = new Credentials(user);
        String actualMessage = userClient.changeUserRejection(jsonWithNewEmail.toString());

        userClient.logout(token);
        token = userClient.login(credentials);

        assertEquals(expectedMessage, actualMessage);
        assertEquals(expectedOldEmail, userClient.getUserEmail(token));
    }

    @Test
    @DisplayName("User email changed")
    @Description("Successful change of user email")
    public void userEmailChangeWithAuthTest() {
        User user = UserGenerator.random();
        String newEmail = UserdataGenerator.randomEmail();

        token = userClient.create(user);

        userClient.logout(token);

        Credentials credentials = new Credentials(user);
        token = userClient.login(credentials);

        String actualNewEmail = userClient.changeUserEmail(token, newEmail);

        assertEquals(newEmail, actualNewEmail);
    }

    @Test
    @DisplayName("User login changed")
    @Description("Successful change of user login")
    public void changeLoginUserDataWithAuthTest() {
        User user = UserGenerator.random();
        String newUsername = UserdataGenerator.randomUsername();
        Credentials credentials = new Credentials(user);

        Token token1 = userClient.create(user);
        userClient.logout(token1);

        token = userClient.login(credentials);

        String actual = userClient.changeUserName(token, newUsername);

        assertEquals(newUsername, actual);
    }

    @Test
    @DisplayName("User password changed")
    @Description("Successful change of user password")
    public void changePasswordUserDataWithAuthTest() {
        User user = UserGenerator.random();
        String newPassword = UserdataGenerator.randomPassword();
        Credentials credentials = new Credentials(user);

        token = userClient.create(user);
        userClient.logout(token);

        token = userClient.login(credentials);

        userClient.changeUserPassword(token, newPassword);
        userClient.logout(token);

        credentials.setPassword(newPassword);
        token = userClient.login(credentials);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }

    @Test
    @DisplayName("Auth with invalid email")
    @Description("Auth check without email")
    public void userLoginWithInvalidEmail() {
        String expected = "email or password are incorrect";

        User user = UserGenerator.random();
        Credentials credentials = new Credentials("bad-email", "password");

        token = userClient.create(user);
        userClient.logout(token);

        String actual = userClient.loginWithBadParams(credentials);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Auth with invalid pass")
    @Description("Auth check with invalid pass")
    public void userLoginWithInvalidPasswordTest() {
        String expected = "email or password are incorrect";

        User user = UserGenerator.random();
        Credentials credentials = new Credentials(user.getEmail(), "");

        token = userClient.create(user);
        userClient.logout(token);

        String actual = userClient.loginWithBadParams(credentials);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("User auth")
    @Description("Successful user authorization check")
    public void userSuccessLoginTest() {
        User user = UserGenerator.random();
        Credentials credentials = new Credentials(user);

        token = userClient.create(user);
        userClient.logout(token);
        token = userClient.login(credentials);

        assertNotNull(token.getAccessToken());
        assertNotNull(token.getRefreshToken());
    }
}