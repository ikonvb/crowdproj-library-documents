plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":crowd-proj-docs-cards-api-v1-jackson"))
    implementation(project(":crowd-proj-docs-cards-common"))
    implementation(libs.kotest.core)
    implementation(libs.kotest.junit5)
    testImplementation(kotlin("test-junit"))
}

tasks.test {
    useJUnitPlatform()
}
