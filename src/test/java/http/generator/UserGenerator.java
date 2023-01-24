package http.generator;

import lombok.Data;
import http.model.User;

import static http.generator.UserdataGenerator.*;

@Data
public class UserGenerator {
    public static User empty() {
        return new User("", "", "");
    }

    public static User random() {
        return new User(randomEmail(), randomPassword(), randomUsername());
    }

    public static User randomWithEmail(String email) {
        return new User(email, randomPassword(), randomUsername());
    }

    public static User randomWithPassword(String password) {
        return new User(randomEmail(), password, randomUsername());
    }

    public static User randomWithName(String username) {
        return new User(randomEmail(), randomPassword(), username);
    }
}
