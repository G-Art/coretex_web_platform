/*
 * Here we iterate over the directories found in the root directory of the
 * main project, and if the subdirectory contains a build.gradle file, it
 * is added to the main project as a subproject.
 */
import java.util.*

println("Searching projects for build ... ")

rootDir.walkTopDown().forEach { module ->
    if (module.name == "build.gradle" || module.name == "build.gradle.kts") {
        val relativePath: String = module.parentFile.absolutePath.removePrefix(rootDir.absolutePath)
        if (!module.parentFile.absolutePath.equals(rootDir.absolutePath)) {
            module.parentFile.walkTopDown().forEach {
                if (it.isFile() && it.name.equals("module.properties")) {
                    val propFile = it
                    val moduleProperties = Properties()
                    moduleProperties.load(propFile.inputStream())

                    val moduleName: String = moduleProperties.getProperty("module.name")

                    if (moduleName != null && !moduleName.equals("")) {
                        include(moduleName)
                        project(":${moduleName}").projectDir = module.parentFile
                        println("Module included to build [name :${moduleName}, dir: ${project(":${moduleName}").projectDir}]")
                    } else {
                        println("No defined module name for ${relativePath} project")
                    }

                }
            }
        }
    }
}