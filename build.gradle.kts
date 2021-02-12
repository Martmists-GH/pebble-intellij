import org.jetbrains.intellij.tasks.PublishTask

val ideaVersion: String by project
val downloadIdeaSources: String by project
val publishUsername: String by project
val publishPassword: String by project
val publishChannels: String by project

buildscript {

    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.jetbrains.intellij") version "0.6.5"
    id("org.sonarqube") version "3.1.1"
    kotlin("jvm") version "1.4.0"
}

project(":") {
    repositories {
        mavenCentral()
    }

    dependencies {
        implementation(project(":parser")) {
            exclude(module = "antlr4")
        }
        implementation("org.antlr", "antlr4-intellij-adaptor", "0.1")
    }

    apply {
        plugin("kotlin")
        plugin("org.jetbrains.intellij")
    }

    intellij {
        version = ideaVersion
        downloadSources = downloadIdeaSources.toBoolean()
        updateSinceUntilBuild = false
        setPlugins("Spring", "java-i18n", "properties"/*, "java"*/)

        tasks.withType<PublishTask> {
            username(publishUsername)
            password(publishPassword)
            channels(publishChannels)
        }
    }

    sonarqube {
        properties {
            property("sonar.projectKey", "bjansen_pebble-intellij")
        }
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    tasks.withType(JavaCompile::class) {
        options.encoding = "UTF-8"
    }
}
