plugins {
    id("build-jvm")
    alias(libs.plugins.kotlin.kapt)
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation(projects.crowdProjDocsCardsCommon)
    implementation(projects.crowdProjDocsCardsCommonRepo)

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.jdk9)
    implementation(libs.uuid)
    implementation(libs.bundles.cassandra)
    kapt(libs.db.cassandra.kapt)
    testImplementation(projects.crowdProjDocsCardsTestsRepo)
    testImplementation(libs.testcontainers.cassandra)
}