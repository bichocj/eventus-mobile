package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 31/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Register implements Serializable {
    Ticket ticket;
    Person person;
    String pk;

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public Register() {
        ticket= new Ticket();
        person= new Person();
    }

    public Register(Ticket ticket, Person person,String pk) {

        this.ticket = ticket;
        this.person = person;
        this.pk=pk;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}
