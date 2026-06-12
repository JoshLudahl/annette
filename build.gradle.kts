// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.android.built.in1.kotlin) apply false
}

extra.apply {
    set("compileSdk", 37)
    set("minSdk", 26)
    set("targetSdk", 37)
    set("javaVersion", JavaVersion.VERSION_21)
}