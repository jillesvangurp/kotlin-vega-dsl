@file:JsModule("echarts")
package echarts

import org.w3c.dom.HTMLDivElement

//@JsName("default")
external fun init(
    dom: HTMLDivElement,
    theme: dynamic = definedExternally,
    opts: dynamic = definedExternally,
): ECharts

external interface ECharts {
    fun setOption(option: dynamic, notMerge: Boolean = definedExternally, lazyUpdate: Boolean = definedExternally)
    fun dispose()
}
