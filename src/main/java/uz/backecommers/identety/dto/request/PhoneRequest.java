package uz.backecommers.identety.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PhoneRequest {

    @JsonProperty("phone_number")
    @Pattern(regexp = "^998\\d{9}$", message = "Telefon raqami 998 bilan boshlanishi va jami 12 raqam bo'lishi kerak.")
    private String phoneNumber;
}
