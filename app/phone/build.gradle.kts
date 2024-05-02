import java.util.Properties

plugins {
	alias(libs.plugins.androidApplication)
	alias(libs.plugins.jetbrainsKotlinAndroid)
	alias(libs.plugins.kapt)
	alias(libs.plugins.dagger.hilt)
}

android {
	namespace = "kanti.catnfact"
	compileSdk = 34

	val signingProperties: String by project
	val file = file(signingProperties)
	val keyStoreProps = Properties()
	keyStoreProps.load(file.inputStream())

	signingConfigs {
		create("release") {
			storeFile = file(keyStoreProps["FILE"] as String)
			storePassword = keyStoreProps["PASSWORD"] as String
			keyAlias = keyStoreProps["ALIAS"] as String
			keyPassword = keyStoreProps["ALIAS_PASSWORD"] as String
		}
	}

	defaultConfig {
		applicationId = "kanti.catnfact"
		minSdk = 24
		targetSdk = 34
		versionCode = 1
		versionName = "1.0"

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			signingConfig = signingConfigs["release"]
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
	implementation(libs.compose.activity)
	implementation(libs.compose.navigation)

	implementation(libs.compose.ui.tooling.preview)
	debugImplementation(libs.compose.ui.tooling)

	implementation(libs.dagger.hilt.android)
	kapt(libs.dagger.hilt.android.compiler)

	implementation(project(":data:app:impl:datastore"))
	implementation(project(":data:breed:impl:retrofit"))
	implementation(project(":data:breed:impl:room"))
	implementation(project(":data:breed:translated:impl:retrofit"))
	implementation(project(":data:breed:translated:impl:room"))
	implementation(project(":data:fact:impl:retrofit"))
	implementation(project(":data:fact:impl:room"))
	implementation(project(":data:fact:translated:impl:retrofit"))
	implementation(project(":data:fact:translated:impl:room"))
	implementation(project(":data:settings:api"))
	implementation(project(":data:settings:impl:datastore"))
	implementation(project(":feat:breeds"))
	implementation(project(":feat:facts"))
	implementation(project(":feat:favourite"))
	implementation(project(":feat:settings"))
	implementation(project(":ui"))
}