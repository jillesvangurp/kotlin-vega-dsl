import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

plugins {
    // multiplatform can only be loaded once so load it here with apply false
    kotlin("multiplatform") apply false
}

allprojects {
    // needed because kotlin-js insists on 22.0.0 by default
    plugins.withType<NodeJsPlugin> {
        // keep this version up to date with whatever the latest lts is
        the<NodeJsEnvSpec>().version = "22.12.0"
        // the<NodeJsEnvSpec>().download = false
    }
}
