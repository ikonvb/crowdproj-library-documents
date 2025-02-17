plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
}

group = "ru.otus.crowd.proj"
version = "0.0.1"

repositories {
    mavenCentral()
}

allprojects {
    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

subprojects {
    repositories {
        mavenCentral()
    }
    group = rootProject.group
    version = rootProject.version
}



tasks {

    create("clean") {
        group = "build"
        gradle.includedBuilds.forEach {
            dependsOn(it.task(":clean"))
        }
    }

    val buildImages: Task by creating {
        dependsOn(gradle.includedBuild("crowd-proj-docs-card-be").task(":buildImages"))
    }

}