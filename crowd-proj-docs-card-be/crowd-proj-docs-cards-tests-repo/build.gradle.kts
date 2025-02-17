plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(kotlin("test-common"))
                api(kotlin("test-annotations-common"))

                api(libs.coroutines.core)
                api(libs.coroutines.test)
                implementation(projects.crowdProjDocsCardsCommon)
                implementation(projects.crowdProjDocsCardsCommonRepo)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.crowdProjDocsCardsStubs)
            }
        }
        jvmMain {
            dependencies {
                api(kotlin("test-junit"))
            }
        }
    }
}