package uz.backecommers.identety.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class UserLoginRequest {
    @Pattern(regexp = "^998\\d{9}$", message = "Telefon raqami 998 bilan boshlanishi va jami 12 raqam bo'lishi kerak.")
    String phoneNumber;

    String password;
}
