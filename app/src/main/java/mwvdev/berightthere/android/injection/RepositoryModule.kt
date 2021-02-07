package mwvdev.berightthere.android.injection

import dagger.Binds
import dagger.Module
import mwvdev.berightthere.android.repository.ITripRepository
import mwvdev.berightthere.android.repository.TripRepository

@Module(includes = [NetworkModule::class, UtilityModule::class])
abstract class RepositoryModule {

    @Binds
    abstract fun provideTripRepository(repository: TripRepository): ITripRepository

}