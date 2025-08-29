package com.jillesvangurp.kotlinvegadsl

import com.jillesvangurp.jsondsl.toJsObject

fun VegaEmbeddable.jsObject(): dynamic {
    return when(this) {
        is VegaSpec -> this.toJsObject()
        is VegaLiteSpec -> this.toJsObject()
        is EChartsOption -> this.toJsObject()
        else -> throw IllegalArgumentException("Not a supported type ${this::class.simpleName}")
    }
}
