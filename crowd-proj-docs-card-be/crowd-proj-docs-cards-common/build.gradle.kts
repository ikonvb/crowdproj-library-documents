plugins {
    id("build-kmp")
}

group = rootProject.group
version = rootProject.version

kotlin {

    sourceSets {

        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api(libs.kotlinx.datetime)
                api("ru.otus.crowd.proj.docs.cards.libs:crowd-proj-docs-card-lib-logging-common")
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(libs.kotest.junit5)
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.junit5)
            }
        }

        val linuxX64Main by getting {
            dependencies {
                implementation(kotlin("stdlib"))
                implementation(kotlin("test")) {
                    exclude("io.kotest")
                }
            }
        }

        val linuxX64Test by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
