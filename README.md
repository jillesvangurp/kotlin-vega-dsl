# JsonDsl

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

## Examples

```kotlin
VegaLiteSpec.pie(
  listOf(1, 4),
  listOf(
    "Pie I have Eaten",
    "Pie I have not eaten",
  ),
  title = "I like Pie!",
)
```

Produces this json:

```application/json
{
  "$": "https://vega.github.io/schema/vega-lite/v5.json",
  "width": "container",
  "height": "container",
  "title": "I like Pie!",
  "mark": {
    "type": "arc",
    "tooltip": true
  },
  "data": {
    "values": [
      {
        "category": "Pie I have Eaten",
        "value": 1
      }, 
      {
        "category": "Pie I have not eaten",
        "value": 4
      }
    ]
  },
  "encoding": {
    "theta": {"field": "value", "type": "quantitative", "stack": true},
    "color": {"field": "category", "type": "nominal", "legend":{"title":null}}
}
}
```

```kotlin
VegaLiteSpec.horizontalBar(
  listOf(1, 4),
  listOf("Pie I have Eaten", "Pie I have not eaten"),
  title = "I like Pie!",
)
```

Produces this json:

```application/json
{
  "$": "https://vega.github.io/schema/vega-lite/v5.json",
  "width": "container",
  "height": "container",
  "title": "I like Pie!",
  "mark": {
    "type": "bar",
    "tooltip": true
  },
  "data": {
    "values": [
      {
        "category": "Pie I have Eaten",
        "value": 1
      }, 
      {
        "category": "Pie I have not eaten",
        "value": 4
      }
    ]
  },
  "encoding": {
    "x": {"field": "value", "type": "quantitative"},
    "y": {"field": "category", "type": "nominal"},
    "color": {"field": "category", "type": "nominal", "legend":{"title":null}}
}
}
```

```kotlin
VegaLiteSpec.verticalBar(
  listOf(1, 4),
  listOf("Pie I have Eaten", "Pie I have not eaten"),
  title = "I like Pie!",
)
```

Produces this json:

```application/json
{
  "$": "https://vega.github.io/schema/vega-lite/v5.json",
  "width": "container",
  "height": "container",
  "title": "I like Pie!",
  "mark": {
    "type": "bar",
    "tooltip": true
  },
  "data": {
    "values": [
      {
        "category": "Pie I have Eaten",
        "value": 1
      }, 
      {
        "category": "Pie I have not eaten",
        "value": 4
      }
    ]
  },
  "encoding": {
    "x": {"field": "category", "type": "nominal"},
    "y": {"field": "value", "type": "quantitative"},
    "color": {"field": "category", "type": "nominal", "legend":{"title":null}}
}
}
```

## How to embed in kotlin-js

Vega DSL is a multi platform library. But of course it is intended to use with 
vega-embed, which is a kotlin-js library. You could probably get it working as part 
of node.js as well and probably there is a web assembly compatible way of doing this as well.
                
To facilitate embedding in javascript, some minimal type mapping for the embed function in vega-embed is bundled.                               

```kotlin
fun TagConsumer<*>.addSpec(spec: VegaEmbeddable) {
  val elementId = Random.nextULong().toString()
  div {
    id = "vegademo-$elementId"
    style = "width: 400px; height: 400px; border: 1px solid;"
  }
  pre {
    id = "preblock-$elementId"
  }
  val vegaElement = document.getElementById("vegademo-$elementId")!! as HTMLElement

  embed(
    vegaElement, spec.toJsObject(),
    VegaConfig.config {
      actions = false
    }.toJsObject(),
  )

  val preEl = document.getElementById("preblock-$elementId")!! as HTMLPreElement
  preEl.append(JSON.stringify(spec.toJsObject(), null, 2))
}

fun main() {
  console.log("HI")
  document.getElementById("target")?.append {
    div {
      h1 {
        +"Vega Spec Demo"
      }
    }
    addSpec(
      VegaLiteSpec.pie(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D"))
    )
    addSpec(
      VegaLiteSpec.horizontalBar(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D"))
    )
    addSpec(
      VegaLiteSpec.verticalBar(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D"))
    )

    addVega {
      title("Horizontal Bar Chart")
      table(
        "pieslices",
        listOf(4.0, 1.0),
        listOf("Pie I have not Eaten", "Pie I have Eaten"),
      ) {
      }
      ordinalColorScale("pieslices")
      horizontalBarChartScales(dataSourceName = "pieslices")
      horizontalBarChartMarks(dataSourceName = "pieslices")
      simpleLegend()
    }

    addVega {
      title("Pie Chart")
      table(
        "pieslices",
        listOf(4.0, 1.0),
        listOf("Pie I have not Eaten", "Pie I have Eaten"),
      ) {
        pieTransform()
      }
      ordinalColorScale("pieslices")
      pieArcMarks("pieslices")
      simpleLegend()
    }
  }
```

## Development status & goals

This is a project that is **under construction**. I'm using it for a project and adding features as I go and my intention is certainly not to cover the full feature set of vega.

I'll update the README once this changes but I'm not looking for a lot of active contributions and I may change this quite a bit for now.

Even though this supports only the bare minimum of Vega features currently, the main point is that it uses [json-dsl](https://github.com/jillesvangurp/json-dsl), which makes it really easy to add stuff to the Json via raw json string literals, manipulating the underlying map, and using e.g. lists or maps in Kotlin. Proper model classes may be added and expanded over time on a need to have basis. But it's useful to me even before I do that.


