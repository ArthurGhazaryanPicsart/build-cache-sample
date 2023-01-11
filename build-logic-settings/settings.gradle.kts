rootProject.name = "build-logic-settings"

pluginManagement {
    apply(from = "dependency-plugin/pluginManagement-shared.settings.gradle.kts")
}

plugins {
    id("com.gradle.enterprise").version("3.12")
    id("com.gradle.common-custom-user-data-gradle-plugin").version("1.8.1")
}

dependencyResolutionManagement {

    repositories {
        mavenCentral()
        google()
        gradlePluginPortal()
    }
}

gradleEnterprise {
    server = "https://gradle.com"
    allowUntrustedServer = true

    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
        isUploadInBackground = true
        capture { isTaskInputFiles = true }
        publishAlways()
        // need to cast here to be able to use publishIfAuthenticated
        this as com.gradle.enterprise.gradleplugin.internal.extension.BuildScanExtensionWithHiddenFeatures
        publishIfAuthenticated()
        obfuscation {
            ipAddresses { addresses -> addresses.map { "0.0.0.0" } }
        }
    }
}

buildCache {
    local {
        isEnabled = true
        isPush = true
        removeUnusedEntriesAfterDays = 4
    }

    remote(gradleEnterprise.buildCache) {
        server = "https://gradle.com"
        path = "/cache/"
        isEnabled = true
        isPush = true
    }
}

fun remoteBuildCacheEnabled(settings: Settings) = settings.buildCache.remote?.isEnabled == true


include("dependency-plugin")
