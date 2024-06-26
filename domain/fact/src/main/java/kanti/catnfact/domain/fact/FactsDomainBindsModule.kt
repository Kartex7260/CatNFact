package kanti.catnfact.domain.fact

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kanti.catnfact.data.model.fact.Fact
import kanti.catnfact.data.paging.Pagination
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
interface FactsDomainBindsModule {

	@Binds
	@FactsPagingQualifier
	fun bindFactsPagingManager(manager: FactsPagingManager): Pagination<Fact>

	@Binds
	@FavouriteFactsPagingQualifier
	fun bindFavouriteFactsPagingManager(manager: FavouriteFactsPagingManager): Pagination<Fact>
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FactsPagingQualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class FavouriteFactsPagingQualifier