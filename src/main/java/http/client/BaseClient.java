package http.client;

import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;
import static http.config.BurgersApi.BASE_URL;

public class BaseClient {
    public final RequestSpecification reqSpec = given()
            .header("Content-type", "application/json")
            .baseUri(BASE_URL);
}
