package kanti.catnfact.feat.facts.screens.randomfact

sealed interface FactScreenIntent

data object ToFactListIntent : FactScreenIntent

data object ToSettingsIntent : FactScreenIntent


sealed interface RandomFactIntent

data class ChangeFavouriteIntent(val hash: String) : RandomFactIntent

data object NextRandomFactIntent : RandomFactIntent

data object InitialRandomFactIntent : RandomFactIntent