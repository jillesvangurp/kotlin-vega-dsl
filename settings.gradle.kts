rootProject.name = "kotlin-echarts"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.60.6"
}

refreshVersions {
}
include("kotlin-echarts-dsl", "demo")
