plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.kapt)
	alias(libs.plugins.dagger.hilt)
}

android {
	namespace = "kanti.catnfact.data.model.fact.datasource.remote"
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
}

dependencies {

	implementation(libs.dagger.hilt.android)
	kapt(libs.dagger.hilt.android.compiler)

	implementation(libs.androidx.annotation)

	implementation(project(":data"))
	implementation(project(":data:fact:api"))
	implementation(project(":data:retrofit:catfact"))
}