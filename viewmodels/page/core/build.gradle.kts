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

    api(projects.viewmodels.component.core)

    api(platform(libs.koin.bom))
    api(libs.koin.core)

    api(libs.kotlinx.coroutines)
    api(libs.androidx.lifecycle.viewmodel)
}