// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.0.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.72")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")
    }
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

subprojects {
    plugins.withType<com.android.build.gradle.internal.plugins.BasePlugin>() {
        configure<com.android.build.gradle.BaseExtension> {
            compileSdkVersion(29)
            buildToolsVersion = "29.0.2"

            defaultConfig {
                minSdkVersion(24)
                targetSdkVersion(29)
                versionCode = 1
                versionName = "1.0"
                testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
            }

            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_1_8
                targetCompatibility = JavaVersion.VERSION_1_8
            }
        }
    }
}

tasks {
    val clean by registering(Delete::class) {
        delete(buildDir)
    }
}