// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
}
buildscript {
    dependencies {
        // Add the SafeArgs plugin dependency
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:2.8.1")
    }
}