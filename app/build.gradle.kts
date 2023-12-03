plugins {
    id("com.android.application")
}

android {
    namespace = "algonquin.cst2335.myapplication"
    compileSdk = 34

    buildFeatures{
        viewBinding = true
    }
    defaultConfig {
        applicationId = "algonquin.cst2335.myapplication"
        minSdk = 24
        targetSdk = 33
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
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.+")
    implementation("com.google.android.material:material:1.+")
    implementation("androidx.constraintlayout:constraintlayout:2.+")
    implementation("androidx.recyclerview:recyclerview-selection:1.+")
    implementation("com.android.volley:volley:1.+")
    testImplementation("junit:junit:4.+")
    androidTestImplementation("androidx.test.ext:junit:1.+")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.+")
    implementation ("org.json:json:20210307")
    val room_version = "2.4.2"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
    implementation ("com.github.bumptech.glide:glide:4.12.0")
}