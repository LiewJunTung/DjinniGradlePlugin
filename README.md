# Djinni Gradle Plugin
A Gradle plugin to help generate C++ linkage code such as JNI for Java or ObjC++ for Objective C via [Djinni](https://github.com/dropbox/djinni)

## Installation
Build script snippet for plugins DSL for Gradle 2.1 and later:
```groovy
// build.gradle
plugins {
  id "org.liewjuntung.djinni_plugin" version "1.0"
}
```


Build script snippet for use in older Gradle versions or where dynamic configuration is required:
```groovy
// build.gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "org.liewjuntung:djinni_plugin:1.0"
  }
}

apply plugin: "org.liewjuntung.djinni_plugin"
```

## Usage (runDjinni)
1. Create a .djinni file, for example: `run_djinni.djinni`
2.
    ```groovy
    // build.gradle
    
    djinni {
        val baseDir = "build/djinni/generated-src/" // (optional) default directory for generated files
                                                    // if jniOut, cppOut, objcOut, objcppOut is change, recommend to a use
                                                    // a variable to set a baseDirectory
        idl = "run_djinni.djinni"                                            
        javaPackage = "org.liewjuntung.app"
        javaClassAccessModifier = "package"
        javaNonNullAnnotation
        javaNullableAnnotation
        identJavaField = "mFooBar"
        //jni
        jniOut = "$baseDir/jni"          // if this is changed, use baseDir, use a variable to set a baseDirectory
        identJniFile = "NativeFooBar"
        identJniClass = "NativeFooBar"
        //c++
        cppOut = "$baseDir/cpp"          // if this is changed, use baseDir, use a variable to set a baseDirectory
        cppNamespace = "genDjinni"
        identCppEnumType
        //objc
        objcOut = "$baseDir/objc"        // if this is changed, use baseDir, use a variable to set a baseDirectory
        objcppOut = "$baseDir/objc"      // if this is changed, use baseDir, use a variable to set a baseDirectory
        objcTypePrefix = "Djinni"
        objcSwiftBridgingHeader = "Djinni-Bridging-Header"
    }
    ```
3. `./gradlew runDjinni`

4. [Here is the link to tutorial on Djinni](http://mobilecpptutorials.com/hello-world-app-part-1.html)

## Usage (downloadSupportFile)
A task to download the necessary Djinni support files, by default it will save to build/djinni/support
1. `./gradlew downloadSupportFile`

<img src="img/djinni_support.png" width="300">

See also [https://plugins.gradle.org/plugin/org.liewjuntung.djinni_plugin](https://plugins.gradle.org/plugin/org.liewjuntung.djinni_plugin)
