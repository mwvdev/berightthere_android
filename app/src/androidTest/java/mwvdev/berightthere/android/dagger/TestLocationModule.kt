package mwvdev.berightthere.android.dagger

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mwvdev.berightthere.android.service.LocationService

@Module
abstract class TestLocationModule {

    @ContributesAndroidInjector(modules = [TestRepositoryModule::class])
    abstract fun contributeLocationServiceAndroidInjector(): LocationService

}