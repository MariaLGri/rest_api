package models.lombok;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateUserResponseLombokTehModel {
    private String name;
    private String job;
    private String id;
    @JsonProperty("createdAt")
    private String createdAt;
}
