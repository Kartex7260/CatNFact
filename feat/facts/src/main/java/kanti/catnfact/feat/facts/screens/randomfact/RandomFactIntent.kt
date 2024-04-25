package kanti.catnfact.feat.facts.screens.randomfact

sealed interface FactScreenIntent

internal data object ToFactListIntent : FactScreenIntent

internal data object ToSettingsIntent : FactScreenIntent


sealed interface RandomFactIntent

internal data class ChangeFavouriteIntent(val hash: String) : RandomFactIntent

internal data object NextRandomFactIntent : RandomFactIntent