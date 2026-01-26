plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.gr.manchid"
    compileSdk = 36

    kotlinOptions {
        jvmTarget = "11"
    }
















    defaultConfig {
        applicationId = "com.gr.manchid"
        minSdk = 26
        targetSdk = 36
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.core)
    implementation("com.airbnb.android:lottie:6.0.0")
    implementation("com.google.android.material:material:1.11.0")


    // Firebase BOM (version control yahin se)
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // Firebase services (jo use karoge wahi rakho)
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // Optional (agar chaho)
     implementation("com.google.firebase:firebase-database")
    // 🔑 Google Sign-In
    implementation("com.google.android.gms:play-services-auth:21.1.1")

    // Material Components (older but stable version)


}