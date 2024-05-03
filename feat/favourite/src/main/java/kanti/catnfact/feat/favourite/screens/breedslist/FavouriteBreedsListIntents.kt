package kanti.catnfact.feat.favourite.screens.breedslist

sealed interface BreedIntent

internal data class OnChangeExpandIntent(val hash: String) : BreedIntent

internal data class OnChangeFavouriteIntent(val hash: String) : BreedIntent

internal data object OnAppendContentIntent : BreedIntent

internal data object OnRefreshIntent : BreedIntent