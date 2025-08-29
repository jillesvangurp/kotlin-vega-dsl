@file:OptIn(ExperimentalWasmDsl::class)

import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent
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
            includeGroup("com.github.jillesvangurp")
        }
    }
}

kotlin {
    jvm {
        // should work for android as well
        compilerOptions {
            jvmTarget = JvmTarget.JVM_21
        }
    }
    js(IR) {
        nodejs {
            testTask {
                useMocha {
                    // javascript is a lot slower than Java, we hit the default timeout of 2000
                    timeout = "60s"
                }
            }
        }
    }
    wasmJs {
        browser()
    }
    // blocked on kotest assertions wasm release
//    wasmWasi()

    sourceSets {

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))
                api("com.jillesvangurp:json-dsl:_")
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

        jvmMain  {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        jvmTest {
            dependencies {
                implementation("org.junit.jupiter:junit-jupiter:_")
                runtimeOnly("org.junit.platform:junit-platform-launcher")
            }
        }

        jsMain  {
            dependencies {
                implementation(kotlin("stdlib-js"))
                // apache echarts for javascript rendering
                implementation(npm("echarts", "_"))
            }
        }

        jsTest  {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        wasmJsTest {
            dependencies {
                implementation(kotlin("test-wasm-js"))
            }
        }

        all {
            languageSettings {
                languageVersion = "2.2"
                apiVersion = "2.2"
            }
            languageSettings.optIn("kotlin.RequiresOptIn")
        }
    }
}

publishing {
    publications {
        withType<MavenPublication> {
            pom {
                url.set("https://github.com/jillesvangurp/kotlin-vega-dsl")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/jillesvangurp/kotlin-vega-dsl/blob/master/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("jillesvangurp")
                        name.set("Jilles van Gurp")
                        email.set("jilles@no-reply.github.com")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/jillesvangurp/kotlin-vega-dsl.git")
                    developerConnection.set("scm:git:ssh://github.com:jillesvangurp/kotlin-vega-dsl.git")
                    url.set("https://github.com/jillesvangurp/kotlin-vega-dsl")
                }
            }
        }
    }

    repositories {
        maven {
            // GOOGLE_APPLICATION_CREDENTIALS env var must be set for this to work
            // public repository is at https://maven.tryformation.com/releases
            url = uri("gcs://mvn-public-tryformation/releases")
            name = "FormationPublic"
        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
    // run tests in parallel
    systemProperties["junit.jupiter.execution.parallel.enabled"] = "true"
    // executes test classes concurrently
    systemProperties["junit.jupiter.execution.parallel.mode.default"] = "concurrent"
    // executes tests inside a class concurrently
    systemProperties["junit.jupiter.execution.parallel.mode.classes.default"] = "concurrent"
    systemProperties["junit.jupiter.execution.parallel.config.strategy"] = "dynamic"
    // random order of test class execution
    systemProperties["junit.jupiter.testclass.order.default"] = "org.junit.jupiter.api.ClassOrderer\$Random"

    testLogging.exceptionFormat = TestExceptionFormat.FULL
    testLogging.events = setOf(
        TestLogEvent.FAILED,
        TestLogEvent.PASSED,
        TestLogEvent.SKIPPED,
        TestLogEvent.STANDARD_ERROR,
        TestLogEvent.STANDARD_OUT
    )
    addTestListener(object : TestListener {
        val failures = mutableListOf<String>()
        override fun beforeSuite(desc: TestDescriptor) {
        }

        override fun afterSuite(desc: TestDescriptor, result: TestResult) {
        }

        override fun beforeTest(desc: TestDescriptor) {
        }

        override fun afterTest(desc: TestDescriptor, result: TestResult) {
            if (result.resultType == TestResult.ResultType.FAILURE) {
                val report =
                    """
                    TESTFAILURE ${desc.className} - ${desc.name}
                    ${
                        result.exception?.let { e ->
                            """
                            ${e::class.simpleName} ${e.message}
                        """.trimIndent()
                        }
                    }
                    -----------------
                    """.trimIndent()
                failures.add(report)
            }
        }
    })
}



