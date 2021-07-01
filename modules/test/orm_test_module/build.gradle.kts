
configurations.all {
    exclude(group = "commons-logging", module = "commons-logging")
}

dependencies {
    api(project(":core"))
}
