@file:JsModule("echarts")
@file:JsNonModule
package echarts

import org.w3c.dom.HTMLElement

external object echarts {
    fun init(dom: HTMLElement): EChartsInstance
}

external interface EChartsInstance {
    fun setOption(option: dynamic)
}

