package kanti.catnfact.feat.facts.screens.randomfact

sealed interface RandomFactIntent

data class ChangeFavouriteIntent(val hash: String) : RandomFactIntent

data object ToFactListIntent : RandomFactIntent

data object NextRandomFactIntent : RandomFactIntent