package uz.backecommers.sms.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;
import java.util.UUID;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "otp")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, unique = true)
    UUID id;

    String content;

    String code; // hashed code

    String phoneNumber;

    @Builder.Default
    Integer repeat = 0; // 3 marta

    @Builder.Default
    Boolean used = Boolean.FALSE;

    @Builder.Default
    Date createdDate = new Date();
    Date modifiedDate;

    boolean send = false;

    public Otp setUsed(Boolean used) {
        this.used = used;
        this.modifiedDate = new Date();
        return this;
    }

    public Otp setRepeat(Integer repeat) {
        this.repeat = repeat;
        this.modifiedDate = new Date();
        return this;
    }

//    public Boolean getUsedTime() {
//        return LocalDateTime.now().isAfter(createdDate);
//    }

    public Long getUsedTime() {
        return new Date().getTime() - getCreatedDate().getTime();
    }

}