import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import io.ktor.plugin.features.*
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

plugins {
    id("build-kmp")
    alias(libs.plugins.ktor)
    alias(libs.plugins.muschko.remote)
    alias(libs.plugins.kotlinx.serialization)
}

application {
    mainClass.set("io.ktor.server.cio.EngineMain")
}

ktor {
    configureNativeImage(project)
    docker {
        localImageName.set(project.name)
        imageTag.set(project.version.toString())
        jreVersion.set(JavaVersion.VERSION_21)
    }
}

jib {
    container.mainClass = application.mainClass.get()
}


kotlin {

    jvm {
        withJava()
    }

    targets.withType<KotlinNativeTarget> {
        binaries {
            executable {
                entryPoint = "ru.otus.crowd.proj.docs.cards.app.ktor.main"
            }
        }
    }

    sourceSets {

        val commonMain by getting {

            dependencies {

                implementation(kotlin("stdlib-common"))
                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.cio)
                implementation(libs.ktor.server.cors)
                implementation(libs.ktor.server.yaml)
                implementation(libs.ktor.server.negotiation)
                implementation(libs.ktor.server.headers.response)
                implementation(libs.ktor.server.headers.caching)
                implementation(project(":crowd-proj-docs-cards-common"))
                implementation(project(":crowd-proj-docs-cards-app-common"))
                implementation(project(":crowd-proj-docs-cards-biz"))
                implementation(project(":crowd-proj-docs-cards-api-v2-kmp"))
                implementation(project(":crowd-proj-docs-cards-stubs"))
                implementation(libs.kotlinx.serialization.core)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.serialization.json)
                implementation(project(":crowd-proj-docs-cards-api-logV1"))
                implementation("ru.otus.crowd.proj.docs.cards.libs:crowd-proj-docs-card-lib-logging-common")
                implementation("ru.otus.crowd.proj.docs.cards.libs:crowd-proj-docs-card-lib-logging-kermit")
            }
        }

        val commonTest by getting {

            dependencies {

                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(libs.ktor.server.test)
                implementation(libs.ktor.client.negotiation)
            }
        }

        val jvmMain by getting {

            dependencies {

                implementation(kotlin("stdlib-jdk8"))
                implementation(libs.ktor.serialization.jackson)
                implementation(libs.ktor.server.calllogging)
                implementation(libs.ktor.server.headers.default)
                implementation(libs.logback)
                implementation(project(":crowd-proj-docs-cards-api-v1-jackson"))
                implementation(project(":crowd-proj-docs-cards-api-v1-mappers"))
                implementation("ru.otus.crowd.proj.docs.cards.libs:crowd-proj-docs-card-lib-logging-logback")
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }


//        val nativeMain by creating {
//            dependencies {
//                //implementation(libs.kotest.native)
//            }
//        }
//
//        val nativeTest by creating {
//            dependsOn(nativeMain)
//            dependencies {
//                implementation(kotlin("test"))
//                implementation(libs.kotest.native)
//            }
//        }
//
//        val linuxMain by getting {
//            dependsOn(nativeMain)
//        }
//        val macosMain by getting {
//            dependsOn(nativeMain)
//        }
//        val linuxTest by getting {
//            dependsOn(nativeTest)
//        }
//        val macosTest by getting {
//            dependsOn(nativeTest)
//        }
    }
}

tasks {
    shadowJar {
        isZip64 = true
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    // Если ошибка: "Entry application.yaml is a duplicate but no duplicate handling strategy has been set."
    // Возникает из-за наличия файлов как в common, так и в jvm платформе
//    withType(ProcessResources::class) {
//        duplicatesStrategy = DuplicatesStrategy.INCLUDE
//    }

    val linkReleaseExecutableLinuxX64 by getting(KotlinNativeLink::class)
    val nativeFileX64 = linkReleaseExecutableLinuxX64.binary.outputFile
    val linuxX64ProcessResources by getting(ProcessResources::class)

    val dockerDockerfileX64 by creating(Dockerfile::class) {
        dependsOn(linkReleaseExecutableLinuxX64)
        dependsOn(linuxX64ProcessResources)
        group = "docker"
        from(Dockerfile.From("ubuntu:22.04").withPlatform("linux/amd64"))
        doFirst {
            copy {
                from(nativeFileX64)
                from(linuxX64ProcessResources.destinationDir)
                into("${this@creating.destDir.get()}")
            }
        }
        copyFile(nativeFileX64.name, "/app/")
        copyFile("application.yaml", "/app/")
        exposePort(8080)
        workingDir("/app")
        entryPoint("/app/${nativeFileX64.name}", "-config=./application.yaml")
    }
    val registryUser: String? = System.getenv("CONTAINER_REGISTRY_USER")
    val registryPass: String? = System.getenv("CONTAINER_REGISTRY_PASS")
    val registryHost: String? = System.getenv("CONTAINER_REGISTRY_HOST")
    val registryPref: String? = System.getenv("CONTAINER_REGISTRY_PREF")
    val imageName = registryPref?.let { "$it/${project.name}" } ?: project.name

    val dockerBuildX64Image by creating(DockerBuildImage::class) {
        group = "docker"
        dependsOn(dockerDockerfileX64)
        images.add("$imageName-x64:${rootProject.version}")
        images.add("$imageName-x64:latest")
        platform.set("linux/amd64")
    }
    val dockerPushX64Image by creating(DockerPushImage::class) {
        group = "docker"
        dependsOn(dockerBuildX64Image)
        images.set(dockerBuildX64Image.images)
        registryCredentials {
            username.set(registryUser)
            password.set(registryPass)
            url.set("https://$registryHost/v1/")
        }
    }
}