
plugins {
    val kotlinVersion = "2.0.21"
    application
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.serialization") version kotlinVersion
    id("io.ktor.plugin") version "3.0.1"
//    id("com.github.johnrengelman.shadow") version "8.1.1"
}

group = "simonvoid.gmx.de"
version = "0.0.1"
application {
    mainClass.set("simonvoid.gmx.de.time_formatter.ApplicationKt")
}

kotlin {
    // uses org.gradle.java.installations.auto-download=false in gradle.properties to disable auto provisioning of JDK
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core-jvm")
    implementation("io.ktor:ktor-server-content-negotiation-jvm")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm")
    implementation("io.ktor:ktor-server-netty-jvm")
    implementation("ch.qos.logback:logback-classic:1.5.12")

    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.11.3")
}

tasks {
//    "build" {
//        dependsOn(shadowJar)
//    }

    test {
        useJUnitPlatform()
    }
}
