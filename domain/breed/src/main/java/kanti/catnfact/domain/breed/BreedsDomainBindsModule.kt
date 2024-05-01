package kanti.catnfact.domain.breed

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.model.breed.Breed
import kanti.catnfact.data.paging.Pagination
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface BreedsDomainBindsModule {

	@Binds
	@BreedsPagingQualifier
	fun bindBreedsPaginationManager(
		manager: BreedsPagingManager
	): Pagination<Breed>
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class BreedsPagingQualifier