package ru.diasoft.digitalq.service;

import java.util.Date;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.diasoft.digitalq.domain.SmsVerification;
import ru.diasoft.digitalq.domain.SmsVerificationCheckRequest;
import ru.diasoft.digitalq.domain.SmsVerificationCheckResponse;
import ru.diasoft.digitalq.domain.SmsVerificationPostRequest;
import ru.diasoft.digitalq.domain.SmsVerificationPostResponse;
import ru.diasoft.digitalq.repository.SmsVerificationRepository;

@RequiredArgsConstructor
@Service
@Primary
@Slf4j
public class SmsVerificationServiceImpl implements SmsVerificationService {

	private final SmsVerificationRepository repository;
	
	@Override
	public SmsVerificationCheckResponse dsSmsVerificationCheck(
			SmsVerificationCheckRequest smsVerificationCheckRequest) {

		Optional<SmsVerification> result = repository.findByGuidAndSecretCodeAndStatus(smsVerificationCheckRequest.getProcessGUID(), smsVerificationCheckRequest.getCode(), "OK");

		log.error("findByGuidAndSecretCodeAndStatus result="+result);
		SmsVerificationCheckResponse response = new SmsVerificationCheckResponse();
		response.setCheckResult(result.isPresent());
		return response;
	}

	@Override
	public SmsVerificationPostResponse dsSmsVerificationCreate(SmsVerificationPostRequest smsVerificationPostRequest) {
		SmsVerification entity = SmsVerification.builder()
				.creationDate(new Date())
				.phoneNumber(smsVerificationPostRequest.getPhoneNumber())
				.guid(UUID.randomUUID().toString())
				.secretCode(String.valueOf(new Random().nextInt(10000)))
				.status("WAITING")
				.build();
		SmsVerification createdEntity = repository.save(entity);
		log.error("createdEntity="+createdEntity);
		
		SmsVerificationPostResponse response = new SmsVerificationPostResponse();
		response.setProcessGUID(createdEntity.getGuid());
		return response;
	}

}
