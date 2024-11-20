package uz.backecommers.sms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import uz.backecommers.sms.entity.Otp;


import java.util.List;
import java.util.UUID;

@Repository
public interface OtpRepository extends JpaRepository<Otp, UUID> {
    List<Otp> getTopBySend(boolean send);
}
