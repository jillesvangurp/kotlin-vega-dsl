package com.jillesvangurp.jsondsl.readme

import com.jillesvangurp.jsondsl.json
import com.jillesvangurp.kotlin4example.SourceRepository
import com.jillesvangurp.kotlinvegadsl.VegaSpec
import java.io.File
import kotlin.test.Test

const val githubLink = "https://github.com/formation-res/pg-docstore"

val sourceGitRepository =
    SourceRepository(
        repoUrl = githubLink,
        sourcePaths = setOf("src/commonMain/kotlin", "src/commonTest/kotlin", "src/jvmTest/kotlin"),
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

        section("Example") {
            example {
                VegaSpec().apply {
                    title("Pie!") {

                    }
                }
            }.let {
                +"""
                   Produces this json:
                """.trimIndent()

                mdCodeBlock(it.result.getOrThrow()!!.json(true), type = "application/json")
            }
        }
        includeMdFile("outro.md")
    }
