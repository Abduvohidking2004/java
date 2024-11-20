package uz.backecommers.sms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import uz.backecommers.enums.NotificationReadStatus;
import uz.backecommers.enums.NotificationSendStatus;
import uz.backecommers.enums.NotificationType;
import uz.backecommers.identety.entity.Users;

import java.sql.Timestamp;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private boolean state = true;

    private String title;

    private String message;

    private String link;

    private String code;

    private Timestamp created_at = new Timestamp(System.currentTimeMillis());

    private Timestamp sendAt;

    @Enumerated(value = EnumType.STRING)
    private NotificationType type;
    @ManyToOne
    private Users user;

    private UUID senderId = UUID.randomUUID();

    @Enumerated(value = EnumType.STRING)
    private NotificationSendStatus sendStatus = NotificationSendStatus.SCHEDULED;

    @Enumerated(value = EnumType.STRING)
    private NotificationReadStatus status = NotificationReadStatus.UNREAD;
}
