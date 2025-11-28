plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    // Temporarily disabled - uncomment after adding google-services.json
    // id("com.google.gms.google-services")
}

android {
    namespace = "com.majelismdpl.majelis_mdpl"
    compileSdk = 36

    buildFeatures {
        viewBinding = true
    }

    defaultConfig {
        applicationId = "com.majelismdpl.majelis_mdpl"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    // AndroidX Core Dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Material Design
    implementation(libs.material)

    // Fragment Support (Untuk Bottom Navigation)
    implementation("androidx.fragment:fragment:1.6.2")

    // ViewPager2 untuk swipe gesture
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Firebase Dependencies
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")

    // Google Play Services untuk Login
    implementation("com.google.android.gms:play-services-auth:21.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    // Credentials untuk Login
    implementation("androidx.credentials:credentials:1.2.2")
    implementation("androidx.credentials:credentials-play-services-auth:1.2.2")

    // Retrofit untuk API (Opsional - hanya jika diperlukan)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

    // Volley untuk HTTP requests (TAMBAHAN BARU)
    implementation("com.android.volley:volley:1.2.1")

    // UI Libraries
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Testing Dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // GOOGLE OAUTH
    implementation("androidx.browser:browser:1.7.0")

    // Glide untuk loading image (SUDAH ADA)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    implementation("androidx.viewpager2:viewpager2:1.1.0")  // untuk Refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    // Pastikan Anda memiliki versi Material Components terbaru (Minimal 1.4.0)
// Versi 1.11.0, 1.12.0, atau yang lebih baru sangat disarankan.
    implementation("com.google.android.material:material:1.11.0")
// Pastikan juga appcompat Anda tidak terlalu usang
    implementation("androidx.appcompat:appcompat:1.6.1")


}
