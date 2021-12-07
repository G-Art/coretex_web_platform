configurations.all {
    exclude(group = "commons-logging", module = "commons-logging")
    exclude(group = "javax.servlet", module = "servlet-api")
}

val v: Map<String, String> by extra
dependencies {
    api(group = "com.google.guava", name = "guava", version = "28.1-jre")
    api(group = "org.apache.commons", name = "commons-lang3", version = "3.9")
    api(group = "org.apache.commons", name = "commons-collections4", version = "4.4")

    api(group = "commons-io", name = "commons-io", version = "2.7")

    api(group = "ch.qos.logback", name = "logback-classic", version = "1.2.3")
    api(group = "org.slf4j", name = "jcl-over-slf4j", version = "1.7.26")
    api(group = "org.apache.metamodel", name = "MetaModel-jdbc", version = "5.3.1")
    api(group = "com.fasterxml.jackson.core", name = "jackson-databind", version = "2.9.9.3")
    api(group = "com.fasterxml.jackson.dataformat", name = "jackson-dataformat-xml", version = "2.9.9")

    api(group = "com.github.jsqlparser", name = "jsqlparser", version = "4.1")

    api(group = "org.javalite", name = "javalite-common", version = "2.3")
    api(group = "com.zaxxer", name = "HikariCP", version = "3.4.1")

    api(group = "org.apache.calcite", name = "calcite-core", version = "1.28.0")

    api(group = "org.springframework.boot", name = "spring-boot-starter-web", version = "2.5.5")
    api(group = "org.springframework", name = "spring-core", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-context", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-beans", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-jdbc", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-aop", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-aspects", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-instrument", version = "${v["spring"]}")
    api(group = "org.springframework", name = "spring-test", version = "${v["spring"]}")

    web(group = "org.springframework", name = "spring-web", version = "${v["spring"]}")
    web(group = "javax.servlet", name = "javax.servlet-api", version = "4.0.1")
}

calcite {
    config.set(file("src/main/codegen/config.fmpp"))
    templates.set(file("src/main/codegen/templates"))
    packageName.set("com.coretex.core.activeorm.query")
}

//val fmppMain by tasks.registering(org.apache.calcite.buildtools.fmpp.FmppTask::class) {
//    config.set(file("codegen/config.fmpp"))
//    templates.set(file("codegen/templates"))
//}
//
//val javaCCMain by tasks.registering(org.apache.calcite.buildtools.javacc.JavaCCTask::class) {
//    dependsOn(fmppMain)
//    val parserFile = fmppMain.map {
//        it.output.asFileTree.matching { include("**/Parser.jj") }
//    }
//    inputFile.from(parserFile)
//    packageName.set("org.apache.calcite.sql.parser.impl")
//}

//tasks.compileKotlin {
//    dependsOn(javaCCMain)
//}
