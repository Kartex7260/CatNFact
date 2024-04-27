package kanti.catnfact.feat.facts.screens.factslist

sealed interface ScreenIntent

internal data object OnBackIntent : ScreenIntent

internal data object ToSettingsIntent : ScreenIntent

sealed interface FactIntent

internal data class OnChangeExpandIntent(val hash: String) : FactIntent

internal data class OnChangeFavouriteIntent(val hash: String) : FactIntent

internal data object OnRefreshIntent : FactIntent

internal data object OnAppendContentIntent : FactIntent

internal data object OnReshowIntent : FactIntent