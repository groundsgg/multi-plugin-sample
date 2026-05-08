// Root project. Applies the grounds-push Gradle plugin so `groundsPush`
// reads grounds.yaml from this directory and bundles the resolved JARs.
// The actual plugin code lives in the :welcome and :goodbye subprojects.
plugins {
    id("gg.grounds.push") version "0.7.0"
}

// All subprojects share the same toolchain + Paper compileOnly dep so
// they can be added to grounds.yaml without per-subproject boilerplate.
subprojects {
    apply(plugin = "java")

    extensions.configure<JavaPluginExtension> {
        toolchain.languageVersion.set(JavaLanguageVersion.of(21))
    }

    repositories {
        mavenCentral()
        // Paper API.
        maven { url = uri("https://repo.papermc.io/repository/maven-public/") }
    }

    dependencies {
        // Paper provides this at runtime; compileOnly so it's never
        // bundled into our subproject JARs.
        "compileOnly"("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    }
}

groundsPush {
    // Internal default — override with GROUNDS_API_URL when pushing
    // against a non-prod cluster.
    apiUrl.set(providers.environmentVariable("GROUNDS_API_URL").orElse("https://platform.grnds.io"))
}
