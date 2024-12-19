package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.JsonDsl
import com.jillesvangurp.jsondsl.PropertyNamingConvention
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

    fun categoryAndValueData(values: List<Number>, categories: List<String>) {
        this["data"] = withJsonDsl {
            this["values"] = categories.zip(values).map { (label, value) ->
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
                categoryAndValueData(values, categories)

                this["encoding"] = withJsonDsl {
                    this["theta"] = withJsonDsl {
                        this["field"] = "value"
                        this["type"] = "quantitative"
                        this["stack"] = true

                    }
                    this["color"] = withJsonDsl {
                        this["field"] = "category"
                        this["type"] = "nominal"
                        this["legend"] = withJsonDsl {
                            this["title"] = null
                        }
                    }
                }
            }
        }

        fun horizontalBar(values: List<Number>, categories: List<String>, title: String? = null, categoryTitle: String?=null, valueTitle: String?=null, temporalCategory: Boolean=false): VegaLiteSpec {
            return VegaLiteSpec().apply {
                title?.let {
                    this["title"] = title
                }
                mark("bar")
                categoryAndValueData(values, categories)
                this["encoding"] = withJsonDsl {
                    this["x"] = withJsonDsl {
                        this["field"] = "value"
                        this["type"] = "quantitative"
                        this["title"] = valueTitle
                    }
                    this["y"] = withJsonDsl {
                        this["field"] = "category"
                        if(temporalCategory) {
                            this["type"] = "temporal"
                            this["axis"] = withJsonDsl {
                                this["format"] = "%Y-%m-%d"
                            }
                        } else {
                            this["type"] = "nominal"
                        }
                        this["title"] = categoryTitle
                    }
                    this["color"] = withJsonDsl {
                        this["field"] = "category"
                        this["type"] = "nominal"
                        this["legend"] = withJsonDsl {
                            this["title"] = categoryTitle
                        }
                    }
                }
            }
        }

        fun verticalBarOrLine(values: List<Number>, categories: List<String>, title: String? = null, categoryTitle: String?=null, chartType:String="bar", valueTitle: String?=null,temporalCategory: Boolean=false): VegaLiteSpec {
            return VegaLiteSpec().apply {
                title?.let {
                    this["title"] = title
                }
                mark(chartType)
                categoryAndValueData(values, categories)
                this["encoding"] = withJsonDsl {
                    this["x"] = withJsonDsl {
                        this["field"] = "category"
                        if(temporalCategory) {
                            this["type"] = "temporal"
                            this["axis"] = withJsonDsl {
                                this["format"] = "%Y-%m-%d"
                            }
                        } else {
                            this["type"] = "nominal"
                        }
                        this["title"] = categoryTitle
                    }
                    this["y"] = withJsonDsl {
                        this["field"] = "value"
                        this["type"] = "quantitative"
                        this["title"] = valueTitle
                    }
                    if(chartType == "bar") {
                        this["color"] = withJsonDsl {
                            this["field"] = "category"
                            this["type"] = "nominal"
                            this["legend"] = withJsonDsl {
                                this["title"] = categoryTitle
                            }
                        }
                    }
                }
            }
        }
    }
}
