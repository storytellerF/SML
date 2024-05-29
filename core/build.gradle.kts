plugins {
    kotlin("jvm")
    `maven-publish`
    `kotlin-dsl`
}

val env: MutableMap<String, String> = System.getenv()
group = group.takeIf { it.toString().contains(".") } ?: env["GROUP"]
        ?: "com.storyteller_f.sml_generator"
version = version.takeIf { it != "unspecified" } ?: env["VERSION"] ?: "0.0.1-local"

afterEvaluate {
    publishing {
        publications {
            register<MavenPublication>("release") {
                val component = components.find {
                    it.name == "java" || it.name == "release"
                }
                from(component)
            }
        }
    }
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.20")
    implementation("com.android.tools.build:gradle:8.2.1")
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}

kotlin {
    jvmToolchain(17)
}