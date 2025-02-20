import com.bmuschko.gradle.docker.tasks.container.*
import com.bmuschko.gradle.docker.tasks.image.DockerPullImage
import com.github.dockerjava.api.command.InspectContainerResponse
import com.github.dockerjava.api.model.ExposedPort
import org.jetbrains.kotlin.gradle.targets.native.tasks.KotlinNativeTest
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.concurrent.atomic.AtomicBoolean

plugins {
    id("build-jvm")
    alias(libs.plugins.muschko.remote)
    alias(libs.plugins.liquibase)
}

repositories {
    google()
    mavenCentral()
}

group = rootProject.group
version = rootProject.version

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.withType<JavaCompile> {
    options.release.set(17)
}

dependencies {
    implementation(projects.crowdProjDocsCardsCommon)
    api(projects.crowdProjDocsCardsCommonRepo)
    implementation(libs.coroutines.core)
    implementation(libs.uuid)
    implementation(kotlin("test-common"))
    implementation(kotlin("test-annotations-common"))
    implementation(projects.crowdProjDocsCardsTestsRepo)
    implementation(kotlin("stdlib"))
    implementation(project(":crowd-proj-docs-cards-api-v1-jackson"))
    implementation(project(":crowd-proj-docs-cards-common"))
    implementation(libs.kotest.core)
    implementation(libs.kotest.junit5)
    testImplementation(kotlin("test-junit"))
    testImplementation(libs.testcontainers.core)
    testImplementation(libs.testcontainers.postgres)
    implementation(libs.db.postgres)
    implementation(libs.bundles.exposed)
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    liquibaseRuntime(libs.liquibase.core)
    liquibaseRuntime(libs.liquibase.picocli)
    liquibaseRuntime(libs.liquibase.snakeyml)
    liquibaseRuntime(libs.db.postgres)
}

var pgPort = 5432
val taskGroup = "pgContainer"
val pgDbName = "mk_plc_doc_cards"
val pgUsername = "postgres"
val pgPassword = "marketplace-pass"
val containerStarted = AtomicBoolean(false)


tasks.withType<KotlinCompile>().configureEach {
    compilerOptions.freeCompilerArgs.add("-Xskip-prerelease-check")
}

tasks {
    // Здесь в тасках запускаем PotgreSQL в контейнере
    // Накатываем liquibase миграцию
    // Передаем настройки в среду тестирования
    val postgresImage = "postgres:latest"
    val pullImage by creating(DockerPullImage::class) {
        group = taskGroup
        image.set(postgresImage)
    }

    val dbContainer by creating(DockerCreateContainer::class) {
        group = taskGroup
        dependsOn(pullImage)
        targetImageId(pullImage.image)
        withEnvVar("POSTGRES_PASSWORD", pgPassword)
        withEnvVar("POSTGRES_USER", pgUsername)
        withEnvVar("POSTGRES_DB", pgDbName)
        healthCheck.cmd("pg_isready")
        hostConfig.portBindings.set(listOf("5432:5432"))
        exposePorts("tcp", listOf(5432))
        hostConfig.autoRemove.set(true)
    }

    val stopPg by creating(DockerStopContainer::class) {
        group = taskGroup
        targetContainerId(dbContainer.containerId)
    }
    val startPg by creating(DockerStartContainer::class) {
        group = taskGroup
        dependsOn(dbContainer)
        targetContainerId(dbContainer.containerId)
        finalizedBy(stopPg)
    }
    val inspectPg by creating(DockerInspectContainer::class) {
        group = taskGroup
        dependsOn(startPg)
        finalizedBy(stopPg)
        targetContainerId(dbContainer.containerId)
        onNext(
            object : Action<InspectContainerResponse> {
                override fun execute(container: InspectContainerResponse) {
                    pgPort = container.networkSettings.ports.bindings[ExposedPort.tcp(5432)]
                        ?.first()
                        ?.hostPortSpec
                        ?.toIntOrNull()
                        ?: throw Exception("Postgres port is not found in container")
                }
            }
        )
    }
    val liquibaseUpdate = getByName("update") {
        group = taskGroup
        dependsOn(inspectPg)
        finalizedBy(stopPg)
        doFirst {
            println("waiting for a while ${System.currentTimeMillis() / 1000000}")
            Thread.sleep(30000)
            println("LQB: \"jdbc:postgresql://localhost:$pgPort/$pgDbName\" ${System.currentTimeMillis() / 1000000}")
            liquibase {
                activities {
                    register("main") {
                        arguments = mapOf(
                            "logLevel" to "info",
                            "searchPath" to layout.projectDirectory.dir("migrations").asFile.toString(),
                            "changelogFile" to "changelog-v0.0.1.sql",
                            "url" to "jdbc:postgresql://localhost:$pgPort/$pgDbName",
                            "username" to pgUsername,
                            "password" to pgPassword,
                            "driver" to "org.postgresql.Driver"
                        )
                    }
                }
            }
        }
    }
    val waitPg by creating(DockerWaitContainer::class) {
        group = taskGroup
        dependsOn(inspectPg)
        dependsOn(liquibaseUpdate)
        containerId.set(startPg.containerId)
        finalizedBy(stopPg)
        doFirst {
            println("PORT: $pgPort")
        }
    }
    withType(KotlinNativeTest::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
    withType(Test::class).configureEach {
        dependsOn(liquibaseUpdate)
        finalizedBy(stopPg)
        doFirst {
            environment("postgresPort", pgPort.toString())
        }
    }
}
