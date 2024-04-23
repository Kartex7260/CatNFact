package kanti.catnfact.data.model.fact.datasource.local

import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.room.fact.FactEntity

fun FactEntity.toFact(): Fact {
	return Fact(
		hash = hash,
		fact = fact,
		isFavourite = isFavourite
	)
}

fun Fact.toEntity(isFavourite: Boolean?): FactEntity {
	return FactEntity(
		hash = hash,
		fact = fact,
		isFavourite = isFavourite ?: this.isFavourite
	)
}