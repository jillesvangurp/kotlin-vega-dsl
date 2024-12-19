import com.jillesvangurp.jsondsl.toJsObject
import com.jillesvangurp.kotlinvegadsl.VegaConfig
import com.jillesvangurp.kotlinvegadsl.VegaEmbeddable
import com.jillesvangurp.kotlinvegadsl.VegaLiteSpec
import com.jillesvangurp.kotlinvegadsl.VegaSpec
import com.jillesvangurp.kotlinvegadsl.config
import com.jillesvangurp.kotlinvegadsl.newSpec
import com.jillesvangurp.kotlinvegadsl.toJsObject
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
import org.w3c.dom.HTMLElement
import org.w3c.dom.HTMLPreElement

fun TagConsumer<*>.addVega(block: VegaSpec.() -> Unit) {
    val spec = VegaSpec.newSpec(block)
    addSpec(spec)
}

fun TagConsumer<*>.addVegaLite(block: VegaSpec.() -> Unit) {
    val spec = VegaSpec.newSpec(block)

    addSpec(spec)
}

fun TagConsumer<*>.addSpec(spec: VegaEmbeddable) {
    val elementId = Random.nextULong().toString()
    div {
        id = "vegademo-$elementId"
        style = "width: 400px; height: 400px; border: 1px solid;"
    }
    pre {
        id = "preblock-$elementId"
    }
    console.log(elementId)
    val vegaElement = document.getElementById("vegademo-$elementId")!! as HTMLElement
    console.log(vegaElement)

//    console.log(spec)
    embed(
        vegaElement, spec.toJsObject(),
        VegaConfig.config {
            actions = false
        }.toJsObject(),
    )

    val preEl = document.getElementById("preblock-$elementId")!! as HTMLPreElement
    preEl.append(JSON.stringify(spec.toJsObject(), null, 2))
    console.log("DONE")
}


fun main() {
    console.log("HI")
    document.getElementById("target")?.append {
        div {
            h1 {
                +"Vega Spec Demo"
            }
        }
        addSpec(
            VegaLiteSpec.pie(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D"))
        )
        addSpec(
            VegaLiteSpec.horizontalBar(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D"))
        )
        addSpec(
            VegaLiteSpec.verticalBar(listOf(1, 2, 3, 4), listOf("A", "B", "C", "D"))
        )

        addVega {
            title("Horizontal Bar Chart")
            table(
                "pieslices",
                listOf(4.0, 1.0),
                listOf("Pie I have not Eaten", "Pie I have Eaten"),
            ) {
            }
            ordinalColorScale("pieslices")
            horizontalBarChartScales(dataSourceName = "pieslices")
            horizontalBarChartMarks(dataSourceName = "pieslices")
            simpleLegend()
        }

        addVega {
            title("Pie Chart")
            table(
                "pieslices",
                listOf(4.0, 1.0),
                listOf("Pie I have not Eaten", "Pie I have Eaten"),
            ) {
                pieTransform()
            }
            ordinalColorScale("pieslices")
            pieArcMarks("pieslices")
            simpleLegend()
        }
    }


//    val vegaElement = document.getElementById("vegademo")!! as HTMLElement
//
//    console.log(vegaElement)
//    val spec = VegaSpec.newSpec {
//        title("I like Pie!")
//        table("pieslices", listOf(4.0, 1.0), listOf("Pie I have not Eaten", "Pie I have Eaten")) {
//            pieTransform()
//        }
//        ordinalScale("pieslices")
//        pieArcMarks("pieslices")
//        simpleLegend()
//
//    }
//    console.log(spec)
//    embed(vegaElement, spec, VegaConfig.config {
//        actions = false
//    })
//
//    val preEl = document.getElementById("preblock")!! as HTMLPreElement
//    preEl.append(JSON.stringify(spec,null,2))
//    console.log("DONE")
}
