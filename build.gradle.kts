plugins {
    id("com.gradle.plugin-publish") version "1.2.1"
    kotlin("jvm") version "1.7.10"
    kotlin("kapt") version "1.7.10"
}

repositories {
    mavenCentral()
}

object Constant {
    val pluginName = "AndroidLintReporterPlugin"
    val group = "io.github.vast00"
    val id = "io.github.vast00.android_lint_reporter"
    val implementationClass = "android_lint_reporter.AndroidLintReporterPlugin"
    val version = "2.2.0"
    val website = "https://github.com/vast00/AndroidLintReporter"
    val displayName = "Android Lint Reporter"
    val description = "Gradle Plugin to parse, format, report Android Lint result back to Github Pull Request using Github Actions"
    val tags = listOf("android", "lint", "github-actions")
}

object Version {
    val retrofit = "2.8.2"
    val moshi = "1.14.0"
}

dependencies {
    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))
    implementation("com.squareup.moshi:moshi-kotlin:${Version.moshi}")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:${Version.moshi}")

    implementation("com.squareup.retrofit2:retrofit:${Version.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Version.retrofit}")

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

// Add a source set for the functional test suite
val functionalTestSourceSet = sourceSets.create("functionalTest") {
    compileClasspath += sourceSets.main.get().output
    runtimeClasspath += sourceSets.main.get().output
}

gradlePlugin.testSourceSets(functionalTestSourceSet)
configurations.getByName("functionalTestImplementation").extendsFrom(configurations.getByName("testImplementation"))

// Add a task to run the functional tests
val functionalTest by tasks.creating(Test::class) {
    testClassesDirs = functionalTestSourceSet.output.classesDirs
    classpath = functionalTestSourceSet.runtimeClasspath
}

val check by tasks.getting(Task::class) {
    // Run the functional tests as part of `check`
    dependsOn(functionalTest)
}

group = Constant.id
version = Constant.version

gradlePlugin {
    website.set(Constant.website)
    vcsUrl.set(Constant.website)
    plugins {
        create(Constant.pluginName) {
            id = Constant.id
            displayName = Constant.displayName
            description = Constant.description
            tags.set(Constant.tags)
            implementationClass = Constant.implementationClass
        }
    }
}
