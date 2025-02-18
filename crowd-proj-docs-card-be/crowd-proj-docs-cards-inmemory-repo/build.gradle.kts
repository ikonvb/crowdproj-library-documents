plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val commonMain by getting {

            dependencies {
                api(projects.crowdProjDocsCardsCommonRepo)
                implementation(projects.crowdProjDocsCardsCommon)
                implementation(libs.coroutines.core)
                implementation(libs.db.cache4k)
                implementation(libs.uuid)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation(projects.crowdProjDocsCardsTestsRepo)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}