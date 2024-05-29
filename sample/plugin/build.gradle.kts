plugins {
    id("java-gradle-plugin")
    id("maven-publish")
    `kotlin-dsl`
}

version = "0.0.1-local"
group = "com.storyteller_f.sml"

gradlePlugin {
    plugins {
        register("sml") {
            // 插件ID
            id = "com.storyteller_f.sml"
            // 插件的实现类
            implementationClass = "com.storyteller_f.sml.SMLPlugin"
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    implementation("com.android.tools.build:gradle:8.2.1")
    implementation(project(":core"))
//    implementation("com.storyteller_f.sml_generator:core:0.0.1-local")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}