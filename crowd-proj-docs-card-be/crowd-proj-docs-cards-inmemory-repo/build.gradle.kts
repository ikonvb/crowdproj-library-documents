plugins {
    kotlin("jvm") version "2.0.21"
}

group = "ru.otus.crowd.proj"
version = "0.0.1"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}