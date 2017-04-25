package Objects;

import android.app.Activity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrador on 23/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SyncObject implements Serializable{
    List<EventResponse> events;
    List<ActivityResponse> activities;
    List<RegisterResponse> registers;
    NumberResponse numbers;

    public SyncObject(List<EventResponse> events, List<ActivityResponse> activities, List<RegisterResponse> registers, NumberResponse numbers) {
        this.events = events;
        this.activities = activities;
        this.registers = registers;
        this.numbers = numbers;
    }

    public SyncObject() {
    }

    public List<EventResponse> getEvents() {
        return events;
    }

    public void setEvents(List<EventResponse> events) {
        this.events = events;
    }

    public List<ActivityResponse> getActivities() {
        return activities;
    }

    public void setActivities(List<ActivityResponse> activities) {
        this.activities = activities;
    }

    public List<RegisterResponse> getRegisters() {
        return registers;
    }

    public void setRegisters(List<RegisterResponse> registers) {
        this.registers = registers;
    }

    public NumberResponse getnumbers() {
        return numbers;
    }

    public void setnumbers(NumberResponse numbers) {
        this.numbers = numbers;
    }
}
