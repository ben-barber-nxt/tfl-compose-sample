plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "uk.co.next.theme"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    api(projects.viewmodels.page.core)
    api(projects.viewmodels.component.stopPoint)
    api(projects.viewmodels.component.lineStatus)

    api(platform(libs.koin.bom))
    api(libs.bundles.koin)

    api(libs.androidx.core.ktx)
    api(libs.androidx.savedstate)
    api(libs.androidx.lifecycle.viewmodel.savedstate)
}