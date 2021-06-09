package ru.diasoft.digitalq.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import ru.diasoft.digitalq.domain.SmsVerification;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.EntityNotFoundException;

@DisplayName("SmsVerificationRepository Test")
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
class SmsVerificationRepositoryTest {

    @Autowired
    private SmsVerificationRepository repository;
	
    private SmsVerification getMockBean(String guid) {
    	return SmsVerification.builder()
    			.creationDate(new Date())
    			.guid(guid)//"ghtdu-kjuyg-qwert-bghcr")
    			.phoneNumber("8-495-777-77-77")
    			.secretCode("123")
    			.status("CREATED")
    			.build();
    }
    
    @Test
    @DisplayName("create")
    void create() {
    	SmsVerification mock = getMockBean("ghtdu-kjuyg-qwert-bghcr");
    	SmsVerification createdEntity = repository.save(mock);

        assertThat(createdEntity).isNotNull();
        assertThat(mock).isEqualToIgnoringGivenFields(createdEntity, "id");
        assertThat(createdEntity.getSmsVerifyId()).isNotNull();
    }

    @Test
    @DisplayName("findByGuidAndSecretCodeAndStatus")
    void findByGuidAndSecretCodeAndStatus() {
    	String guid = "ghtdu-kjuyg-qwert-00000";
    	String secretCode = "123";
    	String status = "CREATED";
    	SmsVerification mock = SmsVerification.builder()
    			.creationDate(new Date())
    			.guid(guid)
    			.phoneNumber("8-495-777-77-77")
    			.secretCode(secretCode)
    			.status(status)
    			.build();
    	SmsVerification createdEntity = repository.save(mock);
    	log.error("findByGuidAndSecretCodeAndStatus createdEntity="+createdEntity);
    	assertThat(repository.findByGuidAndSecretCodeAndStatus(guid, secretCode, status).orElse(SmsVerification.builder().build()))
    		.isEqualToIgnoringGivenFields(createdEntity, "creationDate");
    	assertThat(repository.findByGuidAndSecretCodeAndStatus("222", secretCode, status)).isEmpty();
    }
    
    
    
    
    @Test
    @DisplayName("findById")
    void findById() {
    	SmsVerification link = repository.save(getMockBean("ghtdu-kjuyg-qwert-bgh22"));

    	SmsVerification link2 = repository.findById(link.getSmsVerifyId())
                .orElseThrow(EntityNotFoundException::new);

        Assertions.assertNotNull(link2);
    }
    
    @Test
    @DisplayName("update")
    void update() {
    	SmsVerification mock = getMockBean("ghtdu-kjuyg-qwert-bgh33");
        SmsVerification link = repository.save(mock);
        mock.setSmsVerifyId(link.getSmsVerifyId());

        mock.setPhoneNumber("8-495-888-88-99");
        mock.setStatus("MODIFY");
        
        repository.save(mock);

        SmsVerification link2 = repository.findById(mock.getSmsVerifyId())
                .orElseThrow(EntityNotFoundException::new);

        Assertions.assertEquals(mock, link2);
        
    }

    @Test
    @DisplayName("delete")
    void delete() {
        SmsVerification link = repository.save(getMockBean("ghtdu-kjuyg-qwert-bgh44"));

        Long modelId = link.getSmsVerifyId();
        repository.deleteById(modelId);

        Assertions.assertThrows(Exception.class, () -> {
        	repository.findById(modelId)
                    .orElseThrow(Exception::new);
        });
    }

}