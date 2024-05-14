plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("plugin.serialization") version "1.9.21"
    id("kotlin-kapt")
    id("com.google.devtools.ksp") version "1.9.21-1.0.15"

}

android {

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }

    namespace = "no.uio.ifi.IN2000.team24_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.uio.ifi.IN2000.team24_app"
        minSdk = 26 //Build.VERSION_CODES.O everywhere
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.compose.ui:ui:1.6.5")
    implementation ("androidx.compose.foundation:foundation:1.6.5")



    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.test:core-ktx:1.5.0")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    implementation("androidx.media3:media3-test-utils:1.3.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-android:2.8.0-rc01")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    debugImplementation("androidx.compose.ui:ui-tooling")


    //ktor
    val ktor_version = "2.3.8"
    implementation("io.ktor:ktor-client-core:$ktor_version")
    implementation("io.ktor:ktor-client-android:$ktor_version")
    implementation("io.ktor:ktor-client-content-negotiation:$ktor_version")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktor_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
    implementation("io.coil-kt:coil-compose:2.5.0")


    //location
    implementation ("com.google.android.gms:play-services-location:21.2.0")
    implementation ("com.google.accompanist:accompanist-permissions:0.24.13-rc")
    val activity_version = "1.8.2"
    implementation("androidx.activity:activity-ktx:$activity_version")
    implementation ("com.google.accompanist:accompanist-permissions:0.35.0-alpha")

    val nav_version = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")


    // RoomDb dependencies:
    val room_version = "2.6.1"

    implementation("androidx.room:room-runtime:$room_version")
    //annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")
    // To use Kotlin Symbol Processing (KSP)
    //ksp("androidx.room:room-compiler:$room_version")

    """    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")

    // optional - RxJava2 support for Room
    implementation("androidx.room:room-rxjava2:$room_version")

    // optional - RxJava3 support for Room
    implementation("androidx.room:room-rxjava3:$room_version")

    // optional - Guava support for Room, including Optional and ListenableFuture
    implementation("androidx.room:room-guava:$room_version")

    // optional - Test helpers
    testImplementation("androidx.room:room-testing:$room_version")

    // optional - Paging 3 Integration
    implementation("androidx.room:room-paging:$room_version")
"""
    val espressoVersion = "3.4.0"
    val androidxTestVersion = "1.4.0"

// For instrumental tests
    androidTestImplementation("androidx.test:runner:$androidxTestVersion")
    androidTestImplementation("androidx.test:rules:$androidxTestVersion")

// Optional -- UI testing with Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:$espressoVersion")

// Optional -- UI testing with UI Automator
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")

// Optional -- UI testing with Compose
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.1.0-alpha04")

}