pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
}

rootProject.name = "NextTFLSample"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

include(":app")
include(":api:models-tfl")
include(":api:core-ktor")
include(":api:core-json")
include(":domain:stop-point-search")
include(":domain:core")
include(":viewmodels:page:core")
include(":viewmodels:component:core")
include(":viewmodels:component:search")
include(":viewmodels:page:search")
include(":ui:stop-search")
include(":ui:theme")
include(":domain:stop-point")
include(":viewmodels:component:stop-point")
include(":viewmodels:page:stop-point")
include(":viewmodels:component:line-status")
