pluginManagement {
    repositories {
        gradlePluginPortal()
        // grounds-push is published to GitHub Packages, not Gradle Plugin
        // Portal. Both repos are public; the credential block is only
        // needed for the optional GHCR pull when @groundsgg/* artifacts
        // are referenced — for the plugin itself it's read-anonymous-ok.
        maven {
            url = uri("https://maven.pkg.github.com/groundsgg/grounds-push")
            credentials {
                username = System.getenv("GITHUB_ACTOR") ?: ""
                password = System.getenv("GITHUB_TOKEN") ?: ""
            }
        }
    }
}

rootProject.name = "multi-plugin-sample"

include("welcome", "goodbye")
