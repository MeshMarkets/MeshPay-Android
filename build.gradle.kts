plugins {
    id("java-library")
    kotlin("jvm") version "2.0.0"
}

repositories { mavenCentral() }

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
}
