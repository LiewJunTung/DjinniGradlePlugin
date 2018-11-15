package org.liewjuntung.djinni_plugin

import com.fasterxml.jackson.databind.ObjectMapper
import djinni.Main
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.InputStream
import java.net.URL

open class DjinniPluginExtension {
    var baseDir: String = "build/djinni/generated-src/"
        set(value) {
            javaOut = javaOut ?: "$value/java"
            jniOut = jniOut ?: "$value/jni"
            cppOut = cppOut ?: "$value/cpp"
            objcOut = objcOut ?: "$value/objc"
            objcppOut = objcppOut ?: "$value/objc"
        }
    var idl: String = "run_djinni.djinni"
    //java
    var javaFile = ""
    var javaOut: String = "$baseDir/java/$javaFile"
    var javaPackage: String = "foo.bar.app"
        set(value) {
            value.replace(".", "/").let {
                javaOut = "$baseDir/java/$it"
            }
        }


    var javaClassAccessModifier: String? = "package"
    var javaNonNullAnnotation: String? = null
    var javaNullableAnnotation: String? = null
    var identJavaField: String = "mFooBar"
    //jni
    var jniOut: String? = "$baseDir/jni"
    var identJniFile: String = "NativeFooBar"
    var identJniClass: String = "NativeFooBar"
    //c++
    var cppOut: String = "$baseDir/cpp"
    var cppNamespace: String = "genDjinni"
    var identCppEnumType: String? = null
    //objc
    var objcOut: String = "$baseDir/objc"
    var objcppOut: String = "$baseDir/objc"
    var objcTypePrefix: String = "Djinni"
    var objcSwiftBridgingHeader: String = "Djinni-Bridging-Header"
}

data class DjinniSupportFile(val name: String, val downloadUrl: String, val htmlUrl: String, val parentDir: String)

open class DjinniDefaultTask : DefaultTask() {

    val extension: DjinniPluginExtension? = project.extensions.findByType(DjinniPluginExtension::class.java)

    @TaskAction
    fun runDjinni() {
        val bb = project.delete(extension?.baseDir)
        if (extension != null) {
            Main.main(
                arrayOf(
                    "--idl", extension.idl,
                    "--java-package", extension.javaPackage,
                    "--java-out", extension.javaOut,
                    "--ident-java-field", extension.identJavaField,
                    "--cpp-out", extension.cppOut,
                    "--cpp-namespace", extension.cppNamespace,
                    "--jni-out", extension.jniOut,
                    "--ident-jni-class", extension.identJniClass,
                    "--ident-jni-file", extension.identJniFile,
                    "--objc-out", extension.objcOut,
                    "--objc-type-prefix", extension.objcTypePrefix,
                    "--objcpp-out", extension.objcppOut,
                    "--objc-swift-bridging-header", extension.objcSwiftBridgingHeader
                )
            )
        }
    }

    //val joined = a.joinToString(" ")
    //System.exit(0)
}

open class DjinniSupportPluginExtension {
    var jniSupportUrl: String? = "https://api.github.com/repos/dropbox/djinni/contents/support-lib/jni"
    var objcSupportUrl: String? = "https://api.github.com/repos/dropbox/djinni/contents/support-lib/objc"

    var jniSupportDirectory: String? = "jni"
    var objcSupportDirectory: String? = "objc"
    var baseDirectory: String = "build/djinni/support"
}

open class DjinniSupportDownloadTask : DefaultTask() {

    val extension: DjinniSupportPluginExtension? =
        project.extensions.findByType(DjinniSupportPluginExtension::class.java)

    @TaskAction
    fun runDownload() {
        if (extension != null) {
            val jniList = fetchUrl(extension.jniSupportUrl, "jni")
            val objcList = fetchUrl(extension.objcSupportUrl, "objc")
            jniList.saveFiles()
            objcList.saveFiles()
        }
    }

    fun fetchUrl(url: String?, parentDir: String): List<DjinniSupportFile> {
        val inputStream = URL(url).openConnection().apply {
            readTimeout = 800
            connectTimeout = 200
        }.getInputStream()
        val mapper = ObjectMapper()
        val result = mapper.readTree(inputStream).map { node ->
            val name = node.get("name").asText()
            val downloadUrl = node.get("download_url").asText()
            val htmlUrl = node.get("html_url").asText()
            DjinniSupportFile(
                name,
                downloadUrl,
                htmlUrl,
                parentDir
            )
        }

        return result
    }

    fun saveFile(djinniSupportFile: DjinniSupportFile) {
        val inputStream = URL(djinniSupportFile.downloadUrl).openConnection().apply {
            readTimeout = 800
            connectTimeout = 200
        }.getInputStream()

        val supportDir = File("${extension!!.baseDirectory}/${djinniSupportFile.parentDir}")
        if (!supportDir.exists()) {
            supportDir.mkdirs()
        }
        File(supportDir, djinniSupportFile.name).copyInputStreamToFile(inputStream)
    }

    fun List<DjinniSupportFile>.saveFiles() {
        forEach {
            saveFile(it)
        }
    }

    fun File.copyInputStreamToFile(inputStream: InputStream) {
        inputStream.use { input ->
            this.outputStream().use { fileOut ->
                input.copyTo(fileOut)
            }
        }
    }
}
