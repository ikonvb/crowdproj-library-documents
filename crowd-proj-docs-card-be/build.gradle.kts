plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.muschko.remote) apply false
    alias(libs.plugins.muschko.java) apply false
    alias(libs.plugins.kotlin.kapt) apply false
}

group = "ru.otus.crowd.proj.docs.be"
version = "0.0.1"

allprojects {

    repositories {
        mavenCentral()
    }

    tasks.withType<JavaCompile> {
        sourceCompatibility = "17"
        targetCompatibility = "17"
    }
}

subprojects {
    group = rootProject.group
    version = rootProject.version
}

ext {
    val specDir = layout.projectDirectory.dir("../specs")
    set("spec-v1", specDir.file("specs-docs-card-v1.yaml").toString())
    set("spec-v2", specDir.file("specs-docs-card-v2.yaml").toString())
    set("spec-logV1", specDir.file("specs-docs-card-v1-log.yaml").toString())
}

tasks {
    arrayOf("build", "clean", "check").forEach { tsk ->
        create(tsk) {
            group = "build"
            dependsOn(subprojects.map { it.getTasksByName(tsk, false) })
        }
    }

    create("buildImages") {
        dependsOn(project("crowd-proj-docs-cards-app-ktor").tasks.getByName("publishImageToLocalRegistry"))
        dependsOn(project("crowd-proj-docs-cards-app-ktor").tasks.getByName("dockerBuildX64Image"))
    }
}