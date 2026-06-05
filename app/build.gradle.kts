plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.example"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.aistudio.ironfuelv7.pxrtmq"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "7.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-P",
            "plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)
    
    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    
    // Navigation
    implementation(libs.androidx.navigation.compose)
    
    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    
    // Networking & Serialization
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

tasks.register("copyApkToRoot") {
    outputs.upToDateWhen { false }

    val srcFile = layout.buildDirectory.file("outputs/apk/debug/app-debug.apk")
    val destFile = layout.projectDirectory.file("IronFuel_debug.apk")
    val rootDestFile = file("${project.rootDir}/IronFuel_debug.apk")

    doLast {
        val src = srcFile.get().asFile
        val dest = destFile.asFile
        val rootDest = rootDestFile
        if (src.exists()) {
            if (dest.exists()) {
                dest.delete()
            }
            src.copyTo(dest, overwrite = true)
            println("APK copied successfully to app folder: ${dest.absolutePath} (Size: ${dest.length()} bytes)")

            if (rootDest.exists()) {
                rootDest.delete()
            }
            src.copyTo(rootDest, overwrite = true)
            println("APK copied successfully to workspace root: ${rootDest.absolutePath} (Size: ${rootDest.length()} bytes)")
        } else {
            println("Source APK not found at: ${src.absolutePath}")
        }
    }
}

tasks.matching { it.name == "assembleDebug" }.all {
    finalizedBy("copyApkToRoot")
}
