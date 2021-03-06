val kotlinVersion = "1.2.70"

plugins {
    kotlin("jvm").version("1.2.70")
    `java-library`
    id("com.atlassian.performance.tools.gradle-release").version("0.4.3")
}

configurations.all {
    resolutionStrategy {
        activateDependencyLocking()
        failOnVersionConflict()
        eachDependency {
            when (requested.module.toString()) {
                "commons-codec:commons-codec" -> useVersion("1.10")
            }
            when (requested.group) {
                "org.jetbrains.kotlin" -> useVersion(kotlinVersion)
            }
        }
    }
}

dependencies {
    api("com.atlassian.performance.tools:jira-actions:[2.1.0,4.0.0)")
    api("com.github.stephenc.jcip:jcip-annotations:1.0-1")
    api(webdriver("selenium-api"))

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jre8:$kotlinVersion")
    implementation("org.glassfish:javax.json:1.1")
    implementation(webdriver("selenium-support"))
    implementation(webdriver("selenium-chrome-driver"))
    implementation("org.apache.commons:commons-math3:3.6.1")
    log4j(
        "api",
        "core",
        "slf4j-impl"
    ).forEach { implementation(it) }
    testCompile("junit:junit:4.12")
    testCompile("org.assertj:assertj-core:3.11.1")
}

fun log4j(
    vararg modules: String
): List<String> = modules.map { module ->
    "org.apache.logging.log4j:log4j-$module:2.10.0"
}

fun webdriver(module: String): String = "org.seleniumhq.selenium:$module:3.11.0"

val wrapper = tasks["wrapper"] as Wrapper
wrapper.gradleVersion = "4.9"
wrapper.distributionType = Wrapper.DistributionType.ALL