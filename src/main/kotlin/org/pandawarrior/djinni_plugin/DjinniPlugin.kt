package org.pandawarrior.djinni_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.createTask

class DjinniPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        //To change body of created functions use File | Settings | File Templates.
        project.run {
            extensions.create("djinniDownload", DjinniSupportPluginExtension::class.java)
            extensions.create("djinni", DjinniPluginExtension::class.java)
            createTask("runDjinni", DjinniDefaultTask::class) {
                group = "djinni"
            }
            createTask("downloadSupportFile", DjinniSupportDownloadTask::class) {
                group = "djinni"
            }
        }
    }

}
