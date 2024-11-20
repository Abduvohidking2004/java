package uz.backecommers.identety.dto.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    @Pattern(regexp = "^998\\d{9}$", message = "Telefon raqami 998 bilan boshlanishi va jami 12 raqam bo'lishi kerak.")
    String phoneNumber;

    @NotBlank(message = "Password is mandatory")
    @NotNull
    String password;

    @NotBlank(message = "Password is mandatory")
    @NotNull
    String confirmPassword;
}