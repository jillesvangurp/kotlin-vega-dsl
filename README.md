# JsonDsl

[![CI](https://github.com/jillesvangurp/kotlin-echarts-dsl/actions/workflows/pr_master.yaml/badge.svg)](https://github.com/jillesvangurp/kotlin-echarts-dsl/actions/workflows/pr_master.yaml)

This project implements a Kotlin DSL for [Apache Echarts](https://echarts.apache.org/) that can be used to
render charts in a kotlin-js browser application.

The main challenge is that Apache Echarts expects to be called with a complex javascript object based on it's JSON DSL. Constructing such objects from kotlin requires a bit of convenience.

This library uses my [json-dsl](https://github.com/jillesvangurp/json-dsl) library which is designed exactly for this
problem. It uses a kotlin delegates to manipulate a `Map`. So the included DSL provides a limited amount of support for Apache Echarts via model classes. But since you can just work around whatever is missing by manipulating the underlying `Map`
it supports pretty much everything Apache Echarts has to offer. And a lot of what `json-dsl` does makes using bridging bog
standard Kotlin types with JSON really easy.

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
    implementation("com.jillesvangurp:kotlin-echarts-dsl:0.x.y")
```

## Examples

```kotlin
EChartsOption.pie(
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
  "title": {
    "text": "I like Pie!"
  },
  "tooltip": {
    "trigger": "item",
    "formatter": "{c}"
  },
  "legend": {
    "orient": "vertical",
    "top": "top",
    "right": 0,
    "data": [
      "Pie I have Eaten: 1", 
      "Pie I have not eaten: 4"
    ]
  },
  "series": [
    {
      "name": "Access From",
      "type": "pie",
      "radius": "80%",
      "data": [
        {
          "name": "Pie I have Eaten: 1",
          "value": 1
        }, 
        {
          "name": "Pie I have not eaten: 4",
          "value": 4
        }
      ],
      "label": {
        "show": true,
        "position": "inside",
        "formatter": "{c}"
      },
      "labelLine": {
        "show": false
      },
      "avoidLabelOverlap": true,
      "minShowLabelAngle": 10,
      "emphasis": {
        "itemStyle": {
          "shadowBlur": 5,
          "shadowOffsetX": 0,
          "shadowColor": "rgba(0, 0, 0, 0.5)"
        }
      }
    }
  ]
}
```

```kotlin
EChartsOption.horizontalBar(
  listOf(1, 4),
  listOf("Pie I have Eaten", "Pie I have not eaten"),
  title = "I like Pie!",
)
```

Produces this json:

```application/json
{
  "title": {
    "text": "I like Pie!"
  },
  "tooltip": {
    "trigger": "axis",
    "axisPointer": {
      "type": "shadow"
    },
    "formatter": "{b}: {c}"
  },
  "xAxis": {
    "type": "value"
  },
  "yAxis": {
    "type": "category",
    "data": [
      "Pie I have Eaten: 1", 
      "Pie I have not eaten: 4"
    ]
  },
  "legend": {
    "show": false,
    "orient": "vertical",
    "top": "top",
    "right": 0
  },
  "series": [
    {
      "type": "bar",
      "data": [
        1, 
        4
      ],
      "label": {
        "show": true,
        "position": "insideRight",
        "formatter": "{c}"
      },
      "labelLayout": {
        "hideOverlap": true
      },
      "emphasis": {
        "itemStyle": {
          "shadowBlur": 5,
          "shadowOffsetX": 0,
          "shadowColor": "rgba(0, 0, 0, 0.5)"
        }
      }
    }
  ]
}
```

```kotlin
EChartsOption.verticalBarOrLine(
  listOf(1, 4),
  listOf("Pie I have Eaten", "Pie I have not eaten"),
  title = "I like Pie!",
)
```

Produces this json:

```application/json
{
  "title": {
    "text": "I like Pie!"
  },
  "grid": {
    "containLabel": true
  },
  "tooltip": {
    "trigger": "axis",
    "axisPointer": {
      "type": "shadow"
    }
  },
  "xAxis": {
    "type": "category",
    "data": [
      "Pie I have Eaten", 
      "Pie I have not eaten"
    ]
  },
  "yAxis": {
    "type": "value"
  },
  "legend": {
    "show": false
  },
  "series": [
    {
      "type": "bar",
      "data": [
        1, 
        4
      ],
      "label": {
        "show": false
      },
      "emphasis": {
        "focus": "series",
        "label": {
          "show": true,
          "position": "top",
          "formatter": "{c}"
        }
      },
      "labelLayout": {
        "hideOverlap": true
      }
    }
  ]
}
```

## How to embed in kotlin-js

Vega DSL is a multi platform library. But of course it is intended to use with 
vega-embed, which is a kotlin-js library. You could probably get it working as part 
of node.js as well and probably there is a web assembly compatible way of doing this as well.
                
To facilitate embedding in javascript, some minimal type mapping for the embed 
function in vega-embed is bundled. Here's an example from the demo module on how you 
could use that with Kotlin's html DSL in kotlin-js.                                                

```kotlin
fun TagConsumer<*>.addSpec(option: EChartsOption, debug: Boolean = false) {
  val elementId = Random.nextULong().toString()
  try {
    div {
      id = "echart-$elementId"
      style = "width: 400px; height: 400px; border: 1px solid;"
    }
    if (debug) {
      pre {
        +JSON.stringify(option.toJsObject(), null, 2)
      }
    }
    window.requestAnimationFrame {
      val chartElement = document.getElementById("echart-$elementId")!! as HTMLDivElement
      console.log(chartElement)
      val chart = init(chartElement)
      chart.setOption(option.toJsObject())
    }
  } catch (e: Exception) {
    console.error(e)
  }
}

fun main() {
  console.log("HI")
  document.getElementById("target")?.append {
    div { h1 { +"ECharts Spec Demo" } }
    div {
      style = "display:flex; flex-flow: row wrap; gap:12px; align-items:flex-start; margin:0;"
      addSpec(
        EChartsOption.pie(
          values = listOf(1, 5, 30, 300),
          categories = listOf("A", "B", "C", "D"),
          title = "I like pie!",
        ),
      )
      addSpec(
        EChartsOption.pie(
          values = listOf(1, 5, 30, 300),
          categories = listOf("A", "B", "C", "D"),
          title = "... and donuts",
          donut = true,
        ),
      )
      addSpec(
        EChartsOption.horizontalBar(
          values = listOf(1, 2, 3, 4),
          categories = listOf("A", "B", "C", "D"),
          title = "Bar Chars",
        ),
      )
      addSpec(
        EChartsOption.verticalBarOrLine(
          values = listOf(1, 2, 3, 4),
          categories = listOf("A", "B", "C", "D"),
          title = "Vertical Bar",
        ),
      )
      addSpec(
        EChartsOption.verticalBarOrLine(
          values = listOf(4, 2, 1, 3),
          categories = listOf("A", "B", "C", "D"),
          chartType = "line",
          title = "Line Chart",
        ),
      )
      addSpec(
        EChartsOption.verticalBarOrLine(
          values = listOf(6, 2, 3, 1),
          categories = listOf(
            "2024-12-15T00:00:00Z",
            "2024-12-16T00:00:00Z",
            "2024-12-17T00:00:00Z",
            "2024-12-18T00:00:00Z",
          ),
          chartType = "line",
          temporalCategory = true,
          title = "Line Chart with times",
        ),
      )
    }
  }
}
```

## Development status & goals

This is a project that is **under construction**. I'm using it for one of our internal projects and I'm adding features as I go. My intention is certainly not to cover the full feature set of Apache Echarts. This should not be needed with my `json-dsl`.

I'll update the README once this changes. But I'm not looking for a lot of active contributions and I may change this quite a bit for now.

Even though this supports only a bare minimum of Vega features currently, the main point is that it uses [json-dsl](https://github.com/jillesvangurp/json-dsl), which makes it really easy to add stuff to the Json via raw json string literals, manipulating the underlying map, and using e.g. lists or maps in Kotlin. 

Proper model classes may be added and expanded over time on a need to have basis. But it's useful to me even before I do that.


