package ru.diasoft.digitalq.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import lombok.extern.slf4j.Slf4j;
import ru.diasoft.digitalq.domain.*;
import ru.diasoft.digitalq.repository.SmsVerificationRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@DisplayName("SmsVerificationService Test")
@SpringBootTest
@ActiveProfiles("test")
@Slf4j
@RunWith(SpringRunner.class)
class SmsVerificationServiceTest {

	@Mock
    private SmsVerificationRepository repository;
	
	private SmsVerificationService service;
	
	private final String PHONE_NUMBER = "8-499-111-11-11";
	private final String VALID_CODE = "12345";
	private final String NOT_VALID_CODE = "11111";
	private final String GUID = UUID.randomUUID().toString();
	private final String STATUS_OK = "OK";

	
	@BeforeEach
    public void init() {
		service = new SmsVerificationServiceImpl(repository);
		SmsVerification mock = SmsVerification.builder()
    			.creationDate(new Date())
    			.guid(GUID)
    			.phoneNumber(PHONE_NUMBER)
    			.secretCode(VALID_CODE)
    			.status(STATUS_OK)
    			.build();
		Mockito.when(repository.findByGuidAndSecretCodeAndStatus(GUID, VALID_CODE, STATUS_OK)).thenReturn(Optional.of(mock));
		Mockito.when(repository.findByGuidAndSecretCodeAndStatus(GUID, NOT_VALID_CODE, STATUS_OK)).thenReturn(Optional.empty());
		Mockito.when(repository.save(any(SmsVerification.class))).thenReturn(mock);
    }
    
    @Test
    @DisplayName("dsSmsVerificationCreateTest")
    void dsSmsVerificationCreateTest() {
    	log.info("service="+service);
    	SmsVerificationPostRequest smsVerificationPostRequest = new SmsVerificationPostRequest();
    	smsVerificationPostRequest.setPhoneNumber(PHONE_NUMBER);
    	SmsVerificationPostResponse response = service.dsSmsVerificationCreate(smsVerificationPostRequest);

    	assertThat(response).isNotNull();
    	assertThat(response.getProcessGUID()).isNotEmpty();
    }

    @Test
    @DisplayName("dsSmsVerificationCheckTest")
    void dsSmsVerificationCheckTest() {
    	SmsVerificationCheckRequest smsVerificationCheckRequest = new SmsVerificationCheckRequest();
    	smsVerificationCheckRequest.setProcessGUID(GUID);
    	smsVerificationCheckRequest.setCode(VALID_CODE);
    	SmsVerificationCheckResponse response = service.dsSmsVerificationCheck(smsVerificationCheckRequest);

    	assertThat(response).isNotNull();
    	assertThat(response.getCheckResult()).isTrue();
    }

    @Test
    @DisplayName("dsSmsVerificationCheckTest 2")
    void dsSmsVerificationCheckTest2() {
    	SmsVerificationCheckRequest smsVerificationCheckRequest = new SmsVerificationCheckRequest();
    	smsVerificationCheckRequest.setProcessGUID(GUID);
    	smsVerificationCheckRequest.setCode(NOT_VALID_CODE);
    	SmsVerificationCheckResponse response = service.dsSmsVerificationCheck(smsVerificationCheckRequest);

    	assertThat(response).isNotNull();
    	assertThat(response.getCheckResult()).isFalse();
    }

}