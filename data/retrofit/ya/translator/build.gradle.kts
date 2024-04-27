import java.util.Properties

plugins {
	alias(libs.plugins.androidLibrary)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.kapt)
	alias(libs.plugins.dagger.hilt)
}

android {
	namespace = "kanti.catnfact.data.retrofit.ya.translator"
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
	val yaTranslatorApiKey: String by project
	val props = Properties()
	props.load(file(yaTranslatorApiKey).inputStream())
	buildTypes.forEach {
		it.buildConfigField("String", "YA_TRANSLATOR_API_KEY", props["YANDEX_TRANSLATE_API_KEY"] as String)
		it.buildConfigField("String", "YA_CLOUD_FOLDER_ID", props["YA_CLOUD_FOLDER_ID"] as String)
	}

	compileOptions {
		sourceCompatibility = JavaVersion.VERSION_1_8
		targetCompatibility = JavaVersion.VERSION_1_8
	}
	kotlinOptions {
		jvmTarget = "1.8"
	}
	buildFeatures {
		buildConfig = true
	}
}

dependencies {

	implementation(libs.dagger.hilt.android)
	kapt(libs.dagger.hilt.android.compiler)

	implementation(libs.bundles.retrofit)
}