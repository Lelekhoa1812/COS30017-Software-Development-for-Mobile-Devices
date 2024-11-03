plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "com.example.exerciseactivitytracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.exerciseactivitytracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        javaCompileOptions {
            annotationProcessorOptions {
                arguments.put("room.schemaLocation", "$projectDir/schemas")
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }    }

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
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
//        jvmTarget = "1.8"
        jvmTarget = "11" // Align Kotlin with Java version 11, scale down to ensure compatibility
    }

    // SafeArgs Plugin
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }

    packaging {
        resources {
            // Ignore conflicts
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE.md,LICENSE,LICENSE.txt,NOTICE.md,NOTICE,NOTICE.txt}"
        }
    }

    kapt {
        correctErrorTypes = true
    }
}

dependencies {
    // General AndroidX dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.junit.ktx)

    // Room dependencies
    val room_version = "2.6.1"
    implementation(libs.androidx.room.common)
    implementation(libs.androidx.room.ktx)
    implementation("androidx.room:room-runtime:$room_version")
//    annotationProcessor("androidx.room:room-compiler:$room_version") // Use kapt
    androidTestImplementation("androidx.room:room-testing:$room_version")
    kapt("androidx.room:room-compiler:$room_version")

    // RecyclerView
    implementation(libs.androidx.recyclerview)

    // JUnit test libraries
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    // Use consistent versions for `androidx.test` and `espresso`
    val espresso_version = "3.6.1"
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.espresso.contrib)
    androidTestImplementation("androidx.test.espresso:espresso-accessibility:$espresso_version")
    androidTestImplementation("androidx.test:core:1.6.1") // Ensure consistency
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")

    // Hamcrest for matcher
    androidTestImplementation("org.hamcrest:hamcrest:2.2")
    androidTestImplementation("org.hamcrest:hamcrest-library:2.2")

    // Fragment testing
    val fragment_version = "1.8.4"
    implementation("androidx.fragment:fragment-ktx:$fragment_version")
    debugImplementation("androidx.fragment:fragment-testing:$fragment_version")

    // Navigation dependencies
    val nav_version = "2.8.2"
    implementation("androidx.navigation:navigation-fragment:$nav_version")
    implementation("androidx.navigation:navigation-ui:$nav_version")
    implementation("androidx.navigation:navigation-dynamic-features-fragment:$nav_version")
}