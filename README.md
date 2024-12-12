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

```kotlin
VegaSpec().apply {
  title("Pie!") {

  }
}
```

Produces this json:

```application/json
{
  "$schema": "https://vega.github.io/schema/vega/v5.json",
  "title": {
    "text": "Pie!"
  }
}
```

## Development status & goals

This is a project that is under construction. I'm using it for a project and adding features as I go. 
I'll update the README once this changes but not looking for a lot of active contributions and I may change this
quite a bit for now.

Even though this supports only the bare minimum of Vega features currently, the main point is that it uses [json-dsl](https://github.com/jillesvangurp/json-dsl), which makes it really easy to add stuff to the Json via raw json string literals, manipulating the underlying map, and using e.g. lists or maps in Kotlin. Proper model classes will be added and expanded over time on a need to have basis. But it's useful even before I do that.


