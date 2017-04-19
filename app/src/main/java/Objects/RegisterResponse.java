package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

import app.num.barcodescannerproject.MainActivity;

/**
 * Created by Administrador on 31/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterResponse implements Serializable {
    String pk;
    String activity;
    Register register;
    String is_active;
    String registered_at;
    String updated_at;
    String have_attendance;

    public RegisterResponse() {
        register=new Register();
    }

    public RegisterResponse(String pk, String activity,String ticket_name, String first_name,String last_name,String dni, String is_active, String registered_at, String updated_at, String have_attendance,String pk_person,String pk_register) {
        this.pk = pk;
        this.activity = activity;
        this.register=new Register();
        this.register.setPk(pk_register);
        this.register.getTicket().setName(ticket_name);
        this.register.getPerson().setFirst_name(first_name);
        this.register.getPerson().setLast_name(last_name);
        this.register.getPerson().setDni(dni);
        this.register.getPerson().setPk(pk_person);
        this.is_active = is_active;
        this.registered_at = registered_at;
        this.updated_at = updated_at;
        this.have_attendance = have_attendance;
    }
    public RegisterResponse(String pk, String activity,String ticket_name, String first_name,String last_name,String have_attendance,String pk_register) {
        this.pk = pk;
        this.activity = activity;
        this.register=new Register();
        this.register.setPk(pk_register);
        this.register.getTicket().setName(ticket_name);
        this.register.getPerson().setFirst_name(first_name);
        this.register.getPerson().setLast_name(last_name);
        this.have_attendance = have_attendance;
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public Register getRegister() {
        return register;
    }

    public void setRegister(Register register) {
        this.register = register;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getRegistered_at() {
        return registered_at;
    }

    public void setRegistered_at(String registered_at) {
        this.registered_at = registered_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getHave_attendance() {
        return have_attendance;
    }

    public void setHave_attendance(String have_attendance) {
        this.have_attendance = have_attendance;
    }
}
