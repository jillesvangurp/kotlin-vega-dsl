package com.jillesvangurp.jsondsl.readme

import com.jillesvangurp.jsondsl.json
import com.jillesvangurp.kotlin4example.SourceRepository
import com.jillesvangurp.kotlinvegadsl.VegaLiteSpec
import java.io.File
import kotlin.test.Test

const val githubLink = "https://github.com/formation-res/pg-docstore"

val sourceGitRepository =
    SourceRepository(
        repoUrl = githubLink,
        sourcePaths = setOf(
            "src/commonMain/kotlin",
            "src/commonTest/kotlin",
            "src/jvmTest/kotlin",
            "../demo/src/jsMain/kotlin",
        ),
    )

class ReadmeGenerationTest {

    @Test
    fun `generate docs`() {
        File("..", "README.md")
            .writeText(
                """
            # JsonDsl

        """.trimIndent().trimMargin() +
                    "\n\n" +
                    readmeMd.value,
            )
    }
}

val readmeMd =
    sourceGitRepository.md {
        includeMdFile("intro.md")

        section("Examples") {
            example {
                VegaLiteSpec.pie(
                    listOf(1, 4),
                    listOf(
                        "Pie I have Eaten",
                        "Pie I have not eaten",
                    ),
                    title = "I like Pie!",
                )
            }.let {
                +"""
                   Produces this json:
                """.trimIndent()

                val spec = it.result.getOrThrow()!!
                mdCodeBlock(spec.json(true), type = "application/json")
            }
            example {
                VegaLiteSpec.horizontalBar(
                    listOf(1, 4),
                    listOf("Pie I have Eaten", "Pie I have not eaten"),
                    title = "I like Pie!",
                )
            }.let {
                +"""
                   Produces this json:
                """.trimIndent()

                val spec = it.result.getOrThrow()!!
                mdCodeBlock(spec.json(true), type = "application/json")
            }
            example {
                VegaLiteSpec.verticalBarOrLine(
                    listOf(1, 4),
                    listOf("Pie I have Eaten", "Pie I have not eaten"),
                    title = "I like Pie!",
                )
            }.let {
                +"""
                   Produces this json:
                """.trimIndent()

                val spec = it.result.getOrThrow()!!
                mdCodeBlock(spec.json(true), type = "application/json")
            }
        }
        section("How to embed in kotlin-js") {
            +"""
                Vega DSL is a multi platform library. But of course it is intended to use with 
                vega-embed, which is a kotlin-js library. You could probably get it working as part 
                of node.js as well and probably there is a web assembly compatible way of doing this as well.
                                
                To facilitate embedding in javascript, some minimal type mapping for the embed 
                function in vega-embed is bundled. Here's an example from the demo module on how you 
                could use that with Kotlin's html DSL in kotlin-js.                                                
            """.trimIndent()

            exampleFromSnippet("main.kt", "addSpecExample",allowLongLines = true)
        }
        includeMdFile("outro.md")
    }
