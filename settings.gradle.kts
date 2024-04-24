pluginManagement {
	repositories {
		google {
			content {
				includeGroupByRegex("com\\.android.*")
				includeGroupByRegex("com\\.google.*")
				includeGroupByRegex("androidx.*")
			}
		}
		mavenCentral()
		gradlePluginPortal()
	}
}
dependencyResolutionManagement {
	repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
	repositories {
		google()
		mavenCentral()
	}
}

rootProject.name = "CatNFact"
include(":app:phone")
include(":ui")
include(":feat:settings")
include(":data:settings:api")
include(":data:settings:impl:datastore")
include(":feat:facts")
include(":ui:facts")
include(":data")
include(":data:fact:api")
include(":data:fact:impl:room")
include(":data:room")
include(":data:retrofit")
