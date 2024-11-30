import java.net.URI

plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
}

group = "com.paranid5.mq_to_email"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven { url = URI("https://jitpack.io") }
}

dependencies {
    implementation("com.rabbitmq:amqp-client:5.23.0")
    implementation("com.sun.mail:javax.mail:1.6.2")
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.2")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}