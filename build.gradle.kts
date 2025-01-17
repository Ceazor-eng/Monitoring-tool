// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1"
    id("io.gitlab.arturbosch.detekt") version "1.23.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20" apply false
    id("com.google.dagger.hilt.android") version "2.49" apply false
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    ktlint {
        verbose.set(true)
        android.set(true)
        filter {
            exclude("**/generated/**")
        }
    }
}

apply(plugin = "io.gitlab.arturbosch.detekt")
detekt {
    parallel = true
    config.setFrom(files("${project.rootDir}/config/detekt/detekt.yml"))
}