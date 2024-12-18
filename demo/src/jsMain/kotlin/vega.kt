@file:JsModule("vega-embed")
@file:JsNonModule

import org.w3c.dom.HTMLElement

external fun embed(
    element: HTMLElement,
    spec: dynamic,
    options: dynamic = definedExternally
): dynamic
