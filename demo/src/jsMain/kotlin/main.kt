import com.jillesvangurp.jsondsl.toJsObject
import com.jillesvangurp.kotlinechartsdsl.EChartsOption
import echarts.init
import kotlin.random.Random
import kotlin.random.nextULong
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.html.TagConsumer
import kotlinx.html.div
import kotlinx.html.dom.append
import kotlinx.html.h1
import kotlinx.html.id
import kotlinx.html.pre
import kotlinx.html.style
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLPreElement

// begin-addSpecExample
fun TagConsumer<*>.addSpec(option: EChartsOption, debug: Boolean = false) {
    val elementId = Random.nextULong().toString()
    try {
        div {
            id = "echart-$elementId"
            style = "width: 400px; height: 400px; border: 1px solid;"
        }
        if (debug) {
            pre {
                +JSON.stringify(option.toJsObject(), null, 2)
            }
        }
        window.requestAnimationFrame {
            val chartElement = document.getElementById("echart-$elementId")!! as HTMLDivElement
            console.log(chartElement)
            val chart = init(chartElement)
            chart.setOption(option.toJsObject())
        }
    } catch (e: Exception) {
        console.error(e)
    }
}

fun main() {
    console.log("HI")
    document.getElementById("target")?.append {
        div { h1 { +"ECharts Spec Demo" } }
        div {
            style = "display:flex; flex-flow: row wrap; gap:12px; align-items:flex-start; margin:0;"
            addSpec(
                EChartsOption.pie(
                    values = listOf(1, 5, 30, 300),
                    categories = listOf("A", "B", "C", "D"),
                    title = "I like pie!",
                ),
            )
            addSpec(
                EChartsOption.pie(
                    values = listOf(1, 5, 30, 300),
                    categories = listOf("A", "B", "C", "D"),
                    title = "... and donuts",
                    donut = true,
                ),
            )
            addSpec(
                EChartsOption.horizontalBar(
                    values = listOf(1, 2, 3, 4),
                    categories = listOf("A", "B", "C", "D"),
                    title = "Bar Chars",
                ),
            )
            addSpec(
                EChartsOption.verticalBarOrLine(
                    values = listOf(1, 2, 3, 4),
                    categories = listOf("A", "B", "C", "D"),
                    title = "Vertical Bar",
                ),
            )
            addSpec(
                EChartsOption.verticalBarOrLine(
                    values = listOf(4, 2, 1, 3),
                    categories = listOf("A", "B", "C", "D"),
                    chartType = "line",
                    title = "Line Chart",
                ),
            )
            addSpec(
                EChartsOption.verticalBarOrLine(
                    values = listOf(6, 2, 3, 1),
                    categories = listOf(
                        "2024-12-15T00:00:00Z",
                        "2024-12-16T00:00:00Z",
                        "2024-12-17T00:00:00Z",
                        "2024-12-18T00:00:00Z",
                    ),
                    chartType = "line",
                    temporalCategory = true,
                    title = "Line Chart with times",
                ),
            )
        }
    }
}
// end-addSpecExample

