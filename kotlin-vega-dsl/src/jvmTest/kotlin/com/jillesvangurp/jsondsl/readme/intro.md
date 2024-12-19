This project implements a Kotlin DSL for vega and vega-lite that can be used with vega-embed to render vega charts in a kotlin-js browser application. The main challenge is that vega-embed expects to be called with a complex javascript object based on it's JSON DSL. Constructing such objects from kotlin requires a bit of convenience. This library uses my json-dsl library to doing that easier. 

## Gradle

This library is published to our own maven repository.

```kotlin
repositories {
    mavenCentral()
    maven("https://maven.tryformation.com/releases") {
        // optional but it speeds up the gradle dependency resolution
        content {
            includeGroup("com.jillesvangurp")
            includeGroup("com.tryformation")
        }
    }
}
```

And then you can add the dependency:

```kotlin
    // check the latest release tag for the latest version
    implementation("com.jillesvangurp:kotlin-vega-dsl:0.x.y")
```
