package http.config;

public interface BurgersApi {
    public static final String BASE_URL = "https://stellarburgers.nomoreparties.site/";

    public static final String ORDERS_PATH = "/api/orders";

    public static final String INGREDIENTS_PATH = "/api/ingredients";

    public static final String USER_REGISTER_PATH = "/api/auth/register";
    public static final String USER_LOGIN_PATH = "api/auth/login";
    public static final String USER_LOGOUT_PATH = "api/auth/logout";
    public static final String USER_PROFILE_PATH = "api/auth/user";
}
