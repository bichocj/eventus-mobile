package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.Comparator;

import app.num.barcodescannerproject.MainActivity;

/**
 * Created by Administrador on 31/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterResponse implements Serializable {
    String pk;
    String first_name;
    String last_name;
    String have_attendance;
    public String getHave_attendance() {
        return have_attendance;
    }

    public void setHave_attendance(String have_attendance) {
        this.have_attendance = have_attendance;
    }



    public RegisterResponse(String pk, String first_name, String last_name) {
        this.pk = pk;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    public RegisterResponse() {
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }


    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }
}
class NameComparator implements Comparator<RegisterResponse>
{
    @Override
    public int compare(RegisterResponse lhs, RegisterResponse rhs) {
        return lhs.getFirst_name().compareTo(rhs.getFirst_name());
    }
}