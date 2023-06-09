import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    `kotlin-dsl`
}

version = "0.0.2"
group = "com.storyteller_f.sml"

gradlePlugin {
    plugins {
        register("sml") {
            // 插件ID
            id = "com.storyteller_f.sml"
            // 插件的实现类
            implementationClass = "com.storyteller_f.sml.Sml"
        }
    }
}

publishing {
    repositories {
        maven {
            // $rootDir 表示你项目的根目录
            val file = File(rootDir, "../../repo")
            println(file)
            setUrl(file.absolutePath)
        }
    }
}

dependencies {
    testImplementation("junit:junit:4.13.2")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.8.10")
    implementation("com.android.tools.build:gradle:8.0.1")
}
