package http.model;

import lombok.Data;

@Data
public class Credentials {
    private String email;
    private String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public Credentials(http.model.User user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }
}
