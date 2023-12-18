package com.storyteller_f.sml

import org.gradle.api.Action
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import java.io.File

fun Project.android(configure: Action<com.android.build.gradle.internal.dsl.BaseAppModuleExtension>): Unit =
    (this as ExtensionAware).extensions.configure("android", configure)

fun Project.kotlin(configure: Action<KotlinAndroidProjectExtension>): Unit =
    (this as ExtensionAware).extensions.configure("kotlin", configure)

fun File.writeXlmWithTags(body: String) {
    ("<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
            "<resources>" +
            "$body\n" +
            "</resources>")
        .also { resXml ->
            try {
                createNewFile()
                writeText(resXml)
            } catch (e: Exception) {
                throw GradleException(e.message ?: "")
            }
        }
}