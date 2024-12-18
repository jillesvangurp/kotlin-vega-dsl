rootProject.name = "kotlin-vega"

pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}

plugins {
    id("de.fayard.refreshVersions") version "0.60.5"
}

refreshVersions {
}
include("kotlin-vega-dsl", "demo")
