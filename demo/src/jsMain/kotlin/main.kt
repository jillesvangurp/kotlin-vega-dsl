import com.jillesvangurp.jsondsl.toJsObject
import com.jillesvangurp.jsondsl.withJsonDsl
import com.jillesvangurp.kotlinvegadsl.EChartsOption
import com.jillesvangurp.kotlinvegadsl.jsObject
import echarts.init
import kotlin.random.Random
import kotlin.random.nextULong
import kotlinx.browser.document
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
fun TagConsumer<*>.addSpec(option: EChartsOption) {
    val elementId = Random.nextULong().toString()
    div {
        id = "echart-$elementId"
        style = "width: 400px; height: 400px; border: 1px solid;"
    }
    pre { id = "preblock-$elementId" }
    val chartElement = document.getElementById("echart-$elementId")!! as HTMLDivElement
    val chart = init(chartElement)
    chart.setOption(option.jsObject())
    val preEl = document.getElementById("preblock-$elementId")!! as HTMLPreElement
    preEl.append(JSON.stringify(option.jsObject(), null, 2))
}

fun main() {
    console.log("HI")
    document.getElementById("target")?.append {
        div { h1 { +"ECharts Spec Demo" } }
        addSpec(EChartsOption.pie(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D")))
//        addSpec(EChartsOption.horizontalBar(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D")))
//        addSpec(EChartsOption.verticalBarOrLine(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D")))
//        addSpec(EChartsOption.verticalBarOrLine(listOf(4, 2, 1, 3), listOf("A", "B", "C", "D"), chartType = "line"))
//        addSpec(
//            EChartsOption.verticalBarOrLine(
//                listOf(1, 2, 3, 4),
//                listOf(
//                    "2024-12-15T00:00:00Z",
//                    "2024-12-16T00:00:00Z",
//                    "2024-12-17T00:00:00Z",
//                    "2024-12-18T00:00:00Z",
//                ),
//                chartType = "line",
//                temporalCategory = true,
//            ),
//        )
    }
}
// end-addSpecExample

