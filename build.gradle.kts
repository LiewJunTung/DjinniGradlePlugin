import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.gradle.jvm.tasks.Jar

plugins {
    `java-gradle-plugin`
    id("com.gradle.plugin-publish") version "0.10.0"
    `kotlin-dsl`
    `maven-publish`
}

group = "org.liewjuntung"
version = "1.1"

val extraLib by configurations.creating {
    configurations["implementation"].extendsFrom(this)
}


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
            id = "org.liewjuntung.djinni_plugin"
            implementationClass = "org.liewjuntung.djinni_plugin.DjinniPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

dependencies {
    extraLib(fileTree("libs/djinni.jar"))
    compileOnly(gradleApi())
    testImplementation("junit", "junit", "4.12")
    implementation("com.fasterxml.jackson.core", "jackson-databind", "2.9.7")
}
//
tasks.withType<Jar>() {
    configurations["extraLib"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
}