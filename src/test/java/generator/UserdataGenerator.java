package generator;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class UserdataGenerator {
    public static String randomUsername() {
        return RandomStringUtils.randomAlphabetic(5);
    }

    public static String randomEmail() {
        return RandomStringUtils.randomAlphabetic(5).toLowerCase() + "@yandex.ru";
    }

    public static String randomPassword() {
        return RandomStringUtils.randomAlphabetic(10);
    }
}