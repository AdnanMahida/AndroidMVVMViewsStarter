import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.ad.mvvmstarter"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.ad.mvvmstarter"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        flavorDimensions.add("environment")
        productFlavors {
            create("stage") {
                dimension = "environment"
                applicationId = "com.ad.mvvmstarter.stage"
                buildConfigField(
                    "String", "BASE_URL", "\"https://www.stage-server.com/\""
                )
                buildConfigField(
                    type = "String",
                    name = "CLOUDFRONT_URL",
                    value = "\"https://assets-stage-server.com/\""
                )
                resValue(
                    "string",
                    "app_name",
                    "StarterTemplateStage"
                )
                setProperty(
                    "archivesBaseName",
                    "StarterTemplate_V${defaultConfig.versionName}_${getCurrentDate()}"
                )
            }

            create("production") {
                dimension = "environment"
                applicationId = "com.ad.mvvmstarter"
                buildConfigField(
                    "String", "BASE_URL", "\"https://www.live-server.com/\""
                )
                buildConfigField(
                    type = "String",
                    name = "CLOUDFRONT_URL",
                    value = "\"https://assets-live-server.com/\""
                )
                resValue(
                    "string",
                    "app_name",
                    "StarterTemplate"
                )
                setProperty(
                    "archivesBaseName",
                    "StarterTemplate_V${defaultConfig.versionName}_${getCurrentDate()}"
                )
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        dataBinding = true
    }
    android.buildFeatures.buildConfig = true
}

// Helper function to get the current date
fun getCurrentDate(): String {
    val formatter = SimpleDateFormat(
        "dd_MM_yyyy", Locale.getDefault()
    )
    return formatter.format(Date())
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.logging.interceptor)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.security.crypto)
    implementation(libs.gson)
    implementation(libs.places)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    // Firebase
//    implementation(platform(libs.firebase.bom))
//    implementation(libs.firebase.analytics)

    // Fragment navigation
    implementation(libs.fragnav)

    // Image Loading
    implementation(libs.glide)
    annotationProcessor(libs.compiler)


    ksp(libs.ksp)

    // SDP size
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    ksp(libs.hilt.compiler)
    ksp(libs.hilt.ext.compiler)

    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.play.services.auth)

    //Timber
    implementation(libs.timber)

    // Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // WorkManager
    implementation(libs.androidx.work.runtime.ktx)

    implementation(libs.common)

    // Circle Image view
    implementation(libs.circleimageview)

    // Image Picker
    implementation(libs.imagepicker)

    // Pinch To Zoom
    implementation(libs.pinchtozoom)

    //Places SDK
    implementation(libs.places)

}
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}
