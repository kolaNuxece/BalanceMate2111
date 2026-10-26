plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.sqldelight)

    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)

}

kotlin {
    androidTarget {
        // Правильний спосіб вказати jvmTarget для Android у KMP
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            // SQLDelight
            implementation(libs.sqldelight.runtime)
            implementation(libs.sqldelight.coroutine)

            // ОСЬ ЦЕ ТРЕБА ДОДАТИ, ЩОБ ПРАЦЮВАВ App.kt
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation("com.russhwolf:multiplatform-settings:1.1.1")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")
            implementation("com.russhwolf:multiplatform-settings:1.1.1")

            implementation(libs.androidx.datastore)
            implementation(libs.androidx.datastore.preferences)


        }
        androidMain.dependencies {
            implementation(libs.sqldelight.android)
        }
        iosMain.dependencies {
            implementation(libs.sqldelight.native)
        }
    }
}

android {
    namespace = "com.example.database"
    compileSdk = 35
    defaultConfig {
        minSdk = 24
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

sqldelight {
    databases {
        create("AppDatabase") {
            packageName.set("com.example")
        }
    }
}