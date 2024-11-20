package uz.backecommers.core.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import uz.backecommers.Base.EntityBase;
import uz.backecommers.identety.entity.Users;

import java.util.ArrayList;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Post extends EntityBase {
    private String title;
    private String description;
    @ManyToOne
    private Users user;
}
