package Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Administrador on 23/04/2017.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberResponse {
    ActivityProximate activities;
    RegisterProximate registers;
    EventProximate events;
}
