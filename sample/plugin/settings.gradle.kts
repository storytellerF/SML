@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
    }
}

rootProject.name = "plugin"

include("core")
project(":core").projectDir = File(rootProject.projectDir,"../../core")