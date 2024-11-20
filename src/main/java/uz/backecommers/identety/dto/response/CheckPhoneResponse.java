package uz.backecommers.identety.dto.response;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CheckPhoneResponse {
    private boolean isPresent;
}
