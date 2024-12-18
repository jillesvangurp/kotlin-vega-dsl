package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.JsonDsl
import com.jillesvangurp.jsondsl.PropertyNamingConvention
import com.jillesvangurp.jsondsl.RawJson
import com.jillesvangurp.jsondsl.withJsonDsl

@DslMarker
annotation class VegaDSLMarker

@VegaDSLMarker
class VegaTitle : JsonDsl(namingConvention = PropertyNamingConvention.AsIs) {
    var text by property<String>()
}

@VegaDSLMarker
class Data : JsonDsl(namingConvention = PropertyNamingConvention.AsIs) {
    var name by property<String>()

    fun table(values: List<Number>, labels: List<String>) {
        this["values"] = labels.zip(values)
            .joinToString(",", "[", "]") { (label, value) ->
                """{ "id": "$label", "value": $value }"""
            }.let { RawJson(it) }
    }

    fun pieTransform(valuesField: String = "field") {
        this.getOrCreateMutableList("transform").add(
            withJsonDsl {
                this["type"] = "pie"
                this["field"] = "value"
            },
        )
    }
}

@VegaDSLMarker
class VegaSpec : JsonDsl(namingConvention = PropertyNamingConvention.AsIs) {
    // immutable
    val schema by property("${'$'}schema", "https://vega.github.io/schema/vega/v5.json")

    val data by property<List<Data>>()

    private var title by property<VegaTitle>()

    private var width by property("width", 300)
    private var height by property("height", 300)

    fun title(defaultTitle: String = "", block: (VegaTitle.() -> Unit)? = null) {
        val vt = VegaTitle().also {
            it.text = defaultTitle
        }
        block?.invoke(vt)
        title = vt
    }

    fun table(
        name: String,
        values: List<Number>,
        labels: List<String>,
        block: (Data.() -> Unit)? = null
    ) {
        getOrCreateMutableList("data").add(
            Data().apply {
                this.name = name
                table(values, labels)
                block?.invoke(this)
            },
        )
    }

    fun ordinalColorScale(
        dataSourceName: String = "table",
        labelField: String = "id",
        colorScheme: String = "category20"
    ) {
        this.getOrCreateMutableList("scales").add(
            withJsonDsl {
                this["name"] = "color"
                this["type"] = "ordinal"
                this["domain"] = withJsonDsl {
                    this["data"] = dataSourceName
                    this["field"] = labelField
                }
                this["range"] = withJsonDsl {
                    this["scheme"] = colorScheme
                }
            },
        )
    }

    fun horizontalBarChartScales(
        dataSourceName: String = "table",
        labelField: String = "id",
        valueField: String = "value"
    ) {
        this.getOrCreateMutableList("scales").add(
            RawJson(
                """
{
  "name": "xscale",
  "type": "linear",
  "domain": {
    "data": "$dataSourceName",
    "field": "$valueField"
  },
  "range": "width"
}
            """.trimIndent(),
            ),
        )
        this.getOrCreateMutableList("scales").add(
            RawJson(
                """
{
  "name": "yscale",
  "type": "band",
  "domain": {
    "data": "$dataSourceName",
    "field": "$labelField"
  },
  "range": "height",
  "padding": 0.2
}                
        """.trimIndent(),
            ),
        )
    }

    fun verticalBarChartScales(
        dataSourceName: String = "table",
        labelField: String = "id",
        valueField: String = "value"
    ) {
        this.getOrCreateMutableList("scales").add(
            RawJson(
                """
{
  "name": "xscale",
  "type": "band",
  "domain": {
    "data": "$dataSourceName",
    "field": "$labelField"
  },
  "range": "width",
  "padding": 0.2
}
            """.trimIndent(),
            ),
        )
        this.getOrCreateMutableList("scales").add(
            RawJson(
                """
{
  "name": "yscale",
  "type": "linear",
  "domain": {
    "data": "$dataSourceName",
    "field": "$valueField"
  },
  "range": "height"
}
        """.trimIndent(),
            ),
        )
    }

    fun pieArcMarks(dataSourceName: String, ordinalField: String = "id") {
        this.getOrCreateMutableList("marks").add(
            RawJson(
                """
{
  "type": "arc",
  "from": {"data": "$dataSourceName"},
  "encode": {
    "enter": {
      "fill": {"scale": "color", "field": "id"},
      "x": {"signal": "width / 2"},
      "y": {"signal": "height / 2"},
      "tooltip": {
          "signal": "{'Category': datum.id, 'Value': datum.value}"
        }              
    },
    "update": {
      "startAngle": {"field": "startAngle"},
      "endAngle": {"field": "endAngle"},
      "innerRadius": 0,
      "outerRadius": {"signal": "width / 2"},
      "cornerRadius": 0
    }
  }
}            
        """.trimIndent(),
            ),
        )
    }

    fun horizontalBarChartMarks(
        dataSourceName: String ="table",
        labelField: String = "id",
        valueField: String = "value"
    ) {
        this.getOrCreateMutableList("marks").add(
            RawJson(
                """
{
  "type": "rect",
  "from": {
    "data": "$dataSourceName"
  },
  "encode": {
    "enter": {
      "x": { "value": 0 },
      "x2": { "scale": "xscale", "field": "$valueField" },
      "y": { "scale": "yscale", "field": "$labelField" },
      "height": { "scale": "yscale", "band": 1 },
      "fill": { "scale": "color", "field": "id" },
      "tooltip": { "signal": "{'Category': datum.id, 'Value': datum.value}" }
    }
  }
}            
        """.trimIndent(),
            ),
        )
        this.getOrCreateMutableList("marks").add(
            RawJson(
                """
{
  "type": "text",
  "from": {
    "data": "$dataSourceName"
  },
  "encode": {
    "enter": {
      "x": { "scale": "xscale", "field": "$valueField", "offset": -5 }, 
      "y": {
        "scale": "yscale",
        "field": "$labelField",
        "offset": { "scale": "yscale", "band": 0.5 }
      },
      "fill": { "value": "white" },
      "align": { "value": "right" },
      "baseline": { "value": "middle" },
      "text": { "field": "$valueField" },
      "fontWeight": { "value": "bold" }
    }
  }
}
        """.trimIndent(),
            ),
        )
    }

    fun verticalBarChartMarks(
        dataSourceName: String ="table",
        labelField: String = "id",
        valueField: String = "value"
    ) {
        this.getOrCreateMutableList("marks").add(
            RawJson(
                """
{
  "type": "rect",
  "from": {
    "data": "$dataSourceName"
  },
  "encode": {
    "enter": {
      "x": { "scale": "xscale", "field": "$labelField" },
      "width": { "scale": "xscale", "band": 1 },
      "y": { "value": 0 },      
      "y2": { "scale": "yscale", "field": "$valueField" },
      "fill": { "scale": "color", "field": "id" },
      "tooltip": { "signal": "{'Category': datum.id, 'Value': datum.value}" }
    }
  }
}            
        """.trimIndent(),
            )
        )
//        this.getOrCreateMutableList("marks").add(
//            RawJson(
//                """
//{
//  "type": "text",
//  "from": {
//    "data": "$dataSourceName"
//  },
//  "encode": {
//    "enter": {
//      "x": { "scale": "xscale", "field": "$valueField", "offset": -5 },
//      "y": {
//        "scale": "yscale",
//        "field": "$labelField",
//        "offset": { "scale": "yscale", "band": 0.5 }
//      },
//      "fill": { "value": "white" },
//      "align": { "value": "right" },
//      "baseline": { "value": "middle" },
//      "text": { "field": "$valueField" },
//      "fontWeight": { "value": "bold" }
//    }
//  }
//}
//        """.trimIndent(),
//            ),
//        )
    }

    fun simpleLegend() {
        this.getOrCreateMutableList("legends").add(
            RawJson(
                """
  {
    "fill": "color",
    "orient": "right"
  }            
        """.trimIndent(),
            ),
        )
    }

    // empty companion so we can add extension functions
    companion object {}
}


class VegaConfig : JsonDsl(namingConvention = PropertyNamingConvention.AsIs) {
    var actions by property<Boolean>()

    companion object
}
