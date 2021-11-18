package com.example.springkafkademo2

import com.example.avro.Person
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
class SpringKafkaDemo2Application

fun main(args: Array<String>) {
  runApplication<SpringKafkaDemo2Application>(*args)
}

@RestController
class HelloController(val kafkaTemplate: KafkaTemplate<String, Person>) {

  @GetMapping("/hello")
  fun hello() {
    kafkaTemplate.send("workshop", Person("hello"))
  }
}

@Component
class MyConsumer {

  @KafkaListener(topics = ["workshop"])
  fun listen(record: Person) {
    println(record.name)
  }
}