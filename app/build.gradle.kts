plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("org.jlleitschuh.gradle.ktlint")
}

android {
    namespace = "id.anantyan.foodapps"
    compileSdk = 34

    defaultConfig {
        applicationId = "id.anantyan.foodapps"
        minSdk = 24
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

    // data binding ini hanya untuk library dari ImagePicker/CapturePhoto :(
    buildFeatures { dataBinding = true }

    flavorDimensions += listOf("ads_fiture")
    productFlavors {
        create("ads") {
            dimension = "ads_fiture"
            applicationIdSuffix = "ads"
            versionNameSuffix = "-ads"
            targetSdk = 34
            minSdk = 24
        }
        create("nonAds") {
            dimension = "ads_fiture"
            applicationIdSuffix = "nonAds"
            versionNameSuffix = "-nonAds"
            targetSdk = 34
            minSdk = 24
        }
    }
}

dependencies {

    api(project(":presentation"))

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")

    implementation("androidx.work:work-runtime-ktx:2.8.1")

    implementation("com.google.dagger:hilt-android:2.48.1")
    ksp("com.google.dagger:hilt-compiler:2.48.1")

    // started using technology from Google - Firebase for analytics and crashlytics needs
    implementation(platform("com.google.firebase:firebase-bom:32.6.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics-ktx")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
    android.set(true)
    ignoreFailures.set(true)
    outputToConsole.set(true)
    outputColorName.set("RED")
    disabledRules.set(setOf(""))
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}
