rootProject.name = "crowd-proj-docs-card-be"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../crowd-proj-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":crowd-proj-docs-cards-biz")
include(":crowd-proj-docs-cards-stubs")
include(":crowd-proj-docs-cards-common")
include(":crowd-proj-docs-cards-app-ktor")
include(":crowd-proj-docs-cards-api-logV1")
include(":crowd-proj-docs-cards-app-kafka")
include(":crowd-proj-docs-cards-app-common")
include(":crowd-proj-docs-cards-api-v2-kmp")
include(":crowd-proj-docs-cards-api-v1-mappers")
include(":crowd-proj-docs-cards-api-v1-jackson")
include(":crowd-proj-docs-cards-inmemory-repo")
include(":crowd-proj-docs-cards-common-repo")
include(":crowd-proj-docs-cards-tests-repo")
include(":crowd-proj-docs-cards-stubs-repo")
include(":crowd-proj-docs-cards-pgsql-repo")