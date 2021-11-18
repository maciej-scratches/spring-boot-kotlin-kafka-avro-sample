package com.example.springkafkademo2

import com.example.avro.Person
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.argThat
import org.mockito.kotlin.anyOrNull
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.KafkaContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import org.testcontainers.utility.DockerImageName
import java.time.Duration

@SpringBootTest
@Testcontainers
class SpringKafkaDemo2ApplicationTests {

  companion object {
    @Container
    val kafka = KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:6.2.1"))

    @JvmStatic
    @DynamicPropertySource
    fun props(registry: DynamicPropertyRegistry) {
      registry.add("spring.kafka.bootstrap-servers") { kafka.bootstrapServers }
      registry.add("spring.kafka.properties.schema.registry.url") { "mock://" }
      registry.add("spring.kafka.consumer.auto-offset-reset") { "earliest" }
    }
  }

  @Autowired
  lateinit var kafkaTemplate: KafkaTemplate<String, Person>

  @SpyBean
  lateinit var myConsumer: MyConsumer

  @Test
  fun contextLoads() {
    // send message
    println("Sending")
    kafkaTemplate.send("workshop", Person("Josh"))
    println("Sent")
    // verify that listener handled message
    await().atMost(Duration.ofSeconds(1)).untilAsserted {
      verify(myConsumer).listen(anyOrNull())
    }
  }

}
