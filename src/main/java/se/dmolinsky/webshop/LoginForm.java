package se.dmolinsky.webshop;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class LoginForm {

    @NotBlank(message="Username cannot be blank")
    @Size(min=5,max=25, message="Username has to be min 5 and max 25 letters.")
    private String username;

    @NotBlank(message="Password cannot be blank")
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
