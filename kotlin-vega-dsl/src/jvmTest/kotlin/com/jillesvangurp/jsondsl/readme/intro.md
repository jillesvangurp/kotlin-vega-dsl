[![CI](https://github.com/jillesvangurp/kotlin-vega-dsl/actions/workflows/pr_master.yaml/badge.svg)](https://github.com/jillesvangurp/kotlin-vega-dsl/actions/workflows/pr_master.yaml)

This project implements a Kotlin DSL for vega and vega-lite that can be used with vega-embed to render vega charts in a kotlin-js browser application. 

The main challenge is that vega-embed expects to be called with a complex javascript object based on it's JSON DSL. Constructing such objects from kotlin requires a bit of convenience.

This library uses my [json-dsl](https://github.com/jillesvangurp/json-dsl) library which is designed exactly for this problem. It uses a kotlin delegates to manipulate a `Map`. So the included vega dsl provides a limited amount of support for vega via model classes. But since you can just work around whatever is missing by manipulating the underlying `Map` it supports pretty much everything vega has to offer. And a lot of what `json-dsl` does makes using bridging bog standard Kotlin types with JSON really easy.

Check the examples below for how this works.

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
