plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    kotlin("plugin.serialization") version "1.9.10"
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("de.mannodermaus.android-junit5") version "1.9.3.0"
}

repositories {
    google()
    mavenCentral()
}

android {
    namespace = "com.akinci.doggo"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.akinci.doggo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "com.akinci.doggo.HiltTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    lint {
        abortOnError = true
        ignoreWarnings = false
        warningsAsErrors = true
        checkReleaseBuilds = false
        baseline = file("lint-baseline.xml")
    }
    testOptions {
        unitTests {
            isReturnDefaultValues = true
            all {
                it.useJUnitPlatform()
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    val lifecycleVersion = "2.6.2"
    val composeBomVersion = "2023.10.00"
    val jUnit5Version = "5.10.1"
    val coroutinesVersion = "1.7.3"
    val hiltVersion = "2.48.1"
    val composeDestinationsVersion = "1.9.54"
    val ktorVersion = "2.3.6"
    val roomVersion = "2.6.0"
    val coilVersion = "2.5.0"
    val lottieVersion = "6.2.0"

    // CORE
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.3.6")

    // IMAGE LOADING
    implementation("io.coil-kt:coil-compose:$coilVersion")

    // LOTTIE ANIMATIONS
    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    // NETWORK
    implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
    implementation("io.ktor:ktor-client-logging:$ktorVersion")

    // COROUTINES
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")

    // LIFECYCLE
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion")

    // COMPOSE
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:$composeBomVersion"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.material3:material3")

    // NAVIGATION
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("io.github.raamcosta.compose-destinations:core:$composeDestinationsVersion")
    implementation("io.github.raamcosta.compose-destinations:animations-core:$composeDestinationsVersion")
    ksp("io.github.raamcosta.compose-destinations:ksp:$composeDestinationsVersion")

    // DEPENDENCY INJECTION - HILT
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")
    ksp("com.google.dagger:hilt-android-compiler:$hiltVersion")

    // LOGGING
    implementation("com.jakewharton.timber:timber:5.0.1")

    // STORAGE - ROOM
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // UNIT TESTING
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jUnit5Version")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jUnit5Version")
    testImplementation("org.junit.jupiter:junit-jupiter-params:$jUnit5Version")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    testImplementation("io.kotest:kotest-assertions-core:5.8.0")
    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("app.cash.turbine:turbine:1.0.0")
    implementation("io.ktor:ktor-client-mock:$ktorVersion")

   /* TODO check below dependencies.
   //TEST RELATED
    def junit_5_version = "5.7.1"
    testImplementation "org.junit.jupiter:junit-jupiter-api:${junit_5_version}"
    testRuntimeOnly "org.junit.jupiter:junit-jupiter-engine:${junit_5_version}"
    testImplementation "org.junit.jupiter:junit-jupiter-params:${junit_5_version}"
    testImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2'
    testImplementation 'com.google.truth:truth:1.0.1'
    testImplementation "io.mockk:mockk:1.10.6"
    testImplementation "androidx.room:room-testing:$room_version"
    kaptTest "com.google.dagger:hilt-android-compiler:$hilt_version"
    testAnnotationProcessor "com.google.dagger:hilt-android-compiler:$hilt_version"
    debugImplementation "androidx.compose.ui:ui-test-manifest:1.0.5"

    androidTestImplementation 'junit:junit:4.13.2'
    androidTestImplementation 'androidx.test.ext:junit:1.1.3'
    androidTestImplementation 'android.arch.core:core-testing:1.1.1'
    androidTestImplementation 'androidx.test.espresso:espresso-core:3.4.0'
    androidTestImplementation 'com.google.truth:truth:1.0.1'
    androidTestImplementation 'org.jetbrains.kotlinx:kotlinx-coroutines-test:1.5.2'
    androidTestImplementation "com.google.dagger:hilt-android-testing:$hilt_version"   //hilt integration for testing
    kaptAndroidTest "com.google.dagger:hilt-android-compiler:$hilt_version"            //hilt integration for testing
    androidTestAnnotationProcessor "com.google.dagger:hilt-android-compiler:$hilt_version"
    androidTestImplementation "androidx.compose.ui:ui-test-junit4:$compose_version"
    debugImplementation "androidx.compose.ui:ui-tooling:$compose_version"
    androidTestImplementation "androidx.test:core:1.4.0"
    androidTestImplementation "androidx.test:runner:1.4.0"
    androidTestImplementation "androidx.test:rules:1.4.0"
    androidTestImplementation "androidx.test.ext:junit-ktx:1.1.3"
    androidTestImplementation "com.google.dagger:hilt-android:$hilt_version"
    */
}
