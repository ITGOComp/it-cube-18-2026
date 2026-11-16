import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.it_cube_2026_photo"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.it_cube_2026_photo"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        // Ключ GigaChat читаем из local.properties (этот файл не попадает в git)
        val localProps = Properties()
        val localFile = rootProject.file("local.properties")
        if (localFile.exists()) localFile.inputStream().use { localProps.load(it) }
        val gigaKey = localProps.getProperty("GIGACHAT_KEY", "").trim()
        buildConfigField("String", "GIGACHAT_KEY", "\"$gigaKey\"")
    }

    buildFeatures {
        buildConfig = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.material)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.activity.compose)
}