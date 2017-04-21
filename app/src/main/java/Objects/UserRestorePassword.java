package Objects;

/**
 * Created by Administrador on 20/04/2017.
 */
public class UserRestorePassword {
    String email;

    public UserRestorePassword() {
    }

    public UserRestorePassword(String email) {

        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
