plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()
    sourceSets {
        commonMain.dependencies {
            listOf(
                libs.joml
            ).forEach {
                implementation(it)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
