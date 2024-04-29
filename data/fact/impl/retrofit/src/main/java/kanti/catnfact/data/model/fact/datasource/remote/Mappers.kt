package kanti.catnfact.data.model.fact.datasource.remote

import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.retrofit.catfact.fact.FactDto
import java.math.BigInteger
import java.security.MessageDigest

fun FactDto.toFact(
	digest: MessageDigest
): Fact {
	val rawHash = digest.digest(fact.toByteArray())
	val bigInteger = BigInteger(1, rawHash)
	val hash = bigInteger.toString(16)
	return Fact(
		hash = hash,
		fact = fact,
	)
}