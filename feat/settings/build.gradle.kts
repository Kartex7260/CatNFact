plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.kapt)
	alias(libs.plugins.dagger.hilt)
}

android {
	namespace = "kanti.catnfact.feat.settings"
	compileSdk = 34

	defaultConfig {
		minSdk = 24

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
		consumerProguardFiles("consumer-rules.pro")
	}

	buildTypes {
		release {
			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
	}
	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}

	buildFeatures {
		compose = true
	}
	composeOptions {
		kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
	}
}

dependencies {

	val platform = platform(libs.compose.bom)
	implementation(platform)

	implementation(libs.compose.material3)
	implementation(libs.compose.navigation)
	implementation(libs.compose.navigation.hilt)

	implementation(libs.compose.ui.tooling.preview)
	debugImplementation(libs.compose.ui.tooling)

	implementation(libs.dagger.hilt.android)
	kapt(libs.dagger.hilt.android.compiler)

	implementation(project(":data:settings:api"))
	implementation(project(":ui"))
}