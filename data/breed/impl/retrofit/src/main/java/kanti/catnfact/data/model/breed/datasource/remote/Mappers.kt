package kanti.catnfact.data.model.breed.datasource.remote

import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.retrofit.catfact.breed.BreedDto
import java.math.BigInteger
import java.security.MessageDigest

fun BreedDto.toBreed(digest: MessageDigest): Breed {
	val rawHash = digest.digest(breed.toByteArray())
	val bigInteger = BigInteger(1, rawHash)
	val hash = bigInteger.toString(16)
	return Breed(
		hash = hash,

		breed = breed,
		country = country,
		origin = origin,
		coat = coat,
		pattern = pattern
	)
}