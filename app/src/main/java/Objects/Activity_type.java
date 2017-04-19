package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 30/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity_type implements Serializable {
    String pk;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String name;

    public Activity_type() {

    }

    public Activity_type(String pk, String name) {
        this.pk = pk;
        this.name = name;
    }
}
