package uz.backecommers.attachment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.backecommers.Base.EntityBase;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Attachment extends EntityBase {

    private String fileName;
    private String originalFileName;
    private String filePath;
    private String type;
    private Long size;


}
