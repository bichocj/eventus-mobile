package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrador on 30/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActivityResponse implements Serializable {
    String pk;
    String name;
    String starts_at;
    String ends_at;
    String event;
    String info_about ;
    ArrayList<String> registered;

    public ArrayList<String> getRegistered() {
        return registered;
    }

    public void setRegistered(ArrayList<String> registered) {
        this.registered = registered;
    }

    public ActivityResponse(String pk, String name, String starts_at, String ends_at, String event, String info_about, ArrayList<String> registered) {
        this.pk = pk;
        this.name = name;
        this.starts_at = starts_at;
        this.ends_at = ends_at;
        this.event = event;
        this.info_about = info_about;
        this.registered = registered;
    }

    public ActivityResponse(String pk, String name, String starts_at, String ends_at, String event, String info_about) {
        this.pk = pk;
        this.name = name;
        this.starts_at = starts_at;
        this.ends_at = ends_at;
        this.event = event;
        this.info_about = info_about;
    }

    public ActivityResponse() {
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

    public String getStarts_at() {
        return starts_at;
    }

    public void setStarts_at(String starts_at) {
        this.starts_at = starts_at;
    }

    public String getEnds_at() {
        return ends_at;
    }

    public void setEnds_at(String ends_at) {
        this.ends_at = ends_at;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getInfo_about() {
        return info_about;
    }

    public void setInfo_about(String info_about) {
        this.info_about = info_about;
    }
}
