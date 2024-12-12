# JsonDsl

This project implements a Kotlin DSL for Vega that can be used with vega-embed to render vega charts in a kotlin-js browser application.

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
    implementation("com.jillesvangurp:kotlin-vega-dsl:1.x.y")
```

## Example

The main feature of [kotlin4example](https://github.com/jillesvangurp/kotlin4example) is of course integrating code samples into your documentation.   

### Hello World

```kotlin
println("Hello World!") 
```

And you can actually grab the output and show it in another code block:

```text
Hello World!
```

## Development status

This is a project that is under construction. I'm using it for a project and adding features as I go. 
I'll update the README once this changes but not looking for a lot of active contributions and I may change this
quite a bit for now.

