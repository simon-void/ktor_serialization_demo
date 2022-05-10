
plugins {
    application
    kotlin("jvm") version Deps.kotlinVersion
    id("org.jetbrains.kotlin.plugin.serialization") version Deps.kotlinVersion
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "simonvoid.gmx.de"
version = "0.0.1"
application {
    mainClass.set("simonvoid.gmx.de.time_formatter.ApplicationKt")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec).languageVersion.set(JavaLanguageVersion.of(17))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    val ktorVersion = "2.0.1"

    implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-content-negotiation-jvm:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:$ktorVersion")
    implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:1.2.11")

    testImplementation("io.ktor:ktor-server-tests-jvm:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5:${Deps.kotlinVersion}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
}

tasks {
    "build" {
        dependsOn(shadowJar)
    }

    test {
        useJUnitPlatform()
    }
}
