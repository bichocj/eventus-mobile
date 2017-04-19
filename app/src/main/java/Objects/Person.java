package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 31/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Person implements Serializable {
    String pk;
    String first_name;
    String last_name;
    String email;
    String last_login;
    String dni;
    String phone_number;

    public Person() {
    }

    public Person(String pk, String first_name, String last_name, String email, String last_login, String dni, String phone_number) {
        this.pk = pk;
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.last_login = last_login;
        this.dni = dni;
        this.phone_number = phone_number;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLast_login() {
        return last_login;
    }

    public void setLast_login(String last_login) {
        this.last_login = last_login;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
