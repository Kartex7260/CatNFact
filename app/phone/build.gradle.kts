import java.util.Calendar
import java.util.Properties
import java.util.TimeZone

val calendar: Calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))

fun getVersionByCalendar(calendar: Calendar): String {
	val day = calendar.get(Calendar.DAY_OF_MONTH).toString().padStart(2, '0')
	val month = calendar.get(Calendar.MONTH).plus(1).toString().padStart(2, '0')
	val year = calendar.get(Calendar.YEAR).toString().padStart(4, '0')
	return "$year.$month.$day"
}

fun getMicroVersionByCalendar(calendar: Calendar): String {
	val hour = calendar.get(Calendar.HOUR_OF_DAY)
	val minute = calendar.get(Calendar.MINUTE)
	return (hour * 60 + minute).toString().padStart(4, '0')
}

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
		versionName = getVersionByCalendar(calendar)

		testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
	}

	buildTypes {
		release {
			signingConfig = signingConfigs["release"]

			isDebuggable = false
			applicationIdSuffix = null

			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
		debug {
			signingConfig = signingConfigs["release"]

			isDebuggable = true
			applicationIdSuffix = ".debug"
			versionNameSuffix = "-debug.${getMicroVersionByCalendar(calendar)}"

			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
		}
		create("pre-release") {
			signingConfig = signingConfigs["release"]

			isDebuggable = false
			applicationIdSuffix = ".prerelease"
			versionNameSuffix = "-pre-release"

			isMinifyEnabled = false
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

			matchingFallbacks += listOf("release")
		}
		create("debug-minify") {
			signingConfig = signingConfigs["release"]

			isDebuggable = true
			applicationIdSuffix = ".debug.minify"
			versionNameSuffix = "-debug-minify.${getMicroVersionByCalendar(calendar)}"

			isMinifyEnabled = true
			proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

			matchingFallbacks += listOf("debug")
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
	implementation(project(":data:breed:translated:impl:retrofit:libretranslate"))
	implementation(project(":data:breed:translated:impl:room"))
	implementation(project(":data:fact:impl:retrofit"))
	implementation(project(":data:fact:impl:room"))
	implementation(project(":data:fact:translated:impl:retrofit:libretranslate"))
	implementation(project(":data:fact:translated:impl:room"))
	implementation(project(":data:settings:api"))
	implementation(project(":data:settings:impl:datastore"))
	implementation(project(":feat:breeds"))
	implementation(project(":feat:facts"))
	implementation(project(":feat:favourite"))
	implementation(project(":feat:settings"))
	implementation(project(":ui"))
}