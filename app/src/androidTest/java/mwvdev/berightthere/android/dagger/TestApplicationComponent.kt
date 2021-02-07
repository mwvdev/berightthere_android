package mwvdev.berightthere.android.dagger

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import mwvdev.berightthere.android.injection.UtilityModule
import mwvdev.berightthere.android.injection.ViewModelBuilder
import mwvdev.berightthere.android.repository.ITripRepository
import javax.inject.Singleton

@Singleton
@Component(modules = [
    TestLocationModule::class,
    ViewModelBuilder::class,
    TestRepositoryModule::class,
    TestMainModule::class,
    TestTripModule::class,
    UtilityModule::class,
    AndroidInjectionModule::class
])
interface TestApplicationComponent : AndroidInjector<TestApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): TestApplicationComponent
    }

    val tripRepository: ITripRepository

}