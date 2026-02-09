plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    jvm()

    sourceSets {
        commonMain.dependencies {
            implementation(projects.engine.core)
        }
        jvmMain.dependencies {
            implementation(libs.joml)
            implementation(libs.jogamp.jogl)
            implementation(libs.jogamp.gluegen)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}
