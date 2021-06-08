package ru.example.cloud.demo.subscriber;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class MessageListener {

    @StreamListener(ConsumerChannelsConstants.CHANNEL_DEMO)
    public void listenerDemo(String message) {
    	log.error("listenerDemo started");
    	log.error("listenerDemo: {}", message);

    }
    
}
