@file:JsModule("vega-embed")
@file:JsNonModule
package vegaembed

import org.w3c.dom.HTMLElement

external fun embed(
    element: HTMLElement,
    spec: dynamic,
    options: dynamic = definedExternally
): dynamic
