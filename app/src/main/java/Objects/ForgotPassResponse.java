package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 20/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ForgotPassResponse implements Serializable{
    public ForgotPassResponse() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String status) {
        this.email = status;
    }

    String email;
    

}
