@file:Suppress("unused", "UnstableApiUsage")

package com.storyteller_f.sml

import com.android.build.gradle.internal.tasks.factory.dependsOn
import com.storyteller_f.sml.tasks.ColorTask
import com.storyteller_f.sml.tasks.DimensionTask
import com.storyteller_f.sml.tasks.DrawableDomain
import com.storyteller_f.sml.tasks.ShapeTask
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.MapProperty
import org.gradle.api.tasks.TaskProvider
import org.gradle.kotlin.dsl.support.uppercaseFirstChar
import java.io.File
import java.util.*

interface SmlExtension {
    val color: MapProperty<String, String>
    val dimen: MapProperty<String, String>
    val drawables: NamedDomainObjectContainer<DrawableDomain>
}

class Sml : Plugin<Project> {
    override fun apply(project: Project) {
        val taskProviderList = setup(project, "main", "main")
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

    private fun setupDependencies(project: Project, taskProviderList: List<TaskProvider<out DefaultTask>>) {
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
        variantName: String
    ): List<TaskProvider<out DefaultTask>> {
        val extension = project.extensions.create("sml", SmlExtension::class.java)
        val rootPath = "${project.buildDir}/generated"
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
        dimensMap = extension.dimen.get()
    }

    private fun ColorTask.config(
        colorsOutputDirectory: File,
        extension: SmlExtension
    ) {
        group = "sml"
        outputFile = File(colorsOutputDirectory, "values/generated_colors.xml")
        colorsMap = extension.color.get()
    }

    private fun taskName(type: String, variantName: String): String {
        val name =
            variantName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        return "generate$type$name"
    }
}