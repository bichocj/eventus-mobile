package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * Created by Administrador on 25/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FrequentlySync implements Serializable{
    String estimate;
    String last_sync;

    public FrequentlySync(String estimate, String last_sync) {
        this.estimate = estimate;
        this.last_sync = last_sync;
    }

    public FrequentlySync() {
    }

    public String getEstimate() {
        return estimate;
    }

    public void setEstimate(String estimate) {
        this.estimate = estimate;
    }

    public String getLast_sync() {
        return last_sync;
    }

    public void setLast_sync(String last_sync) {
        this.last_sync = last_sync;
    }
}
