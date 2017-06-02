package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 02/05/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class AttendanceRequest  implements Serializable{
    String register;

    public AttendanceRequest() {
    }

    public AttendanceRequest(String register) {

        this.register = register;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
