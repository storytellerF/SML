@file:Suppress("unused", "UnstableApiUsage")

package com.storyteller_f.sml.generator

import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.storyteller_f.sml.generator.tasks.ColorTask
import com.storyteller_f.sml.generator.tasks.DimensionTask
import com.storyteller_f.sml.generator.tasks.DrawableDomain
import com.storyteller_f.sml.generator.tasks.ShapeTask
import org.gradle.api.DefaultTask
import org.gradle.api.Project
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.io.File
import java.util.*

class SmlExtension(
    val color: Map<String, String>,
    val dimen: Map<String, String>,
    val drawables: List<DrawableDomain>
)

class Sml {
    fun apply(project: Project, extension: SmlExtension) {
        val taskProviderList = setup(project, "main", "main", extension)
        setupDependencies(project, taskProviderList)
        setupSourceSets(project)
    }

    private fun setupSourceSets(project: Project) {
        project.android {
            val smlTargetPath = listOf("sml_res_colors", "sml_res_dimens", "sml_res_drawables")
            val debugPath = smlTargetPath.map {
                "build/generated/$it/"
            }
            val type = listOf("main")
            sourceSets {
                type.forEach {
                    getByName(it) {
                        res.srcDirs(debugPath.map { p ->
                            "$p$it"
                        }.toTypedArray())
                    }
                }
            }
        }
    }

    private fun setupDependencies(
        project: Project,
        taskProviderList: List<TaskProvider<out DefaultTask>>
    ) {
        project.afterEvaluate {
            listOf("debug", "release").forEach {
                val typeName = it.uppercaseFirstChar()
                listOf(
                    "map${typeName}SourceSetPaths",
                    "generate${typeName}Resources",
                    "merge${typeName}Resources",
                ).forEach { name ->
                    val task = tasks.getByName(name)
                    taskProviderList.forEach { taskProvider ->
                        task.dependsOn(taskProvider)
                    }
                }
            }
        }
    }

    @Suppress("SameParameterValue")
    private fun setup(
        project: Project,
        subPath: String,
        variantName: String,
        extension: SmlExtension
    ): List<TaskProvider<out DefaultTask>> {
        val rootPath = "${project.layout.buildDirectory.asFile.get()}/generated"
        val genericTask = project.tasks.register("generateSML") {
            group = "sml"
        }
        return listOf(
            project.tasks.register(taskName("Colors", variantName), ColorTask::class.java) {
                val colorsOutputDirectory =
                    File(File(rootPath, "sml_res_colors"), subPath).apply { mkdirs() }
                config(colorsOutputDirectory, extension)
            },
            project.tasks.register(taskName("Dimens", variantName), DimensionTask::class.java) {
                val dimensOutputDirectory =
                    File(File(rootPath, "sml_res_dimens"), subPath).apply { mkdirs() }
                config(dimensOutputDirectory, extension)
            },
            project.tasks.register(taskName("Shapes", variantName), ShapeTask::class.java) {
                val drawablesOutputDirectory =
                    File(File(rootPath, "sml_res_drawables"), subPath).apply { mkdirs() }
                config(drawablesOutputDirectory, extension)
            }
        ).onEach {
            genericTask.dependsOn(it)
        }
    }

    private fun ShapeTask.config(
        drawablesOutputDirectory: File,
        extension: SmlExtension
    ) {
        group = "sml"
        val path = File(drawablesOutputDirectory, "drawable")
        outputDirectory = path
        outputFileList = extension.drawables.map { shapeDomain ->
            File(path, "${shapeDomain.name}.xml")
        }.toTypedArray()
        drawableDomains = extension.drawables.toTypedArray()
    }

    private fun DimensionTask.config(
        dimensOutputDirectory: File,
        extension: SmlExtension
    ) {
        group = "sml"
        outputFile = File(dimensOutputDirectory, "values/generated_dimens.xml")
        dimensMap = extension.dimen.toMutableMap()
    }

    private fun ColorTask.config(
        colorsOutputDirectory: File,
        extension: SmlExtension
    ) {
        group = "sml"
        outputFile = File(colorsOutputDirectory, "values/generated_colors.xml")
        colorsMap = extension.color.toMutableMap()
    }

    private fun taskName(type: String, variantName: String): String {
        val name =
            variantName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        return "generate$type$name"
    }
}