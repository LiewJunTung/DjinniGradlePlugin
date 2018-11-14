import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
    `kotlin-dsl`
    `maven-publish`
}

group = "org.pandawarrior"
version = "1.0"

pluginBundle {
    website = "https://github.com/LiewJunTung/DjinniGradlePlugin"
    vcsUrl = "https://github.com/LiewJunTung/DjinniGradlePlugin"
    tags = listOf("cpp", "java", "jni")

    (plugins) {
        "greetingsPlugin" {
            displayName = "Djinni Gradle Plugin"
            description = "A Djinni Gradle plugin to aid in using Djinni to generate jni and objcpp linker to c++"
        }
    }
}

gradlePlugin {
    plugins {
        create("greetingsPlugin") {
            id = "org.pandawarrior.djinni_plugin"
            implementationClass = "org.pandawarrior.djinni_plugin.DjinniPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    compileOnly(gradleApi())
    testImplementation("junit", "junit", "4.12")
    implementation ("com.fasterxml.jackson.core", "jackson-databind", "2.9.7")
}
