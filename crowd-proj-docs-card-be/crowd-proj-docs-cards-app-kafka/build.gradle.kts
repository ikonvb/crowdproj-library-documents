plugins {
    application
    id("build-jvm")
    alias(libs.plugins.muschko.java)
}

application {
    mainClass.set("ru.otus.crowd.proj.docs.cards.app.kafka.MainKt")
}

docker {
    javaApplication {
        //baseImage.set("openjdk:21-slim")
        baseImage.set("openjdk:17.0.2-slim")
    }
}

dependencies {

    implementation(libs.kafka.client)
    implementation(libs.coroutines.core)
    implementation(libs.kotlinx.atomicfu)

    implementation("ru.otus.crowd.proj.docs.cards.libs:crowd-proj-docs-card-lib-logging-logback")
    implementation(project(":crowd-proj-docs-cards-app-common"))

    // transport models
    implementation(project(":crowd-proj-docs-cards-common"))
    implementation(project(":crowd-proj-docs-cards-api-v1-jackson"))
    implementation(project(":crowd-proj-docs-cards-api-v1-mappers"))
    implementation(project(":crowd-proj-docs-cards-api-v2-kmp"))
    // logic
    implementation(project(":crowd-proj-docs-cards-biz"))

    testImplementation(kotlin("test-junit"))
}