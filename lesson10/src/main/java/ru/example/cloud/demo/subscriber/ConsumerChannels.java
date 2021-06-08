package ru.example.cloud.demo.subscriber;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface ConsumerChannels {

    @Input(ConsumerChannelsConstants.CHANNEL_DEMO)
    SubscribableChannel subscribeDemo();
    
}