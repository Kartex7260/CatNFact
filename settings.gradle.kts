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
include(":data:retrofit:catfact")
include(":data:fact:impl:retrofit")
include(":data:app:api")
include(":data:app:impl:datastore")
include(":domain:fact")
include(":data:fact:translated:api")
include(":data:fact:translated:impl:room")
include(":data:retrofit:ya:translator")
include(":data:fact:translated:impl:retrofit")
include(":ui:breeds")
include(":feat:breeds")
include(":data:breed:api")
include(":domain:breed")
include(":data:breed:impl:room")
include(":data:breed:impl:retrofit")
include(":data:breed:translated:api")
include(":data:breed:translated:impl:room")
