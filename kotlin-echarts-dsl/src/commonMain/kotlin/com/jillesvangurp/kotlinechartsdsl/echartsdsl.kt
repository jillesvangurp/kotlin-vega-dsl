package com.jillesvangurp.kotlinechartsdsl

import com.jillesvangurp.jsondsl.JsonDsl
import com.jillesvangurp.jsondsl.PropertyNamingConvention
import com.jillesvangurp.jsondsl.withJsonDsl

@DslMarker
annotation class EChartsDSLMarker

@EChartsDSLMarker
class EChartsOption : JsonDsl(namingConvention = PropertyNamingConvention.AsIs), EmbeddableEchart {
    companion object {
        fun pie(
            values: List<Number>,
            categories: List<String>,
            donut: Boolean = false,
            title: String? = null
        ): EChartsOption {
            val displayNames = categories.zip(values).map { (c, v) -> "$c: $v" }

            return EChartsOption().apply {
                title?.let { this["title"] = withJsonDsl { this["text"] = it } }

                this["tooltip"] = withJsonDsl {
                    this["trigger"] = "item"
                    this["formatter"] = "{c}" // show only the value to avoid duplicating the name+(value)
                }

                this["legend"] = withJsonDsl {
                    this["orient"] = "vertical"
                    this["top"] = "top"
                    this["right"] = 0
                    this["data"] = displayNames  // static, precomputed
                }

                this["series"] = listOf(
                    withJsonDsl {
                        this["name"] = "Access From"
                        this["type"] = "pie"
                        this["radius"] = if (donut) listOf("40%", "60%") else "80%"
                        this["data"] = displayNames.zip(values).map { (dn, v) ->
                            withJsonDsl {
                                this["name"] = dn          // legend shows "<cat> (<value>)"
                                this["value"] = v
                            }
                        }
                        this["label"] = withJsonDsl {
                            this["show"] = true
                            this["position"] = "inside"
                            this["formatter"] = "{c}"     // numbers inside slices
                        }
                        this["labelLine"] = withJsonDsl { this["show"] = false }
                        this["avoidLabelOverlap"] = true
                        this["minShowLabelAngle"] = 10
                        this["emphasis"] = withJsonDsl {
                            this["itemStyle"] = withJsonDsl {
                                this["shadowBlur"] = 5
                                this["shadowOffsetX"] = 0
                                this["shadowColor"] = "rgba(0, 0, 0, 0.5)"
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
            // Precompute static display labels: "Category: Value"
            val displayCats = categories.zip(values).map { (c, v) -> "$c: $v" }

            return EChartsOption().apply {
                title?.let { this["title"] = withJsonDsl { this["text"] = it } }

                this["tooltip"] = withJsonDsl {
                    this["trigger"] = "axis"
                    this["axisPointer"] = withJsonDsl { this["type"] = "shadow" }
                    this["formatter"] = "{b}: {c}"
                }

                this["xAxis"] = withJsonDsl {
                    this["type"] = "value"
                    valueTitle?.let { this["name"] = it }
                }

                this["yAxis"] = withJsonDsl {
                    this["type"] = if (temporalCategory) "time" else "category"
                    // Use precomputed labels so values appear on the axis
                    this["data"] = displayCats
                    categoryTitle?.let { this["name"] = it }
                }

                // Legend is usually not useful for a single-series bar; keep hidden
                this["legend"] = withJsonDsl {
                    this["show"] = false
                    this["orient"] = "vertical"
                    this["top"] = "top"
                    this["right"] = 0
                }

                this["series"] = listOf(
                    withJsonDsl {
                        this["type"] = "bar"
                        this["data"] = values
                        this["label"] = withJsonDsl {
                            this["show"] = true
                            this["position"] = "insideRight"
                            this["formatter"] = "{c}"
                        }
                        // Hide overlapping labels if bars are thin
                        this["labelLayout"] = withJsonDsl { this["hideOverlap"] = true }
                        this["emphasis"] = withJsonDsl {
                            this["itemStyle"] = withJsonDsl {
                                this["shadowBlur"] = 5
                                this["shadowOffsetX"] = 0
                                this["shadowColor"] = "rgba(0, 0, 0, 0.5)"
                            }
                        }
                    }
                )
            }
        }

        fun verticalBarOrLine(
            values: List<Number>,
            categories: List<String>,          // ISO-8601 or epoch ms (as strings/numbers)
            title: String? = null,
            categoryTitle: String? = null,
            chartType: String = "bar",         // "bar" | "line"
            valueTitle: String? = null,
            temporalCategory: Boolean = false,
        ): EChartsOption {
            return EChartsOption().apply {
                title?.let { this["title"] = withJsonDsl { this["text"] = it } }

                // avoid clipping long time labels
                this["grid"] = withJsonDsl { this["containLabel"] = true }

                this["tooltip"] = withJsonDsl {
                    this["trigger"] = "axis"
                    this["axisPointer"] = withJsonDsl { this["type"] = if (chartType == "line") "line" else "shadow" }
                }

                // X axis
                this["xAxis"] = withJsonDsl {
                    if (temporalCategory) {
                        this["type"] = "time"
                        this["boundaryGap"] = if (chartType == "bar") true else false
                        this["axisLabel"] = withJsonDsl {
                            this["formatter"] = "{MM}-{dd}"      // tweak as needed
                            this["hideOverlap"] = true
                            this["interval"] = "auto"
                            this["showMinLabel"] = true
                            this["showMaxLabel"] = true
                        }
                    } else {
                        this["type"] = "category"
                        this["data"] = categories
                    }
                    categoryTitle?.let { this["name"] = it }
                }

                // Y axis
                this["yAxis"] = withJsonDsl {
                    this["type"] = "value"
                    valueTitle?.let { this["name"] = it }
                }

                // single-series chart → no legend
                this["legend"] = withJsonDsl { this["show"] = false }

                // Series
                this["series"] = listOf(
                    withJsonDsl {
                        this["type"] = chartType
                        if (temporalCategory) {
                            // pass [time, value] pairs; time can be ISO string or epoch ms (NOT seconds)
                            this["data"] = categories.zip(values).map { (t, v) -> listOf(t, v) }
                        } else {
                            this["data"] = values
                        }
                        if (chartType == "line") this["showSymbol"] = true

                        // keep chart clean; show value only on emphasis/hover
                        this["label"] = withJsonDsl { this["show"] = false }
                        this["emphasis"] = withJsonDsl {
                            this["focus"] = "series"
                            this["label"] = withJsonDsl {
                                this["show"] = true
                                this["position"] = "top"
                                this["formatter"] = "{c}"
                            }
                        }
                        this["labelLayout"] = withJsonDsl { this["hideOverlap"] = true }
                    }
                )
            }
        }
    }
}

