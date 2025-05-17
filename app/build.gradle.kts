plugins {
    id("com.android.application")
}

android {
    compileSdk = 35
    defaultConfig {
        applicationId = "com.example.nhom6"
        minSdk = 23
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        namespace = "com.example.nhom6" // Thêm dòng này
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity:1.10.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.android.material:material:1.12.0")
}