package Objects;

/**
 * Created by Administrador on 30/07/2016.
 */
public class EventResponse {
    String pk;
    String slug;
    String name;
    String domain;
    String start_at;
    String end_at;
    String localitation;
    String address;

    public EventResponse(String pk, String slug, String name, String domain, String start_at, String end_at, String localitation, String address) {
        this.pk = pk;
        this.slug = slug;
        this.name = name;
        this.domain = domain;
        this.start_at = start_at;
        this.end_at = end_at;
        this.localitation = localitation;
        this.address = address;
    }

    public EventResponse() {
    }

    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getStart_at() {
        return start_at;
    }

    public void setStart_at(String start_at) {
        this.start_at = start_at;
    }

    public String getEnd_at() {
        return end_at;
    }

    public void setEnd_at(String end_at) {
        this.end_at = end_at;
    }

    public String getLocalitation() {
        return localitation;
    }

    public void setLocalitation(String localitation) {
        this.localitation = localitation;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
