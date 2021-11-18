import com.github.davidmc24.gradle.plugin.avro.GenerateAvroJavaTask
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  id("org.springframework.boot") version "2.5.6"
  id("io.spring.dependency-management") version "1.0.11.RELEASE"
  kotlin("jvm") version "1.5.31"
  kotlin("plugin.spring") version "1.5.31"
  id("com.github.davidmc24.gradle.plugin.avro") version "1.3.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11

repositories {
  mavenCentral()
  maven {
    url = uri("https://packages.confluent.io/maven/")
  }
}

tasks.named("generateTestAvroJava") {
  this.enabled = false
}

dependencies {
  implementation("io.confluent:kafka-avro-serializer:7.0.0")
  implementation("org.apache.avro:avro:1.11.0") // otherwise, generated sources won't compile
  implementation("org.springframework.boot:spring-boot-starter-web")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
  implementation("org.jetbrains.kotlin:kotlin-reflect")
  implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
  implementation("org.springframework.kafka:spring-kafka")
  testImplementation("org.springframework.boot:spring-boot-starter-test")
  testImplementation("org.springframework.kafka:spring-kafka-test")
  testImplementation("org.testcontainers:junit-jupiter:1.16.2")
  testImplementation("org.testcontainers:kafka:1.16.2")
  testImplementation("org.mockito.kotlin:mockito-kotlin:4.0.0")
  testImplementation("org.awaitility:awaitility:4.1.1")
}

tasks.withType<GenerateAvroJavaTask> {
  source("src/main/resources/avro")
}

tasks.withType<KotlinCompile> {
  kotlinOptions {
    freeCompilerArgs = listOf("-Xjsr305=strict")
    jvmTarget = "11"
  }
  dependsOn("generateAvroJava")
}

tasks.withType<Test> {
  useJUnitPlatform()
}
