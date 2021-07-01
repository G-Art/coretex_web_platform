import com.bmuschko.gradle.docker.tasks.image.*

group = "com.coretex.core"
version = "He-2.0.0"

plugins {
    distribution
}


buildscript {
    repositories {
        mavenCentral()
        maven(uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/"))
        maven(uri("https://repo.spring.io/milestone"))
        maven(uri("https://jitpack.io"))
        maven(uri("https://repo.spring.io/libs-release-remote"))
        maven(uri("https://jetbrains.bintray.com/trove4j/"))
    }
    dependencies {
        classpath(group = "com.bmuschko", name = "gradle-docker-plugin", version = "6.6.1")
        classpath(group = "com.coretex.build.plugin.helium", name = "build.plugin", version = "He-2.0.19-Dev")
    }
}

apply(from = "gradle-build/config.gradle.kts")
apply(plugin = "build.plugin")
apply(from = "gradle-build/subprojects.gradle.kts")
apply(plugin = "com.bmuschko.docker-remote-api")

distributions {
    main {
        distributionBaseName.set("coretex-web-platform")
        contents {
            from(projectDir)
            exclude("tmp")
            exclude("**/*.iml")
            exclude("**/.idea")
            exclude("**/.gradle")
            exclude("**/node_modules")
            exclude("**/jsbuild")
            exclude("**/webSrc")
            exclude("**/genSrc")
            exclude("**/classes")
            exclude("**/libs")
            exclude("build")
            exclude("data")
        }
    }
}

//distTar.enabled = false
//distZip.enabled = false


tasks {
    wrapper {
        gradleVersion = "7.1"
    }

    val createDockerfile = register<Dockerfile>("createDockerfile") {
        dependsOn("distTar")
        destFile.set(project.layout.buildDirectory.file("distributions/Dockerfile"))
        from("openjdk:15")
        addFile("coretex-web-platform-H-1.0.tar", "/opt/coretex/")
        workingDir("/opt/coretex/coretex-web-platform-He-2.0.0")
        environmentVariable("JAVA_OPTS", "-Djava.awt.headless=true -server -Xms48m -Xmx512M -XX:MaxPermSize=512m -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/opt/tmp/heapdump.bin")
        exposePort(8080)
    }

    register<DockerBuildImage>("buildImage") {
        dependsOn(createDockerfile)
        inputDir.set(createDockerfile.get().destFile.get().asFile.parentFile)
        images.add("gerasimenkoart/coretex_commerce:latest")
    }

}


