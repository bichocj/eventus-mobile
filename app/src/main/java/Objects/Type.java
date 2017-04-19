package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 30/07/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Type implements Serializable {
    java.lang.String pk;
    java.lang.String max_per_user;
    java.lang.String lowercased;
    Activity_type activity_type;
    java.lang.String color;
    java.lang.String is_grouping_by_institution;

    public java.lang.String getPk() {
        return pk;
    }

    public void setPk(java.lang.String pk) {
        this.pk = pk;
    }

    public java.lang.String getMax_per_user() {
        return max_per_user;
    }

    public void setMax_per_user(java.lang.String max_per_user) {
        this.max_per_user = max_per_user;
    }

    public java.lang.String getLowercased() {
        return lowercased;
    }

    public void setLowercased(java.lang.String lowercased) {
        this.lowercased = lowercased;
    }

    public Objects.Activity_type getActivity_type() {
        return activity_type;
    }

    public void setActivity_type(Objects.Activity_type activity_type) {
        this.activity_type = activity_type;
    }

    public java.lang.String getColor() {
        return color;
    }

    public void setColor(java.lang.String color) {
        this.color = color;
    }

    public java.lang.String getIs_grouping_by_institution() {
        return is_grouping_by_institution;
    }

    public void setIs_grouping_by_institution(java.lang.String is_grouping_by_institution) {
        this.is_grouping_by_institution = is_grouping_by_institution;
    }

    public Type() {
        activity_type=new Activity_type();

    }
}
