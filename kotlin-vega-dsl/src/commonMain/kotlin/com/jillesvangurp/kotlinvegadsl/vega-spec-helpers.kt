package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.JsonDsl

fun VegaSpec.Companion.newSpec(block: VegaSpec.() -> Unit): VegaEmbeddable {
    return VegaSpec().apply(block)
}

fun VegaLiteSpec.Companion.newSpec(block: VegaLiteSpec.() -> Unit): VegaEmbeddable {
    return VegaLiteSpec().apply(block)
}

fun VegaConfig.Companion.config(block: VegaConfig.() -> Unit): JsonDsl {
    return VegaConfig().apply(block)
}
