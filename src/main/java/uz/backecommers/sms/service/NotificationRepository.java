package uz.backecommers.sms.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backecommers.sms.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {

}
