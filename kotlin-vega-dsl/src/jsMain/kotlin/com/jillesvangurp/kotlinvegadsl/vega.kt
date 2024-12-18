package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.toJsObject

fun VegaSpec.Companion.newSpec(block: VegaSpec.() -> Unit): dynamic {
    return VegaSpec().apply(block).toJsObject()
}

fun VegaLiteSpec.Companion.newSpec(block: VegaLiteSpec.() -> Unit): dynamic {
    return VegaLiteSpec().apply(block).toJsObject()
}


fun VegaConfig.Companion.config(block: VegaConfig.() -> Unit): dynamic {
    return VegaConfig().apply(block).toJsObject()

}
