plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation(libs.cor)
                implementation(project(":crowd-proj-docs-cards-common"))
                implementation(project(":crowd-proj-docs-cards-stubs"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                api(libs.coroutines.test)
            }
        }
        jvmMain {
            dependencies {
                implementation(libs.cor)
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        jvmTest {
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}