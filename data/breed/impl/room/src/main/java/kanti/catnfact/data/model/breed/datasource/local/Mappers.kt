package kanti.catnfact.data.model.breed.datasource.local

import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.room.breed.BreedEntity

fun Breed.toEntity(isFavourite: Boolean = this.isFavourite): BreedEntity {
	return BreedEntity(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern,

		isFavourite = isFavourite
	)
}

fun BreedEntity.toBreed(): Breed {
	return Breed(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern,

		isFavourite = isFavourite
	)
}