package mwvdev.berightthere.android.injection

import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import mwvdev.berightthere.android.*
import javax.inject.Singleton


@Singleton
@Component(modules = [
    ViewModelBuilder::class,
    DatabaseModule::class,
    NetworkModule::class,
    RepositoryModule::class,
    LocationModule::class,
    MainModule::class,
    TripModule::class,
    AndroidInjectionModule::class
])
interface ApplicationComponent : AndroidInjector<BeRightThereApplication> {

    @Component.Factory
    interface Factory : AndroidInjector.Factory<BeRightThereApplication>

}