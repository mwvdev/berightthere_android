package mwvdev.berightthere.android.injection

import dagger.Module
import dagger.android.ContributesAndroidInjector
import mwvdev.berightthere.android.service.LocationService

@Module
abstract class LocationModule {

    @ContributesAndroidInjector(modules = [NetworkModule::class, RepositoryModule::class, UtilityModule::class])
    abstract fun contributeLocationServiceAndroidInjector(): LocationService

}