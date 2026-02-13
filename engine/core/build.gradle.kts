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
                api(it)
            }
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
