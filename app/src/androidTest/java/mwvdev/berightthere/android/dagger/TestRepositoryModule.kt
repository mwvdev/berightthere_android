package mwvdev.berightthere.android.dagger

import dagger.Binds
import dagger.Module
import mwvdev.berightthere.android.fake.FakeTripRepository
import mwvdev.berightthere.android.repository.ITripRepository

@Module
abstract class TestRepositoryModule {

    @Binds
    abstract fun provideTripRepository(repository: FakeTripRepository): ITripRepository

}