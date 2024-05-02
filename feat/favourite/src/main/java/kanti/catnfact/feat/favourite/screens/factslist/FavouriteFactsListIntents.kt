package kanti.catnfact.feat.favourite.screens.factslist

sealed interface FactIntent

internal data class OnChangeExpandIntent(val hash: String) : FactIntent

internal data class OnChangeFavouriteIntent(val hash: String) : FactIntent

internal data object OnAppendContentIntent : FactIntent

internal data object OnRefreshIntent : FactIntent