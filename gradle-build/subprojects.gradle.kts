subprojects {
    val v = mapOf(
            "spring" to "5.3.3",
            "slf4j" to "1.7.7")
    extra["v"] = v

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_15
        targetCompatibility = JavaVersion.VERSION_15
    }

    repositories {
        mavenCentral()
        maven(uri("https://repo.coretexplatform.com/artifactory/coretex_repo-libs-release/"))
        maven(uri("https://repo.spring.io/milestone"))
        maven(uri("https://repo.spring.io/libs-release-remote"))
        maven(uri("https://jetbrains.bintray.com/trove4j/"))
    }

    val testImplementation by configurations
    dependencies {
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-api", version = "5.3.1")
        testImplementation(group = "org.hamcrest", name = "java-hamcrest", version = "2.0.0.0")
        testImplementation(group = "org.mockito", name = "mockito-core", version = "2.21.0")
        testImplementation(group = "org.junit.jupiter", name = "junit-jupiter-engine", version = "5.3.1")
    }

    configure<SourceSetContainer> {
        named("main") {
            java.srcDir("resources")
        }
        named("test") {
            java.srcDir("testResources")
        }
    }

    tasks.getByName<Jar>("jar").enabled = false
}