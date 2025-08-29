package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.JsonDsl
import com.jillesvangurp.jsondsl.PropertyNamingConvention
import com.jillesvangurp.jsondsl.withJsonDsl

@DslMarker
annotation class EChartsDSLMarker

@EChartsDSLMarker
class EChartsOption : JsonDsl(namingConvention = PropertyNamingConvention.AsIs), VegaEmbeddable {
    companion object {
        fun pie(values: List<Number>, categories: List<String>, donut: Boolean = false, title: String? = null): EChartsOption {
            return EChartsOption().apply {
                title?.let {
                    this["title"] = withJsonDsl { this["text"] = it }
                }
                this["tooltip"] = withJsonDsl { this["trigger"] = "item" }
                this["legend"] = withJsonDsl { this["data"] = categories }
                this["series"] = listOf(
                    withJsonDsl {
                        this["type"] = "pie"
                        if (donut) {
                            this["radius"] = listOf("40%", "70%")
                        } else {
                            this["radius"] = "70%"
                        }
                        this["data"] = categories.zip(values).map { (c, v) ->
                            withJsonDsl {
                                this["value"] = v
                                this["name"] = c
                            }
                        }
                    }
                )
            }
        }

        fun horizontalBar(
            values: List<Number>,
            categories: List<String>,
            title: String? = null,
            categoryTitle: String? = null,
            valueTitle: String? = null,
            temporalCategory: Boolean = false,
        ): EChartsOption {
            return EChartsOption().apply {
                title?.let {
                    this["title"] = withJsonDsl { this["text"] = it }
                }
                this["tooltip"] = withJsonDsl {}
                this["xAxis"] = withJsonDsl {
                    this["type"] = "value"
                    valueTitle?.let { this["name"] = it }
                }
                this["yAxis"] = withJsonDsl {
                    this["type"] = if (temporalCategory) "time" else "category"
                    this["data"] = categories
                    categoryTitle?.let { this["name"] = it }
                }
                this["legend"] = withJsonDsl { this["show"] = false }
                this["series"] = listOf(
                    withJsonDsl {
                        this["type"] = "bar"
                        this["data"] = values
                    }
                )
            }
        }

        fun verticalBarOrLine(
            values: List<Number>,
            categories: List<String>,
            title: String? = null,
            categoryTitle: String? = null,
            chartType: String = "bar",
            valueTitle: String? = null,
            temporalCategory: Boolean = false,
        ): EChartsOption {
            return EChartsOption().apply {
                title?.let {
                    this["title"] = withJsonDsl { this["text"] = it }
                }
                this["tooltip"] = withJsonDsl {}
                this["xAxis"] = withJsonDsl {
                    this["type"] = if (temporalCategory) "time" else "category"
                    this["data"] = categories
                    categoryTitle?.let { this["name"] = it }
                }
                this["yAxis"] = withJsonDsl {
                    this["type"] = "value"
                    valueTitle?.let { this["name"] = it }
                }
                this["legend"] = withJsonDsl { this["show"] = chartType == "bar" }
                this["series"] = listOf(
                    withJsonDsl {
                        this["type"] = chartType
                        this["data"] = values
                    }
                )
            }
        }
    }
}

