package com.jillesvangurp.kotlinvegadsl
import com.jillesvangurp.jsondsl.JsonDsl

@DslMarker
annotation class VegaDSLMarker

@VegaDSLMarker

class VegaTitle : JsonDsl() {
    var text by property<String>()
}

@VegaDSLMarker
class VegaSpec: JsonDsl() {
    // immutable
    val schema by property("${'$'}schema", "https://vega.github.io/schema/vega/v5.json")
    private var title by property<VegaTitle>()
    fun title(defaultTitle: String = "",block: (VegaTitle.() -> Unit)?=null) {
        val vt = VegaTitle().also {
            it.text = defaultTitle
        }
        block?.invoke(vt)
        title = vt
    }

    // empty companion so we can add extension functions
    companion object {}
}
