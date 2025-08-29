@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    `maven-publish`
}

repositories {
    mavenCentral()
    maven(url = "https://jitpack.io") {
        content {
            includeGroup("com.github.jillesvangurp")
        }
    }
    maven("https://maven.tryformation.com/releases") {
        // optional but it speeds up the gradle dependency resolution
        content {
            includeGroup("com.jillesvangurp")
        }
    }
}

kotlin {
    js(IR) {
        browser {
            testTask {
                useMocha {
                    // javascript is a lot slower than Java, we hit the default timeout of 2000
                    timeout = "60s"
                }
            }
        }
        binaries.executable()
    }

    sourceSets {

        commonMain {
            dependencies {
                implementation(project(":kotlin-echarts-dsl"))
                implementation(kotlin("stdlib-common"))
            }
        }

        commonTest {
            dependencies {
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
                implementation("io.kotest:kotest-assertions-core:_")
            }
        }

        jsMain  {
            dependencies {
                implementation(kotlin("stdlib-js"))
                implementation("org.jetbrains.kotlinx:kotlinx-html:_")
                // apache echarts
                implementation(npm("echarts", "_"))

            }
        }

        jsTest  {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        all {
            languageSettings {
                languageVersion = "1.9"
                apiVersion = "1.9"
            }
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}





