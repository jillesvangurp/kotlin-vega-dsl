package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.toJsObject

fun VegaSpec.Companion.newSpec(block: VegaSpec.() -> Unit): dynamic {
    val spec = VegaSpec()
    block.invoke(spec)
    return spec.toJsObject()
}
