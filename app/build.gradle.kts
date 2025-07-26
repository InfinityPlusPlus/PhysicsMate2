import com.android.build.api.dsl.Packaging

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.physicsmate"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.physicsmate"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        packaging {
            resources{
                //excludes += ['graphml.xsd', 'xlink.xsd', 'viz.xsd', 'gexf.xsd', 'META-INF/DEPENDENCIES']
                excludes += "graphml.xsd"
                excludes += "xlink.xsd"
                excludes += "viz.xsd"
                excludes += "gexf.xsd"
            }
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}


dependencies {

    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    implementation("androidx.navigation:navigation-fragment:2.9.2")
    implementation("androidx.navigation:navigation-ui:2.9.2")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //______________________________________________________________________________

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0") //graphs
    implementation("org.matheclipse:matheclipse-core:2.0.0") //symbolic java
    implementation("com.airbnb.android:paris:2.0.0") //styling
    implementation("com.google.android.flexbox:flexbox:3.0.0") //flexible layout, reflow
    implementation("com.github.gregcockroft:AndroidMath:ALPHA") //rendering latex


}