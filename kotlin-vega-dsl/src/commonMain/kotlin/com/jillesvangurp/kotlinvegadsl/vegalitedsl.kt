package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.JsonDsl
import com.jillesvangurp.jsondsl.PropertyNamingConvention
import com.jillesvangurp.jsondsl.RawJson
import com.jillesvangurp.jsondsl.withJsonDsl

@DslMarker
annotation class VegaLiteDSLMarker

@VegaLiteDSLMarker
class VegaLiteSpec : JsonDsl(namingConvention = PropertyNamingConvention.AsIs), VegaEmbeddable {
    val schema by property("${'$'}", "https://vega.github.io/schema/vega-lite/v5.json")

    // set dimensions from the outside on the embedding container
    val width by property("width","container")
    val height by property("height","container")

    fun mark(type: String, tooltip: Boolean = true, block: (JsonDsl.() -> Unit)?=null) {
        this["mark"] = withJsonDsl {
            this["type"] = type
            this["tooltip"] = tooltip
            block?.invoke(this)
        }
    }

    fun data(values: List<Number>, labels: List<String>) {
        this["data"] = withJsonDsl {
            this["values"] = labels.zip(values).map { (label, value) ->
                withJsonDsl {
                    this["category"] = label
                    this["value"] = value
                }
            }
        }
    }


    companion object {
        fun pie(values: List<Number>, categories: List<String>, donut: Boolean = false,title: String?=null): VegaLiteSpec {
            return VegaLiteSpec().apply {
                title?.let {
                    this["title"] = title
                }
                mark("arc") {
                    if(donut) {
                        this["innerRadius"] = 50
                    }
                }
                data(values, categories)
                this["width"]="container"
                this["height"]="container"
                this["encoding"] = RawJson(
                    """
{
    "theta": {"field": "value", "type": "quantitative", "stack": true},
    "color": {"field": "category", "type": "nominal", "legend":{"title":null}}
}
""".trimIndent(),
                )
            }
        }

        fun horizontalBar(values: List<Number>, categories: List<String>, title: String? = null): VegaLiteSpec {
            return VegaLiteSpec().apply {
                title?.let {
                    this["title"] = title
                }
                mark("bar")
                data(values, categories)
                this["encoding"] = RawJson(
                    """
{
    "x": {"field": "value", "type": "quantitative"},
    "y": {"field": "category", "type": "nominal"},
    "color": {"field": "category", "type": "nominal", "legend":{"title":null}}
}
""".trimIndent()
                )
            }
        }

        fun verticalBar(values: List<Number>, categories: List<String>, title: String? = null): VegaLiteSpec {
            return VegaLiteSpec().apply {
                title?.let {
                    this["title"] = title
                }
                mark("bar")
                data(values, categories)
                this["encoding"] = RawJson(
                    """
{
    "x": {"field": "category", "type": "nominal"},
    "y": {"field": "value", "type": "quantitative"},
    "color": {"field": "category", "type": "nominal", "legend":{"title":null}}
}
""".trimIndent()
                )
            }
        }
    }
}
