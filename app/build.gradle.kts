plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")}

android {
    namespace = "com.example.ratestation"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.ratestation"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Clave TMDB
        buildConfigField("String", "TMDB_API_KEY", "\"3ea2f57f628b9099353b5842ec2f6d68\"")
        buildConfigField(
            "String",
            "TMDB_ACCESS_TOKEN",
            "\"eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiIzZWEyZjU3ZjYyOGI5MDk5MzUzYjU4NDJlYzJmNmQ2OCIsIm5iZiI6MTc1NjgzNTI3Ny41MTksInN1YiI6IjY4YjcyZGNkNmEyMzc2NjZkYjNmMGEzYSIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.EWcx09vbagqkKWiT0qLzNHONr_kqwxyc29vbpj8E6ao\""
        )

        // Clave RAWG
        buildConfigField("String", "RAWG_API_KEY", "\"34f24367ac9e483a84cb3e2fe3a6f713\"")
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

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation(libs.firebase.firestore)
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}