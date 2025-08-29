# JsonDsl

This project implements a Kotlin DSL for [Apache ECharts](https://echarts.apache.org) that can be used to render charts in a Kotlin/JS browser application.

The DSL is built on top of the [json-dsl](https://github.com/jillesvangurp/json-dsl) library and makes constructing the complex ECharts option objects convenient from Kotlin.

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

```json
{
  "title": { "text": "I like Pie!" },
  "tooltip": { "trigger": "item" },
  "legend": { "data": ["Pie I have Eaten", "Pie I have not eaten"] },
  "series": [
    {
      "type": "pie",
      "radius": "70%",
      "data": [
        { "value": 1, "name": "Pie I have Eaten" },
        { "value": 4, "name": "Pie I have not eaten" }
      ]
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

```json
{
  "title": { "text": "I like Pie!" },
  "tooltip": {},
  "xAxis": { "type": "value" },
  "yAxis": { "type": "category", "data": ["Pie I have Eaten", "Pie I have not eaten"] },
  "legend": { "show": false },
  "series": [ { "type": "bar", "data": [1, 4] } ]
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

```json
{
  "title": { "text": "I like Pie!" },
  "tooltip": {},
  "xAxis": { "type": "category", "data": ["Pie I have Eaten", "Pie I have not eaten"] },
  "yAxis": { "type": "value" },
  "legend": { "show": true },
  "series": [ { "type": "bar", "data": [1, 4] } ]
}
```

## How to embed in kotlin-js

The ECharts DSL is a multi platform library. To render the charts in the browser, include the `echarts` npm package and initialize it with a DOM element.

```kotlin
// from demo module
fun TagConsumer<*>.addSpec(option: EChartsOption) {
    val elementId = Random.nextULong().toString()
    div {
        id = "echart-$elementId"
        style = "width: 400px; height: 400px; border: 1px solid;"
    }
    val chartElement = document.getElementById("echart-$elementId")!! as HTMLElement
    val chart = echarts.init(chartElement)
    chart.setOption(option.toJsObject())
}
```

## Development status & goals

This is a project that is **under construction**. I'm using it for one of our internal projects and I'm adding features as I go. My intention is certainly not to cover the full feature set of ECharts. This should not be needed with my `json-dsl`.

Even though this supports only a bare minimum of ECharts features currently, the main point is that it uses [json-dsl](https://github.com/jillesvangurp/json-dsl), which makes it really easy to add stuff to the Json via raw json string literals, manipulating the underlying map, and using e.g. lists or maps in Kotlin.

