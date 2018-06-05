package guru.springframework.api.v1.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CustomerDTO {
    private String firstname;
    private String lastname;

    // in the video JT had this @JsonProperty, I am commenting it out just to have it as reference material!
    // @JsonProperty("customerUrl")
    private String customer_url;
}
