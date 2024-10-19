plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.mobdeve.cherie"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.mobdeve.cherie"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        buildFeatures {
            viewBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}



dependencies {

    val fragment_version="1.6.1"

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation("androidx.fragment:fragment:$fragment_version")
    implementation(libs.circleimageview)
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))

//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.analytics)
    implementation("com.google.firebase:firebase-analytics")
    implementation(libs.material.v190)
}