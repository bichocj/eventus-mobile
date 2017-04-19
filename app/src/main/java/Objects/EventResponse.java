package Objects;

/**
 * Created by Administrador on 30/07/2016.
 */
public class EventResponse {
    String pk;
    String name;
    String short_name;
    String logo;
    String main_email;
    String second_email;

    public EventResponse(String pk, String name, String short_name, String logo, String main_email, String second_email, String payment_account_info, String domain, String description, String slug, String slogan, String start_at, String end_at, String owner, String is_active, String event_type, String city, String localitation, String address) {
        this.pk = pk;
        this.name = name;
        this.short_name = short_name;
        this.logo = logo;
        this.main_email = main_email;
        this.second_email = second_email;
        this.payment_account_info = payment_account_info;
        this.domain = domain;
        this.description = description;
        this.slug = slug;
        this.slogan = slogan;
        this.start_at = start_at;
        this.end_at = end_at;
        this.owner = owner;
        this.is_active = is_active;
        this.event_type = event_type;
        this.city = city;
        this.localitation = localitation;
        this.address = address;
    }

    String payment_account_info;

    public String getShort_name() {
        return short_name;
    }

    public void setShort_name(String short_name) {
        this.short_name = short_name;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getMain_email() {
        return main_email;
    }

    public void setMain_email(String main_email) {
        this.main_email = main_email;
    }

    public String getSecond_email() {
        return second_email;
    }

    public void setSecond_email(String second_email) {
        this.second_email = second_email;
    }

    public String getPayment_account_info() {
        return payment_account_info;
    }

    public void setPayment_account_info(String payment_account_info) {
        this.payment_account_info = payment_account_info;
    }

    String domain;
    String description;
    String slug;
    String slogan;
    String start_at;
    String end_at;
    String owner;
    String is_active;
    String event_type;
    String city;
    String localitation;
    String address;
    public EventResponse() {

    }

    public EventResponse(String pk, String name, String domain, String description, String slug, String slogan, String start_at, String end_at, String owner, String is_active, String event_type, String city, String localitation, String address) {
        this.pk = pk;
        this.name = name;
        this.domain = domain;
        this.description = description;
        this.slug = slug;
        this.slogan = slogan;
        this.start_at = start_at;
        this.end_at = end_at;
        this.owner = owner;
        this.is_active = is_active;
        this.event_type = event_type;
        this.city = city;
        this.localitation = localitation;
        this.address = address;
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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getIs_active() {
        return is_active;
    }

    public void setIs_active(String is_active) {
        this.is_active = is_active;
    }

    public String getEvent_type() {
        return event_type;
    }

    public void setEvent_type(String event_type) {
        this.event_type = event_type;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
