package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 30/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityResponse implements Serializable {
    String pk;
    String name;
    java.lang.String start_at;
    java.lang.String end_at;
    java.lang.String address;
    java.lang.String capacity;
    Type _type;
    java.lang.String event;
    java.lang.String speaker;
    java.lang.String avaliable;
    java.lang.String registered_number;
    java.lang.String attendees;
    java.lang.String institution;

    public java.lang.String getPk() {
        return pk;
    }

    public void setPk(java.lang.String pk) {
        this.pk = pk;
    }

    public java.lang.String getName() {
        return name;
    }

    public void setName(java.lang.String name) {
        this.name = name;
    }

    public java.lang.String getStart_at() {
        return start_at;
    }

    public void setStart_at(java.lang.String start_at) {
        this.start_at = start_at;
    }

    public java.lang.String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(java.lang.String end_at) {
        this.end_at = end_at;
    }

    public java.lang.String getAddress() {
        return address;
    }

    public void setAddress(java.lang.String address) {
        this.address = address;
    }

    public java.lang.String getCapacity() {
        return capacity;
    }

    public void setCapacity(java.lang.String capacity) {
        this.capacity = capacity;
    }

    public Type getType() {
        return _type;
    }

    public void setType(Type type) {
        this._type = type;
    }

    public java.lang.String getEvent() {
        return event;
    }

    public void setEvent(java.lang.String event) {
        this.event = event;
    }

    public java.lang.String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(java.lang.String speaker) {
        this.speaker = speaker;
    }

    public java.lang.String getAvaliable() {
        return avaliable;
    }

    public void setAvaliable(java.lang.String avaliable) {
        this.avaliable = avaliable;
    }

    public java.lang.String getRegistered_number() {
        return registered_number;
    }

    public void setRegistered_number(java.lang.String registered_number) {
        this.registered_number = registered_number;
    }

    public java.lang.String getAttendees() {
        return attendees;
    }

    public void setAttendees(java.lang.String attendees) {
        this.attendees = attendees;
    }

    public java.lang.String getInstitution() {
        return institution;
    }

    public void setInstitution(java.lang.String institution) {
        this.institution = institution;
    }

    public ActivityResponse(java.lang.String pk, java.lang.String name, java.lang.String start_at, java.lang.String end_at, java.lang.String address, java.lang.String capacity, java.lang.String eventtype, java.lang.String maxnumber, java.lang.String event, java.lang.String speaker, java.lang.String avaliable, java.lang.String registered_number, java.lang.String attendees, java.lang.String institution) {
        this.pk = pk;
        this.name = name;
        this.start_at = start_at;
        this.end_at = end_at;
        this.address = address;
        this.capacity = capacity;
        this._type=new Type();
        this._type.setLowercased(eventtype);
        this.event = event;
        this.speaker = speaker;
        this.avaliable = avaliable;
        this.registered_number = registered_number;
        this.attendees = attendees;
        this.institution = institution;
    }

    public ActivityResponse() {
        _type=new Type();

    }
}
