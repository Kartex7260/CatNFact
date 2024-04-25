package kanti.catnfact.feat.facts.screens.factslist

sealed interface ScreenIntent

internal data object OnBackIntent : ScreenIntent

internal data object ToSettingsIntent : ScreenIntent

sealed interface FactIntent

internal data class OnChangeExpandIntent(val hash: String) : FactIntent

internal data class ChangeFavouriteIntent(val hash: String) : FactIntent

internal data object RefreshIntent : FactIntent

internal data object AppendContentIntent : FactIntent