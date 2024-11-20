package uz.backecommers.identety.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import uz.backecommers.Base.EntityBase;
import uz.backecommers.enums.GenderEnum;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Builder

@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auth_users")
public class Users extends EntityBase {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @JsonIgnore
    private String password;

    private String mobilePhone;

    private String fullName;

    private LocalDate birthDate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Permission> roles = new HashSet<>();

    Long telegramId;

    @Enumerated(EnumType.STRING)
    private GenderEnum gender;

    @Builder.Default
    Boolean is_register = Boolean.FALSE;


    public Users(String phoneNumber, String password) {
        this.mobilePhone = phoneNumber;
        this.password = password;
        this.username = phoneNumber;
    }
}
