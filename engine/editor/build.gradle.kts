plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
}

kotlin {

    jvm()

    sourceSets {
        commonMain.dependencies {
            listOf(
                projects.engine.render,
                libs.compose.runtime,
                libs.compose.foundation,
                libs.compose.material3,
                libs.compose.ui,
                libs.compose.uiToolingPreview,
                libs.androidx.lifecycle.viewmodelCompose,
                libs.androidx.lifecycle.runtimeCompose
            ).forEach {
                implementation(it)
            }
        }
        jvmMain.dependencies {
            listOf(
                compose.desktop.currentOs,
                libs.kotlinx.coroutinesSwing
            ).forEach {
                implementation(it)
            }
        }
        commonTest.dependencies {
            listOf(
                libs.kotlin.test
            ).forEach {
                implementation(it)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "com.edosoft.vertexengine.editor.MainKt"
    }
}
