package ru.diasoft.digitalq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ru.diasoft.digitalq.domain.SmsVerification;

import java.util.List;
import java.util.Optional;

@Repository
public interface SmsVerificationRepository extends JpaRepository<SmsVerification, Long> {

    List<SmsVerification> findByGuid(String guid);
    
    Optional<SmsVerification> findByGuidAndSecretCodeAndStatus(String guid, String secretCode, String status);
    
}
