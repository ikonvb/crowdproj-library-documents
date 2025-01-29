plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "crowdproj-library-documents"

includeBuild("crowd-proj-docs-card-be")
includeBuild("crowd-proj-docs-card-libs")
includeBuild("crowd-proj-plugin")