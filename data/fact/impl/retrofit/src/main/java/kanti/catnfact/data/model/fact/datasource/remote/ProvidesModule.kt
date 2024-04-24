package kanti.catnfact.data.model.fact.datasource.remote

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.security.MessageDigest
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProvidesModule {

	@Provides
	@Singleton
	fun provideMessageDigest(): MessageDigest {
		return MessageDigest.getInstance("SHA-256")
	}
}