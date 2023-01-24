package http.client;

import com.google.gson.JsonObject;
import http.dto.Token;
import http.model.Credentials;
import http.model.User;

import static http.config.BurgersApi.*;

public class UserClient extends BaseClient {

    public Token login(Credentials credentials) {
        var response = reqSpec
                .body(credentials)
                .when()
                .post(USER_LOGIN_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract();

        return new Token(response.path("accessToken"), response.path("refreshToken"));
    }

    public String loginWithBadParams(Credentials credentials) {
        return reqSpec
                .body(credentials)
                .when()
                .post(USER_LOGIN_PATH)
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
    }

    public void logout(Token token) {
        var requestBody = new JsonObject();
        requestBody.addProperty("token", token.getRefreshToken());

        reqSpec
                .body(requestBody.toString())
                .when()
                .post(USER_LOGOUT_PATH)
                .then()
                .assertThat()
                .statusCode(200);
    }

    public Token create(User user) {
        var response = reqSpec
                .body(user)
                .when()
                .post(USER_REGISTER_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract();

        return new Token(response.path("accessToken"), response.path("refreshToken"));
    }

    public void delete(Token token) {
        reqSpec
                .header("authorization", token.getAccessToken())
                .when()
                .delete(USER_PROFILE_PATH)
                .then()
                .assertThat()
                .statusCode(202);
    }

    public String createBadUser(User user) {
        return reqSpec
                .body(user)
                .when()
                .post(USER_REGISTER_PATH)
                .then()
                .assertThat()
                .statusCode(403)
                .extract()
                .path("message");
    }

    public String changeUserEmail(Token token, String email) {
        var requestBody = new JsonObject();
        requestBody.addProperty("email", email);

        return reqSpec
                .header("authorization", token.getAccessToken())
                .body(requestBody.toString())
                .when()
                .patch(USER_PROFILE_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("user.email");
    }

    public String changeUserName(Token token, String name) {
        var requestBody = new JsonObject();
        requestBody.addProperty("name", name);

        return reqSpec
                .header("authorization", token.getAccessToken())
                .body(requestBody.toString())
                .when()
                .patch(USER_PROFILE_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("user.name");
    }

    public void changeUserPassword(Token token, String password) {
        var requestBody = new JsonObject();
        requestBody.addProperty("password", password);

        reqSpec
                .header("authorization", token.getAccessToken())
                .body(requestBody.toString())
                .when()
                .patch(USER_PROFILE_PATH)
                .then()
                .assertThat()
                .statusCode(200);
    }

    public String changeUserRejection(String json) {
        return reqSpec
                .body(json)
                .when()
                .patch(USER_PROFILE_PATH)
                .then()
                .assertThat()
                .statusCode(401)
                .extract()
                .path("message");
    }

    public String getUserEmail(Token token) {
        return reqSpec
                .header("authorization", token.getAccessToken())
                .when()
                .patch(USER_PROFILE_PATH)
                .then()
                .assertThat()
                .statusCode(200)
                .extract()
                .path("user.email");
    }
}
