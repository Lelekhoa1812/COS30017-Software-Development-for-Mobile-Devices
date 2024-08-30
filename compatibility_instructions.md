To solve incompatible AS dependency:
In app level build gradle:
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

In proj level build grade:
plugins {
    id("com.android.application") version "8.4.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false // or the correct Kotlin version you're using
}