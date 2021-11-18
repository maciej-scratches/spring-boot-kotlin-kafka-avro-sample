package com.example.springkafkademo2

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.SeekToCurrentErrorHandler
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaConfiguration {

  @Bean
  fun errorHandler(recoverer: DeadLetterPublishingRecoverer)
  = SeekToCurrentErrorHandler(recoverer, FixedBackOff(1000, 3))

  @Bean
  fun deadLetterPublishingRecoverer(kafkaTemplate: KafkaTemplate<*,*>)
  = DeadLetterPublishingRecoverer(kafkaTemplate)
}