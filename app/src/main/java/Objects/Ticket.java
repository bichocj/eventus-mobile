package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 31/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket implements Serializable {
    String pk;
    String name;
    String quantity;
    String cost;
    String event;
    String created_at;
    String is_active;

    public Ticket() {
    }

    public Ticket(String pk, String name, String quantity, String cost, String event, String created_at, String is_active) {
        this.pk = pk;
        this.name = name;
        this.quantity = quantity;
        this.cost = cost;
        this.event = event;
        this.created_at = created_at;
        this.is_active = is_active;
    }

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

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }
}
