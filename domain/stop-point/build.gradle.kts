plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

dependencies {

    api(platform(libs.koin.bom))
    api(libs.koin.core)
    api(libs.ktor.client.core)

    api(projects.api.coreKtor)
    api(projects.api.modelsTfl)

    api(projects.domain.core)
}