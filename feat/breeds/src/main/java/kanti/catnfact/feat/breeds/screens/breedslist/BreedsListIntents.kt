package kanti.catnfact.feat.breeds.screens.breedslist

sealed interface ScreenIntent

internal data object ToSettingsIntent : ScreenIntent

internal data object OnStartIntent : ScreenIntent

sealed interface BreedsIntent

internal data object OnAppendContentIntent : BreedsIntent

internal data object OnRefreshIntent : BreedsIntent

internal data class OnChangeExpandIntent(val hash: String) : BreedsIntent

internal data class OnChangeFavouriteIntent(val hash: String) : BreedsIntent